# Mimir

Horizontally scalable, multi-tenant, Prometheus-compatible time-series
database. What Grafana Cloud runs under the hood for metrics.

## Why it exists

Prometheus caps out at ~10M active series per process and stores data on
local disk. A fleet of 10+ microservices plus infra metrics across a handful
of environments blows past that. You hit three walls simultaneously:

1. **Scale**: one Prometheus can't ingest or query enough.
2. **Retention**: local SSD is expensive and finite. 13 months of fleet data
   won't fit.
3. **HA + multi-tenant**: two Prometheus replicas diverge; there is no native
   tenant isolation.

Mimir solves all three by decomposing Prometheus's storage + query engine into
~10 stateless (or shard-stateful) services, backed by object storage (S3/GCS).

## How it's used

From the outside, Mimir **is** Prometheus:

- Writes arrive via the **Prometheus remote_write** protocol (Alloy, the
  Prometheus agent, Grafana Agent, or Prometheus itself pushing).
- Reads are **PromQL** via `/api/v1/query` and friends — identical contract.

What changes operationally:

- **You don't scrape from Mimir.** Mimir receives pushed samples. Scraping
  lives at the edge (Alloy).
- **Tenant isolation is first-class.** Each write and read carries an
  `X-Scope-OrgID` header; Mimir keeps data partitioned per tenant. Grafana
  Cloud is a Mimir with thousands of tenants.

## When to use it

- Many services, many clusters, many environments.
- Long retention required (13 months is Grafana Cloud's default).
- HA at the metrics layer is non-negotiable.
- Multi-tenant needs (one platform serves several teams, or you sell a SaaS
  and want per-customer metrics).

If you have a single cluster, single team, and can live with local-disk
retention → **stay on Prometheus**. Mimir's operational cost is real.

## Alternatives

| Option | Why pick it |
|---|---|
| **Prometheus** | Single-box, trivial ops. The baseline. |
| **VictoriaMetrics (cluster)** | Fewer components (4 vs 10), faster ingest per core, Prom-compatible |
| **Thanos** | Retrofit onto existing Prometheus fleet; sidecar + compactor + query federation |
| **Cortex** | Mimir's predecessor (same team forked Mimir from Cortex); maintained but Mimir is the strategic path now |
| **Datadog Metrics / Chronosphere / Honeycomb** | Managed, proprietary, fast to adopt, expensive at scale |
| **Amazon Managed Prometheus (AMP)** | AWS-native Cortex fork; fine if all-in on AWS |

## Tradeoffs

- **+** Scales to billions of active series.
- **+** Object storage for bulk data → cheap long retention.
- **+** Built-in HA via write replication (factor 3).
- **+** Multi-tenant out of the box.
- **+** 100% PromQL compatibility (passes Prometheus conformance suite).
- **−** ~10 separate components to deploy, scale, and monitor. Real operational
  complexity — mitigated only by using Grafana Cloud / managed.
- **−** Higher total resource cost than a single Prometheus for the same load
  at small scale.
- **−** Query paths traverse multiple network hops (frontend → scheduler →
  querier → ingester/store-gateway), so P99 latency > Prometheus on tiny queries.

## How it works — general

**Mimir = Prometheus's storage + query engine, split into microservices and
backed by object storage.** No scraping. Receives writes via remote_write.
Writes fan out across sharded **ingesters** (memory + WAL), which flush
**TSDB blocks** to S3/GCS every ~2 hours. Reads merge recent data from
ingesters with historical data from object storage.

```
Alloy                                                  S3 / GCS
  │ remote_write                                    (TSDB blocks)
  ▼                                                       ▲
Distributor ──▶ Ingester (×N, RF=3) ──flush 2h──────────┘
                    │                             ▲
Query Frontend ─▶ Scheduler ─▶ Querier ───────────┤   │
                                    ▲             │   │
                                    └ merges recent (ingester) + historical (store-gw)
Compactor, Ruler, Alertmanager — background
```

## How it works — detailed

### Write path

1. **Distributor** (stateless): validates samples, enforces per-tenant limits,
   hashes `(tenant, label-set)` → picks N ingesters via consistent hashing
   (replication factor, typically 3), forwards the batch.
2. **Ingester** (stateful): appends to local WAL, holds recent samples in
   memory as a TSDB head. Serves recent-data queries directly from memory.
3. Every ~2h, ingesters flush a **TSDB block** (same format Prometheus writes
   locally) to object storage.

### Read path

1. **Query Frontend** (stateless): caches results, **splits** long-range queries
   into many parallel sub-queries (30 days → 30×1-day), queues to Scheduler.
2. **Query Scheduler** (stateless): work queue between frontend and queriers;
   provides backpressure.
3. **Querier** (stateless): executes each sub-query. Asks the hash ring which
   ingesters own the queried series → fetches recent data from them. Asks the
   **Store Gateway** for older data. Merges and deduplicates samples (since
   each sample exists on 3 replicas), runs PromQL, returns result.
4. **Store Gateway** (semi-stateful): holds in memory the **index-headers** and
   bloom filters for all blocks in object storage, fetches minimum byte ranges
   from S3 for a query. Makes S3 reads feel like local SSD.

### Background

- **Compactor**: merges small 2h blocks into bigger ones (24h → weekly),
  deduplicates across replicas, drops expired blocks per retention policy.
- **Ruler**: evaluates recording rules and alert rules on schedule. Recording
  rules store their result as new series. Alert rules fire on PromQL
  expressions being truthy for N minutes.
- **Alertmanager**: multi-tenant; routes, deduplicates, groups alerts to
  destinations (Slack, PagerDuty, webhooks).

## How it works — under the hood

### Hash ring

All ingesters, store gateways, and compactors register themselves in a
**consistent hash ring** stored in a KV backend (etcd, Consul, memberlist).
Every component reads the ring to figure out "who owns this series?" or
"who owns this block?". Adding or removing ingesters rebalances ownership
gradually without data loss.

### Replication factor

Write RF=3 means each sample is written to 3 ingesters. Read queries fetch
from all 3 and dedupe. Any one ingester can fail without data loss; two can
fail before a write is rejected (quorum).

### Object storage as the backbone

Blocks are immutable. Object storage (S3, GCS, Azure Blob, MinIO) is the
long-term, durable, cheap store. Ingesters are effectively a **write buffer**
that protects against 2h of data loss on crash; blocks are the real archive.

A Mimir cluster with 1 PB of metrics in S3 is cheap. The same 1 PB on
local SSDs would be an expensive disaster.

### Blocks storage format

Same as Prometheus TSDB: meta.json + chunks + index. Each chunk ~120 samples,
delta+XOR encoded, ~1.3 bytes/sample average. The index is an inverted
index: `label_name=value → list of series IDs → list of chunk refs`.

Store Gateway fetches only the index-header (a compact subset of the full
index), keeps it in memory per block, and uses it to decide which chunk files
and byte ranges to fetch from S3 for a given query.

### Query caching

- **Result cache** (Memcached/Redis): caches final query results keyed by
  (query-string, time-window, step). Dashboards re-opening the same time
  window hit this.
- **Chunks cache**: caches chunk bytes fetched from S3. Two queries hitting
  the same chunk hit cache, not S3.
- **Metadata cache**: label and series lookups.

### Ruler behavior

Recording rules are evaluated by the Ruler on schedule. Example:

```promql
# Recording rule
record: job:http_request_rate:5m
expr: sum by (job) (rate(http_server_requests_seconds_count[5m]))
```

Each evaluation writes a new series `job:http_request_rate:5m` back to Mimir.
Dashboards query the recorded series instead of recomputing the 5-min rate
live — faster, cheaper. Worth adopting for any query that shows up in 10+ dashboards.

### What you run yourself

If self-hosting: all ~10 components as Kubernetes Deployments/StatefulSets
plus object storage (MinIO or cloud bucket). Helm chart covers it, but
operational burden is real.

If using Grafana Cloud: **nothing**. You push to an endpoint and query a URL.
The 10 components are their problem. You pay per active series + per sample
ingested + per query GB scanned.
