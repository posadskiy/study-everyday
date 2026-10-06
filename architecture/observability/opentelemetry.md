# OpenTelemetry (OTel)

Vendor-neutral instrumentation framework and wire protocol (**OTLP**). The
industry's answer to "please stop locking telemetry into proprietary agents."

## Why it exists

Pre-OTel, every APM vendor had its own SDK, agent, and protocol. Switching
from New Relic to Datadog meant rewriting instrumentation in every service.
Tracing had two competing standards (OpenTracing, OpenCensus). Metrics had
Prometheus client libs in every language. Logs had none of this.

OpenTelemetry unifies:

- **One SDK per language** with auto-instrumentation for common frameworks
  (HTTP, gRPC, JDBC, Kafka, ...).
- **One wire protocol (OTLP)** for all three signals (traces, metrics, logs).
- **One collector** (OTel Collector / Alloy) that receives OTLP, transforms,
  and exports to any backend.

Net effect: instrument once, switch backends freely.

## How it's used

### In application code (usually invisible)

For Micronaut: add `micronaut-tracing-opentelemetry-http` + `opentelemetry-exporter-otlp`.
No code changes beyond dependencies. Auto-instrumentation:

- Instruments every inbound HTTP request as a **server span**.
- Instruments every outbound HTTP / JDBC / gRPC call as a **client span**.
- Propagates W3C `traceparent` headers across service boundaries.
- Injects `traceId` / `spanId` into SLF4J MDC so logs carry them.

Manual spans (only when auto isn't enough):

```java
@Inject Tracer tracer;

Span span = tracer.spanBuilder("recalculate-rollup").startSpan();
try (Scope s = span.makeCurrent()) {
    span.setAttribute("tenant.id", tenantId);
    // work
} finally {
    span.end();
}
```

### Configuration (env-var driven)

```
OTEL_SERVICE_NAME=costy-api
OTEL_EXPORTER_OTLP_ENDPOINT=http://grafana-alloy.observability.svc.cluster.local:4317
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
OTEL_TRACES_SAMPLER=parentbased_traceidratio
OTEL_TRACES_SAMPLER_ARG=1.0
```

## When to use it

- **Always**, for any new service. It's the default correct answer for
  instrumentation in 2025+.
- **Replacing** proprietary SDKs in existing services when vendor churn is
  possible or tracing/metrics are fragmented across teams.

Don't use OTel:

- For pure OS-level metrics (CPU, disk) — `node_exporter` is fine.
- For the JVM's own intrinsics (GC, heap) — Micrometer exposes them via
  Prometheus format, no need to pipe through OTel.

## Alternatives

| Option | Why pick it |
|---|---|
| **Datadog tracer / New Relic agent** | Best-in-class UX with their backend; vendor lock-in |
| **Jaeger client SDKs** | Legacy; migrating to OTel is the path forward |
| **Zipkin B3 propagation + custom SDKs** | Legacy; superseded by OTel |
| **Prometheus client libs + custom logging** | Only if you don't need distributed tracing — fine for metrics-only scenarios |

Most shops running OTel still use Prometheus client libs (e.g. Micrometer) for
metrics because ecosystem maturity is higher and scraping is already wired.
OTLP metrics exist and work; adoption is growing but not universal.

## Tradeoffs

- **+** Vendor-neutral: swap Grafana Cloud ↔ Datadog ↔ self-hosted without
  touching app code.
- **+** Auto-instrumentation for common libraries is broad and high-quality.
- **+** Context propagation (traceparent) is standardized (W3C) and cross-language.
- **+** Logs, metrics, traces unified under one SDK (though in practice many
  use Micrometer for metrics + OTel for traces — that's fine).
- **−** More moving parts than a single vendor SDK. The Collector, SDK,
  instrumentation libraries all evolve.
- **−** Occasional version mismatches between SDK and Collector show up as
  "unknown protobuf field" warnings (benign thanks to protobuf forward-compat,
  but confusing).
- **−** The OTel spec is large and evolves quickly; keeping up with best
  practices (semantic conventions, resource attributes) takes effort.

## How it works — general

SDK in the app produces telemetry objects → exporter serializes them to OTLP
protobuf → sent over gRPC/HTTP to a Collector (Alloy in your case) → Collector
batches/transforms/exports to one or more backends (Tempo, Prometheus/Mimir,
Loki).

```
  App (Java)
    │ auto-instrumentation + SDK
    ▼
  Spans / Metrics / Logs (in-memory)
    │ BatchSpanProcessor
    ▼
  OTLP exporter (protobuf over gRPC)
    │
    ▼
  Collector (Alloy) ─ batch, transform, route
    │
    ▼
  Backends (Tempo / Mimir / Loki)
```

## How it works — detailed

### Tracing SDK

A **Tracer** produces **Spans**. Spans have:

- `trace_id` (16 bytes) — identifies the whole distributed operation.
- `span_id` (8 bytes) — identifies this one operation.
- `parent_span_id` — links to the span that caused this one (empty for the
  root span).
- `name`, `start_time`, `end_time`, `kind` (server/client/internal/producer/consumer).
- `attributes` — key/value metadata (e.g. `http.method=POST`, `db.statement=...`).
- `events` — timestamped within-span entries (exception stack traces live here).
- `status` — OK / ERROR.
- `resource` — attributes of the **emitting process** (service.name, k8s.pod.name,
  host.name). Attached once per SDK instance, not per span.

### Context propagation

When service A calls service B via HTTP, OTel injects a W3C header:

```
traceparent: 00-{trace_id}-{span_id}-01
```

Service B's instrumentation reads this header on the inbound request, starts
a new span with the same `trace_id` and the received `span_id` as
`parent_span_id`. That's how traces span services.

Same for gRPC (metadata), Kafka (message headers), and async frameworks.

### Exporters and batching

Spans are not sent one-by-one. The SDK has a **BatchSpanProcessor**:

- In-memory queue (bounded, drops spans under pressure).
- Flush on size (512 spans) or time (5s), whichever first.
- Serialize batch as protobuf `ExportTraceServiceRequest`.
- gRPC POST to the collector.

### Metrics SDK

**Instruments**: `Counter`, `UpDownCounter`, `Gauge`, `Histogram`, async variants.
Each instrument is tied to a **Meter** (named scope, usually per-library).

**Views** let you configure aggregations (change histogram bucket boundaries,
drop specific attributes to reduce cardinality).

Metrics flow via an OTLP exporter (OTLP/gRPC or OTLP/HTTP) to a collector,
which can then re-export in Prometheus format. Common alternative:
PrometheusExporter directly in-process, scraped by Alloy — simpler, and what
your Micronaut stack does via Micrometer.

### Logs SDK

Newer than traces/metrics. Most mature integration pattern today: leave logs
in their existing logger (Logback, log4j), emit JSON to stdout, let Alloy
pick them up via file tailing. OTLP logs exist but aren't as widely adopted.

### Sampling

Three models:

- **Head sampling** (in the SDK): decide at span start. Cheap, predictable,
  can lose interesting traces. `parentbased_traceidratio(1.0)` = keep
  everything the root decided to keep.
- **Tail sampling** (in the Collector): buffer spans per trace, decide after
  the root ends based on trace properties ("was it slow?", "did it error?").
  Expensive, gives better signal-to-noise.
- **Probabilistic** at any point: keep N%.

Rule: head-sample 100% until you hit ingestion cost; then move to tail
sampling with "keep errors + slow + sample of fast" policy in the Collector.

## How it works — under the hood

### Wire format (OTLP)

Protobuf. The schema lives in `opentelemetry-proto`:

```protobuf
message Span {
  bytes trace_id = 1;
  bytes span_id = 2;
  bytes parent_span_id = 4;
  string name = 5;
  fixed64 start_time_unix_nano = 7;
  fixed64 end_time_unix_nano = 8;
  repeated KeyValue attributes = 9;
  repeated Event events = 11;
  Status status = 15;
  SpanKind kind = 6;
}
```

gRPC streaming: one TCP connection, HTTP/2, concurrent requests, backpressure
via flow control. Typical throughput: 10k+ spans/sec per SDK client without
breaking a sweat.

### Instrumentation libraries

Framework-specific wrappers that hook into the framework's extension points:

- **Micronaut tracing**: intercepts HTTP server filters, HTTP client filters,
  JDBC data sources.
- **Java agent** (`opentelemetry-javaagent.jar`): bytecode manipulation at
  class-load time; works without code changes at all for hundreds of libraries.
- **Spring Boot**: `opentelemetry-spring-boot-starter` auto-wires.

### Resource detection

On SDK startup, `Resource.Builder` collects identity: `service.name`,
`service.version`, `deployment.environment`, `host.name`, `k8s.pod.name`,
`k8s.namespace.name`, `process.pid`, etc. These attach to every span/metric
the SDK emits and surface as labels in the backend.

### Semantic conventions

OTel specifies **standard attribute names** so different languages produce
consistent data: `http.request.method`, `http.response.status_code`,
`db.system`, `messaging.system`, etc. Dashboards built on these conventions
work across Java, Go, Python services without special-casing.

Honor the conventions — query writers everywhere will thank you.

### Collector vs direct export

You can make services export **directly to the backend** (e.g. straight to
Tempo). But a collector in the middle buys:

- Retry and buffering (service outage doesn't lose spans).
- Sampling / filtering (tail sampling).
- Routing (send errors to tool A, everything to tool B).
- Authentication (services hold no vendor credentials).
- Protocol bridging (OTLP in → Prometheus out).

Always put a collector in the path. Alloy or OTel Collector.
