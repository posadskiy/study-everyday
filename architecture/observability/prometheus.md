# Prometheus

Pull-based time-series database + query engine. The single-process baseline
that everyone else is measured against.

## Why it exists

Before Prometheus, metrics were overwhelmingly **push-based** (StatsD, Graphite).
Push means: services fire-and-forget at an aggregator that may or may not be
up. Prometheus inverted the model — the server **pulls** from passive
endpoints on a schedule. That inversion gives:

- **Discoverability**: the server knows what it scrapes; if a target is down,
  `up == 0` is itself a signal.
- **Resilience**: if the server dies, targets keep serving their current
  values; no data loss of intermediate rates.
- **Simplicity in apps**: instrumentation just updates in-memory counters;
  nobody ever blocks on a network call to "send a metric."

## How it's used

1. Each service exposes `/metrics` via a client library (Micrometer, prom-client, etc.).
2. A `prometheus.yml` config lists scrape targets or a service-discovery mechanism.
3. Prometheus scrapes every ~15–60s, stores samples locally, serves PromQL at
   `/api/v1/query` and `/api/v1/query_range`.
4. Grafana is configured with Prometheus as a data source and runs PromQL.

## When to use it

- **One cluster, single-tenant, moderate scale** (< ~10M active series, < 1M samples/sec ingest).
- Retention needs are days to weeks, fits on local SSD.
- You don't need high availability at the metrics layer, or can accept
  "run two independent replicas, tolerate slight divergence."

Beyond that — reach for **Mimir**, **VictoriaMetrics**, **Thanos**, or a
managed service.

## Alternatives

| Option | Why pick it |
|---|---|
| **Mimir** | Horizontal scale, multi-tenant, long retention in object storage |
| **VictoriaMetrics** | Single-binary, higher ingest than Prom, Prom-compatible; operationally simple but less ecosystem |
| **Thanos** | Retrofit long retention + global query over existing Prometheus fleet |
| **InfluxDB** | Different data model (tags vs labels); push-heavy workloads; Flux query language |
| **Datadog Metrics** | Fully managed, push-based, proprietary; fastest to set up, expensive |
| **CloudWatch / Stackdriver** | Native in AWS/GCP; poor query language; fine for minimal ops overhead |

## Tradeoffs

- **+** Trivial operational model: one process, local disk, a YAML config.
- **+** PromQL is the de-facto standard; skills transfer everywhere.
- **+** Enormous exporter ecosystem (databases, message queues, hardware).
- **−** Single-box scale ceiling. Sharding is manual.
- **−** No native HA; running two replicas means queries may return slightly
  different answers between them (Prometheus is eventually-consistent across
  independent replicas — you either pick one as source of truth or dedupe
  at query time with Thanos/Mimir).
- **−** Retention is bounded by local disk. Remote write is bolt-on.
- **−** Pull model needs network reachability from server to targets —
  awkward for transient jobs, easy in a mesh/k8s, fine for VM fleets with
  discovery.

## How it works — general

One Go binary that does three jobs concurrently:

1. **Scraper** — pulls `/metrics` from targets on a schedule.
2. **TSDB** — stores time series on local disk in 2-hour "blocks."
3. **Query engine** — executes PromQL against the TSDB; serves HTTP API.

```
Targets (/metrics)
     │ scrape_interval
     ▼
  ┌─────────────────────────────────────────┐
  │  Prometheus binary                      │
  │  ┌─────────┐  ┌──────┐   ┌─────────────┐│
  │  │ Scraper │─▶│ TSDB │◀──│ Query engine││──▶ HTTP API (Grafana)
  │  └─────────┘  └──────┘   └─────────────┘│
  │                   │                     │
  │                   ▼                     │
  │            /var/lib/prometheus          │
  │            (WAL + blocks on SSD)        │
  └─────────────────────────────────────────┘
```

## How it works — detailed

### Scraping

- Each scrape job has its own goroutine pool.
- For each target: HTTP GET `/metrics` → parse text-based exposition format
  (counters, gauges, histograms, summaries) → append samples to the TSDB
  with timestamp = scrape start time.
- Failed scrape → `up{job=...,instance=...}` sample set to 0.
- Staleness: if a series stops being reported, Prometheus inserts a special
  "stale" marker 5 minutes later so PromQL doesn't keep returning old values.

### Storage (TSDB)

- In memory: a **head block** accumulates recent samples. Writes also go to a
  WAL (write-ahead log) so crashes don't lose data.
- Every 2 hours (by default), the head block is persisted to disk as an
  immutable **block**: a directory containing an index + chunks + meta.
- Compactor merges small blocks into bigger ones over time (2h → 24h → up to 31d).
- Retention: old blocks outside the retention window are deleted.

### Query engine

- PromQL parser → AST → execution plan.
- Time range query: iterate each time step (step = resolution), resolve matching
  series from the TSDB index, run the expression, return matrix/vector.
- `rate()`, `histogram_quantile()`, aggregation operators all operate on the
  series stream — no full materialization unless necessary.

## How it works — under the hood

### TSDB block layout

```
/var/lib/prometheus/
  wal/                                   # write-ahead log (recent, pre-block)
  01H....XXXX/                           # 2h block (ULID-named)
    meta.json
    chunks/000001                        # compressed sample chunks
    index                                # inverted index: label → series IDs → chunk refs
    tombstones
```

An **index** maps `label=value` pairs to series IDs (inverted index like a search
engine). A **chunk** holds ~120 samples for one series, encoded with delta +
XOR compression (Gorilla paper). Typical compression: **~1.3 bytes per sample**
on average after compaction — exceptional density.

### Counter vs gauge vs histogram

- **Counter**: monotonically increasing integer (`http_requests_total`). Only
  useful through `rate()` / `increase()`. Reset to 0 on process restart; PromQL
  handles resets automatically.
- **Gauge**: current value (CPU, queue depth). Use directly.
- **Histogram**: a family of counters — one per bucket. `_bucket{le="0.5"}`,
  `_bucket{le="1.0"}`, `_count`, `_sum`. Quantiles computed with
  `histogram_quantile(0.95, sum by (le) (rate(x_bucket[5m])))`.
- **Summary**: pre-computed quantiles client-side. Cannot be aggregated across
  instances. Avoid in distributed systems — use histogram.

### Remote write

A bolt-on push path. Prometheus can forward every sample it scrapes to a
remote endpoint (Mimir, Thanos Receive, VictoriaMetrics) using a snappy-compressed
protobuf `WriteRequest`. This is the bridge from the pull-based local store
to horizontally scalable remote backends. Alloy uses the same protocol to
ship to Grafana Cloud.

### Federation

A second Prometheus can scrape `/federate?match[]=...` on a first Prometheus to
pull aggregated series. Useful for hub-and-spoke topologies, but brittle at
scale — prefer remote_write to Mimir instead.

## Interop with Mimir

PromQL and the HTTP API are 100% compatible. Migration from Prometheus to
Mimir is transparent at the query layer — swap the Grafana data source URL.
The *write* side changes: you stop storing locally and start remote_writing
(via Alloy or Prometheus itself).
