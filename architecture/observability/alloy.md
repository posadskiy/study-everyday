# Grafana Alloy

Telemetry collection agent. The only observability process you run inside your
own cluster. Everything else lives in Grafana Cloud.

## Why it exists

Historically, each signal needed its own agent: **Prometheus** for metrics,
**Promtail/Fluent Bit** for logs, **OpenTelemetry Collector** for traces.
Three deployments, three configs, three OOM kills to debug. Alloy merges them
into one binary with a unified pipeline language (River).

It solves:

- One agent, one config format, one set of CPU/memory limits to reason about.
- A pipeline model (components connected by named outputs) that lets you mix
  signals — e.g. derive metrics from logs, or generate service-graph metrics
  from traces — without leaving the agent.

## How it's used

Deployed inside Kubernetes in **two shapes simultaneously**:

| Shape | Purpose |
|---|---|
| **DaemonSet** — one pod per node | Tails container log files on the local node's filesystem |
| **Deployment / StatefulSet** — a few HA replicas | Scrapes `/metrics` across the cluster; receives OTLP pushes from services |

Typical flow:

```
Pods (/prometheus)   ──scrape──▶  Alloy  ──remote_write──▶  Mimir
stdout JSON logs     ──tail────▶  Alloy  ──loki.write────▶  Loki
Services (OTLP gRPC) ──push────▶  Alloy  ──OTLP HTTP─────▶  Tempo
```

## When to use it

- Kubernetes-based microservice systems emitting **more than one signal**.
- You are (or plan to be) a Grafana Cloud customer. Alloy's batteries are
  tilted toward Grafana components, though OTLP output is standard.
- You want a single agent to own every stdout line and every metric endpoint
  in the cluster instead of three sidecars.

Don't use it for a single-process monolith on one VM — plain Prometheus or
`node_exporter` alone is simpler.

## Alternatives

| Option | When it wins |
|---|---|
| **Prometheus agent mode** | Metrics only; tiny footprint; already standardized on Prom |
| **OpenTelemetry Collector** | Multi-vendor strategy; want "pure" upstream OTel components only |
| **Fluent Bit** | Logs only; non-Kubernetes environments; embedded devices |
| **Vector** | Complex log transformations (rich DSL); mixed sources (Kafka, files, syslog) |
| **Datadog Agent / New Relic Infrastructure** | Proprietary stack; convenience over neutrality |

## Tradeoffs

- **+** One process, one config, fewer moving parts in production.
- **+** Rich Kubernetes-native discovery (`discovery.kubernetes` is first-class).
- **+** Supports the full OTel pipeline — it's a drop-in OTel Collector under the hood.
- **−** River config is proprietary-ish (HCL-like) — learning curve vs YAML.
- **−** Tilted toward Grafana components (e.g. `loki.process`, `prometheus.remote_write`);
  you can still export OTLP, but it's not vendor-neutral by default.
- **−** One binary owning three pipelines means a bug in the log processor can
  starve the metrics scraper. Monitor Alloy itself.

## How it works — general

Alloy is a **pipeline runtime**. You declare components in River; each component
has named outputs wired as inputs to the next component. Think Unix pipes with
types. A component can be:

- A discovery source (`discovery.kubernetes`, `discovery.relabel`)
- A receiver (`otelcol.receiver.otlp`, `loki.source.kubernetes`)
- A processor (`loki.process`, `otelcol.processor.batch`)
- An exporter (`prometheus.remote_write`, `loki.write`, `otelcol.exporter.otlphttp`)

At runtime, components run as independent goroutines communicating through
channels. If any component's config changes, only its subgraph is restarted.

## How it works — detailed

### Metrics pipeline

1. `discovery.kubernetes` watches the kube API for pods matching a namespace filter.
2. `discovery.relabel` keeps only pods with `prometheus.io/scrape=true`, extracts
   `prometheus.io/path` and `prometheus.io/port` from annotations.
3. `prometheus.scrape` issues HTTP GETs on those endpoints every scrape interval
   (typically 60s), parses the Prometheus exposition format, and produces samples.
4. `prometheus.remote_write` batches samples, snappy-compresses them into
   protobuf `WriteRequest`, and POSTs over HTTPS to Mimir with bearer auth.

### Logs pipeline

1. `loki.source.kubernetes` discovers pods, resolves their log files on the node
   (`/var/log/pods/<ns>_<pod>_<uid>/<container>/0.log` — symlinks to container
   runtime logs).
2. An `inotify`-driven tailer wakes on file modifications, reads new bytes from
   the last recorded offset, and persists the offset to a **positions file**
   (survives restarts, prevents duplicates and gaps).
3. `loki.process` runs pipeline stages: JSON parse → extract `level`, `traceId`,
   `spanId` → promote `level` to a label, `traceId`/`spanId` to structured
   metadata, drop healthcheck noise.
4. `loki.write` batches and ships to Loki.

### Traces pipeline

1. `otelcol.receiver.otlp` opens a gRPC listener on :4317 and an HTTP listener
   on :4318. Services push spans here.
2. `otelcol.processor.batch` groups spans into batches (10s timeout or size cap)
   to avoid hammering Tempo per-span.
3. `otelcol.exporter.otlphttp` forwards batches to Tempo's OTLP endpoint.

## How it works — under the hood

- **Log tailing** uses Linux `inotify` (kernel file-change notifications). One
  watcher per tailed file. When kubelet appends, the kernel wakes Alloy; the
  tailer reads forward, runs the processing pipeline, and batches for write.
  On pod deletion, kubelet closes the file; Alloy flushes and stops the tailer.
- **Positions file** is a tiny YAML/JSON file stored on a persistent volume for
  the DaemonSet. `file_path → byte_offset`. On restart, Alloy resumes from the
  recorded offset. Corruption causes duplicates or gaps — rare but documented.
- **Scrape pool** is a per-target goroutine with its own HTTP client, staggered
  to spread load. Failed scrapes produce an `up=0` sample (invaluable for
  alerting on scrape-down).
- **Remote write** uses a WAL on local disk: every sample is appended before
  send. On network failure, samples buffer to disk and replay on reconnect.
  This is why disk sizing matters for Alloy even though it doesn't "store"
  anything long-term.
- **OTLP gRPC receiver** is the upstream OTel Collector receiver embedded
  verbatim. Streaming RPC; backpressure flows back to callers via gRPC flow
  control if the batch processor is full.
- **Resource-consumption budget** on healthy clusters: ~200–500 MB RAM per
  DaemonSet pod, a few hundred MB for the Deployment pods. Primary OOM
  drivers: very wide log lines, regex-heavy `loki.process` stages, or
  unbounded scrape target growth.

## Operational gotchas

- **Namespace discovery is silent when wrong.** `discovery.kubernetes` only
  watches namespaces you list. A new namespace is invisible until added.
- **`/prometheus` must be unauthenticated** for Alloy to scrape, or you need
  explicit bearer auth in the scrape config. With Micronaut Security, whitelist
  the endpoint with `isAnonymous()`.
- **River configs are hot-reloaded** on file change — edit carefully, a bad
  config reload can stop a pipeline mid-flight.
