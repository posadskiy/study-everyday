# Loki

Log aggregation system that indexes **only labels**, not content. Optimized
for cost at scale and tight integration with metrics and traces.

## Why it exists

Elasticsearch / Splunk index every word of every log line. That gives
fast arbitrary-text search but is expensive: the inverted index is often
larger than the raw data, and ingestion CPU is high. For most real incidents,
you already know *which service* you're investigating — you don't need to
grep every word of every log.

Loki inverts the tradeoff:

- **Index only labels** (service, namespace, pod, level) — small, cheap.
- **Store raw log lines** compressed in object storage — nearly free.
- **Filter content at query time** (brute-force on already-narrowed data).

Result: ~10–100× cheaper storage per GB logged; forces you to design a
sensible label taxonomy up front.

## How it's used

1. An agent (Alloy, Promtail, Fluent Bit) tails container stdout, extracts
   a small set of **labels**, optionally parses structure, and ships lines.
2. Loki stores lines compressed, indexed by label set.
3. Query via **LogQL**: `{stream-selector} | line-filter | parser-filter`.
4. Correlate to traces by clicking `trace_id` in structured metadata → jumps
   to Tempo via Grafana's derived fields.

Typical LogQL:

```logql
{service="costy-api", level="ERROR"}
  | json
  | http_status >= 500
  |= "NullPointerException"
```

## When to use it

- Kubernetes or container-first environments where stdout is already
  structured (or easily made so).
- Cost-sensitive log retention (terabytes per month).
- You're using (or will use) Grafana for metrics + traces — correlation is
  the main payoff.

Don't use Loki for:

- Security/SIEM use cases where you need full-text search across years of
  data with ad-hoc text queries. Use Elasticsearch / Splunk / OpenSearch.
- Log analytics where you join logs to other datasets with SQL. Use
  ClickHouse or BigQuery.

## Alternatives

| Option | Why pick it |
|---|---|
| **Elasticsearch / OpenSearch** | Full-text indexing, SIEM, advanced analytics; ~10× more expensive |
| **Splunk** | Enterprise feature set, SPL; most expensive |
| **ClickHouse** | Columnar analytics SQL; excellent for log analytics but heavier ops |
| **CloudWatch Logs** | AWS-native, minimal setup, query language is weak, costs add up |
| **Datadog Logs** | Managed, excellent UX, expensive |
| **Vector + object storage only** | Ship logs to S3/GCS parquet, query ad-hoc with DuckDB/Athena; ultra-cheap but no real-time UX |

## Tradeoffs

- **+** Dramatically cheaper storage than word-indexed stores.
- **+** Labels match Kubernetes metadata naturally.
- **+** Tight correlation with Mimir/Tempo: same labels, one-click jumps.
- **+** LogQL can derive metrics from logs (see below).
- **−** Arbitrary text search across **all** services is slow or blocked.
  You must know your labels.
- **−** Cardinality footguns: a `user_id` or `trace_id` label destroys Loki.
  Use structured metadata instead.
- **−** Not a SIEM. Compliance retention + content search needs another tool.

## How it works — general

Every log line belongs to exactly one **stream**, identified by its unique
set of labels. Lines for a stream accumulate in memory, are sealed as
**chunks** (~1.5 MB compressed, ~1 hour), and flushed to object storage.

An **index** maps `{labels}` → list of chunk references. Queries hit the
index to narrow chunks, then decompress and filter content.

```
Log line (JSON)
    │
Labels extracted (service, level, pod)
    │
    ▼
Stream (unique label set) ── chunk rolling in memory
                                │  size/time threshold
                                ▼
                        Sealed chunk (immutable)
                                │
                                ▼
                         Object storage (S3)
                          + index (chunk refs)
```

## How it works — detailed

### Ingestion

- Agent ships lines with labels to Loki's **Distributor**.
- Distributor hashes labels → routes to N ingesters (replication factor).
- **Ingester** accumulates lines per stream in memory. When a chunk hits
  ~1.5 MB compressed, ~1 h old, or the stream goes idle, the chunk is sealed
  and flushed to object storage. Index entries (chunk ref, time range, stream
  labels) go to the index store.

### Index vs content

The index is a compact mapping `label_name=value → list of chunk refs`.
Historically backed by Cassandra/BoltDB; modern Loki (TSDB schema) uses
the same TSDB format as Prometheus, stored in object storage.

Content (the log lines themselves) lives inside chunks. Chunks sit in object
storage as compressed blobs.

### Query execution

For `{service="costy-api", level="ERROR"} |= "NPE"`:

1. **Index lookup**: intersect streams matching `service=costy-api` and
   `level=ERROR` → set of stream IDs.
2. **Chunk lookup**: for each stream, which chunks overlap the query's time
   range? → list of chunk refs.
3. **Fetch**: parallel byte-ranged GETs from object storage (or chunk cache).
4. **Decompress** relevant sub-blocks of each chunk (blocks are
   independently compressed, so partial-overlap queries only decompress the
   needed slices).
5. **Filter**: line-filter `|= "NPE"` scans decompressed lines; `| json`
   parses and filters further; structured-metadata filters run on columnar
   data inside blocks.

### Structured metadata

High-cardinality keys like `trace_id` and `span_id` must not be labels —
each unique value creates a new stream. Loki provides **structured metadata**:
key-value pairs stored *alongside* each line in a separate columnar section
of the chunk. Not indexed, but query-time filtering on them is efficient
(reads only the metadata column, not the full line).

Rule of thumb:

| Data | Where to put it |
|---|---|
| Low cardinality (<1000 unique values): `service`, `level`, `env` | **Label** |
| High cardinality but needs filtering: `trace_id`, `user_id`, `request_id` | **Structured metadata** |
| Bulk content: message, stack trace, body | **Log line** |

### Metrics from logs

LogQL can aggregate over matching lines:

```logql
sum by (service) (
  rate({service=~".+", level="ERROR"}[5m])
)
```

Returns a time series: ERRORs/sec per service. Fed directly into dashboards
or alerts. Useful when you don't have a Prometheus counter for some event
but you do log it.

## How it works — under the hood

### Chunk internals

A chunk is not a single compressed blob. It's a sequence of **blocks** plus a
block index:

```
Chunk file:
  Header
  Block 1 (lines t1..tN, compressed independently, ~256 KB uncompressed)
  Block 2 (lines tN+1..tM, compressed independently)
  Block 3 ...
  Block index (timestamp range → byte offset of each block)
```

Partial-overlap time queries decompress only needed blocks. Structured
metadata columns inside each block enable filter-without-full-parse.

### Tailing logs

Alloy's `loki.source.kubernetes` tails `/var/log/pods/<ns>_<pod>_<uid>/<container>/0.log`
using Linux `inotify`. On append, the kernel wakes the agent; the agent reads
forward from a persisted offset (positions file), runs the processing
pipeline, and pushes to Loki's Distributor.

### Decompression is in-memory, not on disk

Chunks stay compressed in the chunk cache (Memcached). For each query, the
querier decompresses relevant blocks in RAM, filters, emits results, releases
the buffer. No temp files on disk. CPU is the cost, not I/O.

### Cardinality math

Streams = unique combinations of labels. If you have:

- 15 services × 4 pods × 3 levels = **180 streams** → fine.
- Add `trace_id` as a label: 180 × millions = **billions of streams** →
  Loki index collapses, ingest rate-limited.

This is the single most expensive mistake you can make. Always use structured
metadata for IDs.

### Tiered caching

| Cache | Contents | Purpose |
|---|---|---|
| **Result cache** | Final query results | Dashboards re-open, instant |
| **Chunk cache** | Raw compressed chunks | Avoid S3 round-trips |
| **Index cache** | Stream → chunk lookups | Faster metadata queries |

Queries are fast when they hit caches. The hot path is "Grafana dashboard
for the last hour" — entirely served from caches, no S3 GET.

### Operational sizing (self-hosted)

For ~50 MB/s log ingest and 30 daily users:

- 3 ingesters × 8–12 GB RAM
- 3–6 queriers × 2–4 GB RAM
- 2 store gateways × 2 GB
- Distributor, frontend, compactor: smaller
- Memcached (chunk + index cache): 2 × 4 GB
- Total: ~60–70 GB cluster RAM

Horizontal scaling is the only tuning lever. If one pod OOMs, add more pods;
don't grow pod size.

### Operational gotchas

- **Broad queries are rejected**: Grafana Cloud caps max bytes read per query
  to prevent runaway scans. `{namespace="prod"} |= "error"` over 30 days will
  fail.
- **Narrow time windows**: habit of starting at "last 15 min" is 10× cheaper
  than "last 24 h."
- **Label hygiene review**: periodically list active streams with the
  `/loki/api/v1/labels` API; catch new high-cardinality labels before
  they blow up.
