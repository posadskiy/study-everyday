# Tempo

Distributed trace backend. Stores spans indexed by `trace_id`, optimised for
cheap long retention on object storage.

## Why it exists

Earlier trace backends (Jaeger, Zipkin) relied on Elasticsearch or Cassandra.
Both are expensive at the volumes a busy microservice fleet produces
(thousands of spans per second per service), and they index more than you
usually need — attribute search that rarely gets used.

Tempo takes the Loki playbook and applies it to traces:

- **Primary lookup is `trace_id`** — when you have the ID (from a log or a
  click), you don't need a heavy index.
- **Everything else sits in object storage** — cheap, scalable, long retention.
- **TraceQL** provides attribute-level queries without pre-indexing every
  attribute (it scans bloom-filtered blocks).

## How it's used

1. Services instrument with OpenTelemetry (or any OTLP-compatible SDK).
2. Spans are exported via **OTLP gRPC** to the nearest collector (Alloy or
   OTel Collector) which batches and forwards.
3. Tempo receives, stores spans in object storage, and serves queries:
   - `trace_id` lookup (primary use case)
   - **TraceQL** attribute search: `{ service.name="costy-api" && status=error }`
   - **Service graph** — topology auto-built from span parent/child relationships
   - **Span metrics** — RED metrics (rate, errors, duration) derived from spans
     and pushed back into Mimir

## When to use it

- Distributed systems — any time one user action crosses more than one service,
  database call, or async message.
- Debugging latency breakdowns ("the request took 800ms — where?").
- Any time you've wired logs with `trace_id` in MDC — Tempo becomes the
  correlated endpoint for every log line.

For a literal monolith with no downstream dependencies, you don't strictly
need tracing; metrics + profiling cover it. But the second you have two
services, instrument.

## Alternatives

| Option | Why pick it |
|---|---|
| **Jaeger** | OSS, OpenTelemetry-native, pluggable storage (ES/Cassandra/memory); heavier ops if you want long retention |
| **Zipkin** | Simpler, older, smaller feature set |
| **Datadog APM** | Best-in-class UX, built-in analytics, proprietary, expensive |
| **New Relic Distributed Tracing** | Similar to Datadog in positioning |
| **Honeycomb** | High-cardinality event model, strong querying; different mental model (wide events, not spans) |
| **AWS X-Ray / GCP Cloud Trace** | Cloud-native, minimal setup, weaker ecosystem |

## Tradeoffs

- **+** Object storage = extremely cheap long retention.
- **+** Designed around OpenTelemetry; no custom SDKs required.
- **+** Service graph and span metrics are automatic add-ons.
- **+** Tight Grafana integration: log → trace → log correlation in one click.
- **−** TraceQL across large time ranges is slower than a pre-indexed
  trace store (Jaeger on ES is faster for "find all traces where
  user.id=42" over 30 days — but you pay for that index every day).
- **−** Attribute queries require block scanning; bloom filters help but
  don't eliminate it.
- **−** Sampling strategy is your problem — high-traffic services may need
  to downsample. Default 100% only works at modest scale.

## How it works — general

Spans are **pushed** (not pulled — unlike metrics). OTLP is the protocol.
Tempo batches, writes compressed blocks to object storage, and serves queries
by loading blocks and filtering.

```
Services (OTLP) ──▶ Alloy ──OTLP─▶ Distributor ──▶ Ingester ──flush──▶ Object storage
                                                       │                     │
                                       Querier ◀───────┘                     │
                                          │                                  │
                                          ▼                                  │
                                    TraceQL, trace_id lookup, service graph ◀┘
```

## How it works — detailed

### Ingestion

1. **Distributor** receives OTLP batches, validates, routes spans to ingesters
   via consistent hashing on `trace_id` (all spans of a trace go to the same
   ingester set for locality).
2. **Ingester** buffers spans by `trace_id` in memory. When a trace is
   "complete" (idle timeout — spans stopped arriving) or the flush interval
   hits, spans are grouped into a **block** and written to object storage.
3. A block contains many traces; each block has an index (trace_id → offset)
   and a bloom filter over attribute values.

### Reads

Two primary query types:

**Trace lookup by ID** (cheap):

1. Query bloom filters of recent + historical blocks to narrow.
2. Byte-ranged GET into the matching block at the trace's offset.
3. Return the span tree.

Typical latency: tens of ms on cached blocks, a few hundred ms on cold.

**TraceQL** (attribute search):

```traceql
{ span.http.status_code >= 500 && resource.service.name="costy-api" }
```

1. Scan bloom filters for candidate blocks.
2. Download candidate blocks (byte-ranged).
3. Parse spans; filter by attribute predicates; return matching traces.

Expensive over wide time ranges; narrow aggressively by service and time.

### Service graph

Tempo observes `parent → child` span relationships and builds a service graph
in the background: an auto-generated topology of which services call which,
with latency and error-rate edges. Written as Mimir metrics
(`traces_service_graph_request_total`, `_failed`, `_duration_seconds_bucket`).

### Span metrics

The same pipeline derives RED metrics per service + operation — rate, errors,
duration histograms — from the span stream and writes them to Mimir. You get
Prometheus-style dashboards for services that don't even expose `/metrics`.
"Tracing gives you metrics for free" is a real win.

## How it works — under the hood

### Block storage format

Tempo blocks are Parquet-based (modern vParquet versions) or older
custom formats. Each block:

- **Object storage path**: `<tenant>/blocks/<uuid>/`
- Files: `data.parquet` (spans), `index` (trace_id → row offset), `meta.json`,
  `bloom-0..N` (bloom filters on attributes).

Parquet is columnar: TraceQL filters (e.g. `status >= 500`) read only the
`status` column, skipping the rest of the span payload. Same principle as
ClickHouse or BigQuery.

### Consistent hashing on trace_id

All spans of a trace land on the same ingester set. This matters because:

- Traces must be reconstructed (spans arrive out of order) — local to one
  ingester makes assembly cheap.
- Block locality — all spans of a trace in the same block = fast trace lookup.

### Bloom filters

Each block ships with bloom filters over high-cardinality attribute values
(service names, HTTP status, specific `resource.*` fields). TraceQL uses them
to skip blocks that definitely don't contain matches. Reduces S3 GETs
dramatically on filtered queries.

### Retention and compaction

Same story as Mimir/Loki:
- **Compactor** merges small blocks into larger ones.
- Blocks outside retention are deleted from object storage.
- Grafana Cloud default: 30 days for traces (configurable).

### Sampling

Sampling happens **before** Tempo, in the SDK / agent:

- **Parent-based, ratio** (`parentbased_traceidratio` at 1.0) — what the
  Micronaut config uses: head sampling, 100% kept.
- Reduce to 0.1 / 0.2 only if ingestion volume forces it.
- **Tail sampling** (keep only errors, or slow traces) — done by the OTel
  Collector / Alloy processor, optional and configurable.

Best practice: head-sample 100%, tail-sample errors at 100% and slow spans at
100%, downsample healthy fast traces to 10–20%. Keeps all interesting data.

### Operational sizing

Self-hosted Tempo for a moderate fleet (~10k spans/sec):

- 2–3 distributors
- 3 ingesters (RF=3)
- 3 queriers
- 1 compactor
- Metrics-generator (span metrics + service graph) — 1–2 pods
- Total: moderate. Object storage handles the bulk.

Grafana Cloud: you don't size it. You pay per GB ingested.

### Correlation is the killer feature

- **Logs → Trace**: Loki shows `trace_id` in structured metadata → click → Tempo trace.
- **Trace → Logs**: Tempo span → "Logs for this span" button → Loki query with
  `trace_id=... AND span_id=...`.
- **Metrics → Trace** (via exemplars): histogram buckets can carry a
  `trace_id` exemplar; a p99 latency spike in a dashboard links to an actual
  slow trace.
- **Frontend → Backend**: Faro injects W3C `traceparent` in every fetch; the
  browser span and the server span share `trace_id`.

This is the payoff of having one platform: every click jumps across signals.
