# Observability

Production-grade observability for polyglot microservice systems, built on the
**Grafana Cloud** stack (Alloy, Mimir, Loki, Tempo, Pyroscope, Faro) with
OpenTelemetry as the instrumentation standard.

## The four signals

| Signal | What it answers | Storage | Query language |
|---|---|---|---|
| **Metrics** | "Is something wrong, broadly?" | Mimir (Prometheus-compatible) | PromQL |
| **Logs** | "What did a specific request do?" | Loki | LogQL |
| **Traces** | "Where did the latency go across services?" | Tempo | TraceQL |
| **Profiles** | "Which line of code is hot?" | Pyroscope | flamegraph / PQL |

Frontend real-user data (RUM, Web Vitals, JS errors, browser traces) is a
separate ingestion path through **Faro** that fans out into the same backends.

## Reference architecture

```
  ┌──────────────────────────────────────────────────────────────────────┐
  │  YOUR KUBERNETES CLUSTER                                             │
  │                                                                      │
  │   Micronaut svc  ──/prometheus──┐                                    │
  │   Micronaut svc  ──/prometheus──┤    scrape 60s                      │
  │   Micronaut svc  ──/prometheus──┤                                    │
  │                                 ▼                                    │
  │   Micronaut svc  ──OTLP gRPC─▶  ALLOY  (DaemonSet + Deployment)      │
  │                                  │                                   │
  │   stdout JSON logs  ──tail────▶  │                                   │
  │                                  │                                   │
  │                                  ▼                                   │
  │                         remote_write / loki.write / OTLP HTTP        │
  └──────────────────────────────────│───────────────────────────────────┘
                                     │ HTTPS + token
                                     ▼
  ┌──────────────────────────────────────────────────────────────────────┐
  │  GRAFANA CLOUD (managed)                                             │
  │                                                                      │
  │   Mimir  ◀── metrics       Loki  ◀── logs       Tempo  ◀── traces   │
  │   Pyroscope  ◀── profiles  Faro Collector  ◀─── browser RUM         │
  │                                                                      │
  │   Grafana UI  ─ PromQL / LogQL / TraceQL / Flamegraphs / Alerts     │
  └──────────────────────────────────────────────────────────────────────┘
                                     ▲
                                     │ direct POST (no Alloy in the loop)
                                     │
  ┌──────────────────────────────────│───────────────────────────────────┐
  │  BROWSER (React / Vite / Next)   │                                   │
  │   @grafana/faro-* SDK  ──────────┘                                   │
  │   Web Vitals, JS errors, fetch traces with traceparent               │
  └──────────────────────────────────────────────────────────────────────┘
```

Two truths drive everything:

1. **Alloy is the only agent inside the cluster.** One binary handles scraping,
   log tailing, and OTLP reception; it ships to Grafana Cloud over HTTPS.
2. **Labels unify the pillars.** `service`, `namespace`, `pod`, `trace_id`,
   `span_id` are consistent across Mimir/Loki/Tempo, which is what makes
   one-click correlation work.

## Components in this module

| File | Role |
|---|---|
| [alloy.md](./alloy.md) | Telemetry collection agent (scrape, tail, receive OTLP, ship everything) |
| [prometheus.md](./prometheus.md) | Pull-based TSDB for metrics — the single-box baseline |
| [mimir.md](./mimir.md) | Horizontally scalable, multi-tenant Prometheus (what Grafana Cloud runs) |
| [loki.md](./loki.md) | Label-indexed log store — cheap at scale, different model from Elasticsearch |
| [tempo.md](./tempo.md) | Trace store — optimized for `trace_id` lookup, object-storage backed |
| [opentelemetry.md](./opentelemetry.md) | Vendor-neutral instrumentation + OTLP wire protocol |
| [protobuf.md](./protobuf.md) | Serialization format behind OTLP, gRPC, Prometheus remote_write |
| [faro.md](./faro.md) | Frontend RUM SDK + collector (browser observability) |
| [pyroscope.md](./pyroscope.md) | Continuous profiling — the signal Prometheus can't give you |

## When to reach for which tool

- **Dashboards and alerts** on service health → metrics (Mimir / PromQL).
- **Root cause of a specific failed request** → logs (Loki / LogQL), then jump
  to the matching trace by `trace_id`.
- **Latency breakdown across services** → traces (Tempo / TraceQL).
- **"CPU is hot — which method?"** → profiles (Pyroscope).
- **Real user performance in the browser** → Faro (Web Vitals, errors, sessions).
- **"My vendor lock-in is a smell"** → OpenTelemetry SDKs + OTLP; swap the
  backend (Grafana Cloud → Datadog → self-hosted) without touching app code.

## Operational notes

- **Alloy crashes = telemetry stops for that node.** Monitor Alloy itself;
  its own `/metrics` endpoint goes through the same pipeline.
- **Namespace discovery is the #1 silent failure.** If a service's namespace
  isn't in Alloy's `discovery.kubernetes.namespaces`, it won't appear in
  Grafana — config looks fine, UI is empty.
- **Cardinality kills.** Never put `user_id`, `trace_id`, `request_id` in
  Prometheus labels or Loki labels. Use structured metadata (Loki) or span
  attributes (Tempo).
- **Sampling is a cost lever, not a correctness lever.** Default to 100% for
  traces and RUM until ingestion volume forces a reduction.
