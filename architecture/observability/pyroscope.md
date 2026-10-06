# Pyroscope

Continuous profiling backend. Stores periodic stack-sample snapshots from
running processes and renders **flamegraphs over time**. The signal that
metrics and traces can't give you: "which line of code is hot?"

## Why it exists

Metrics and traces tell you **something is slow or expensive**. Neither
answers **where in the code**. A p95 latency spike tells you an endpoint got
slow; a trace shows a 400ms span; but inside that span, **which methods are
responsible** is invisible without profiling.

Traditional profiling is ad-hoc: connect a profiler to production for 30s,
read a flamegraph, disconnect. It finds regressions only when you're
already looking. Continuous profiling runs 24/7 at low overhead, so:

- You can compare `now` vs `last week` flamegraphs.
- Regressions after a deploy are visible without a manual session.
- Incident investigations gain a fourth data source alongside metrics,
  logs, traces.

## How it's used

1. Attach a profiling agent to each service:
   - **Java**: `pyroscope.jar` Java agent, or `pyroscope-agent` via
     OpenTelemetry, or Grafana Alloy with `pyroscope.java` component.
   - **Go, Python, Ruby, .NET, Node, Rust**: language-specific agents.
   - **eBPF**: Alloy or Parca can sample entire hosts without in-process
     agents (Linux kernel 4.14+).
2. Agent samples stack traces every ~10ms, aggregates locally, ships to
   Pyroscope every ~10s.
3. In Grafana → Explore → Profiles data source, you get flamegraphs,
   timeline views, and diff views.

## When to use it

- **Continuous performance awareness** — always on.
- **Post-deploy regression checks** — "CPU/allocations higher after
  release v1.4?"
- **Incident debugging** — "the service is slow *right now* — which method?"
- **Capacity planning** — "which services would benefit most from
  optimization?"

Don't run Pyroscope:

- For short-lived serverless functions (overhead per invocation is
  noticeable; JFR's startup cost is higher than the lambda's runtime).
- In environments with strict CPU quotas where even 1% overhead matters
  (rare in practice).

## Alternatives

| Option | Why pick it |
|---|---|
| **Datadog Continuous Profiler** | Managed, strong JVM/.NET support, integrated with Datadog APM, expensive |
| **async-profiler** ad-hoc | Free, excellent quality, JVM-only, manual invocation |
| **Java Flight Recorder (JFR)** | Built into JVM, low overhead, but no continuous aggregation — you deal with `.jfr` files manually |
| **Parca** | Open-source eBPF-based continuous profiling, runs as a cluster service |
| **Polar Signals Cloud** | Commercial eBPF continuous profiling |
| **perf / flamegraph.pl** | Classic Linux profiling, manual workflow |
| **Google Cloud Profiler / AWS CodeGuru** | Cloud-native alternatives |

## Tradeoffs

- **+** Very low overhead: typically <1% CPU with 10ms sample interval.
- **+** Aggregation is continuous and queryable — not one-off sessions.
- **+** Same Grafana UI as the rest of your stack; shared labels and time
  navigation.
- **+** Diff view lets you compare two time windows or two versions.
- **−** Storage and ingest cost are real for large fleets — profiles are
  bulkier than metrics.
- **−** Another agent to deploy and keep updated per language runtime.
- **−** eBPF profiling requires kernel support and, on managed Kubernetes
  sometimes, additional privileges.
- **−** Flamegraphs take training to read — developers must invest in the
  skill for the data to pay off.

## How it works — general

Agents periodically sample the call stack of every thread (or CPU, with
eBPF). Samples are aggregated into a tree of `(function, file, line) → count`.
The tree is serialized (typically as a **pprof** protobuf) and pushed to
Pyroscope, which stores time-windowed profiles indexed by labels
(`service`, `pod`, `env`).

```
JVM / Go process                      Host (via eBPF)
   │ async-profiler                      │ perf events
   ▼                                     ▼
Stack samples                       Stack samples (kernel)
   │ aggregate every 10s                 │ aggregate every 10s
   ▼                                     ▼
pprof protobuf payload              pprof protobuf payload
   │                                     │
   └──────────▶ Pyroscope ◀───────────────┘
                    │
                    ▼
              Grafana → flamegraph / diff / timeline
```

## How it works — detailed

### Sampling approach

- **JVM agents** use async-profiler underneath: safepoint-free stack walking
  via `AsyncGetCallTrace`, CPU cycles via `perf_events`, allocations via
  JVMTI. Very low overhead.
- **eBPF agents** attach to the kernel's `perf_events` subsystem, sample
  every CPU periodically. No application instrumentation. Sees kernel frames
  and native code; requires symbol resolution on the agent side.
- Frequency: typically 100Hz (10ms intervals). Aggregate over 10s windows.

### What gets sampled

- **CPU** time (where threads are on-CPU).
- **Wall** time (also includes blocked time).
- **Allocations** (JVM JVMTI; Go alloc profile).
- **Lock contention** (JVM).
- **Memory in-use** (Go heap profile).

Each is a distinct profile type, labeled `__profile_type__="cpu"`,
`"inuse_objects"`, etc.

### Storage

Profiles are stored in object storage, indexed by:

- `service.name`
- Environment labels (`env`, `namespace`, `version`)
- Profile type (`cpu`, `wall`, `alloc_objects`, ...)
- Time range

Similar design to Loki chunks / Tempo blocks: cheap long-term storage,
time + label index on top.

### Query model

You select a **time range**, a **service label**, and a **profile type**.
Pyroscope aggregates all samples in that window into a single flamegraph.
Common views:

- **Flamegraph**: box per function; width = fraction of samples; stacked by
  call relationships.
- **Timeline**: CPU fraction over time — see spikes.
- **Diff**: two time windows (or two versions) subtracted — "what got
  hotter?"

### Correlation with traces

Modern Pyroscope supports **span profiles**: spans carry profile references,
so clicking a slow Tempo span surfaces the profile captured during that
span. Requires compatible agent + trace instrumentation wiring. Still
maturing across languages.

## How it works — under the hood

### pprof format

Both Pyroscope and its competitors use the **pprof** protobuf (invented by
Google for Go). Key message:

```protobuf
message Profile {
  repeated ValueType sample_type = 1;   // e.g. ["cpu", "nanoseconds"]
  repeated Sample sample = 2;           // each: stack (list of locations) + values
  repeated Mapping mapping = 3;         // shared libraries / JIT code ranges
  repeated Location location = 4;       // addresses and lines
  repeated Function function = 5;       // function names, file, start line
  repeated int64 string_table = 6;      // interned strings
  int64 time_nanos = 9;
  int64 duration_nanos = 10;
}
```

Strings are interned into a single table, then referenced by index.
Locations reference functions by ID; mappings identify which binary a
location is in. Encoding is extremely compact.

### Symbol resolution

- **JIT-compiled JVM**: agent resolves at sample time (has access to JVM
  internals).
- **Native binaries**: need symbols (debug info) either on the agent host
  or uploaded to Pyroscope. Stripped binaries give addresses without names.
- **Go**: symbols embedded in binary; eBPF agents can resolve by reading
  Go's runtime structures.

### Low overhead

The primary cost of sampling is:

- Interrupting a thread to read its stack (async-profiler skips safepoints
  via AsyncGetCallTrace, the reason it's low overhead on JVM).
- Hashing the stack for aggregation (cheap).
- Periodic network flush (tiny).

Result: <1% CPU overhead at 100Hz sampling on typical JVM workloads. eBPF
profiling has similar overhead, measured at the host level.

### Deployment patterns

- **Sidecar agent**: run agent as a separate container in the pod, sample the
  main container. Works well on Kubernetes.
- **Java agent**: `-javaagent:pyroscope.jar` at JVM startup. Embedded in the
  app process; one fewer container.
- **Alloy with pyroscope components**: discover Kubernetes pods, sample via
  eBPF from an Alloy DaemonSet, push to Pyroscope. Zero app-side config.

### Writing flamegraphs

Each sample's stack is hashed; identical stacks increment a counter.
Rendered by stacking function boxes, width proportional to count. Time
aggregation merges adjacent samples.

Reading flamegraphs:

- Wide boxes at the bottom/middle of a stack = hot call sites.
- `BigDecimal.multiply` being 18% of a service's CPU = probably worth
  optimizing.
- Compare view ("diff flamegraph"): green = cheaper after change, red =
  more expensive. Invaluable during performance work.

### What you'd add to your Micronaut stack

One dependency or one agent attach (`-javaagent:pyroscope.jar`), plus a few
env vars:

```
PYROSCOPE_APPLICATION_NAME=costy-api
PYROSCOPE_SERVER_ADDRESS=https://profiles-prod-<region>.grafana.net
PYROSCOPE_BASIC_AUTH_USER=<user>
PYROSCOPE_BASIC_AUTH_PASSWORD=<glc token>
PYROSCOPE_FORMAT=pprof
```

Small investment, outsized payoff during performance incidents.
