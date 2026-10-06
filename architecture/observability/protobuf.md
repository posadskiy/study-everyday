# Protocol Buffers (Protobuf)

Compact, strongly-typed, schema-driven binary serialization format. Google's
answer to "JSON is too slow and too loose for machine-to-machine traffic."

## Why it exists

JSON is great for humans and browsers. For internal service traffic it's
expensive:

- Text-based — 2–5× larger than needed.
- Untyped — `"id": "42"` vs `"id": 42` is a runtime surprise.
- Field names transmitted every message — wasted bytes on repeated payloads.
- Schema is an unwritten contract — breakage caught at runtime.
- Parsing is slow (tokenize characters, allocate strings, build trees).

Protobuf fixes all five:

- Binary, varint-encoded — tiny payloads.
- Strongly typed — generated classes are compile-time checked.
- Field identity = numeric tag, not a string — zero-cost identity.
- Schema is an explicit, versioned `.proto` file.
- Parse is a tight loop of byte reads.

## How it's used

1. Write a `.proto` schema describing message types.
2. Run `protoc` (or language-specific plugin) to generate source code —
   classes with getters/setters, builders, `toByteArray()`, `parseFrom(bytes)`.
3. Use the generated classes in app code. Serialize with `.toByteArray()`,
   ship bytes over the wire, deserialize on the other side with `.parseFrom()`.

Example schema:

```protobuf
syntax = "proto3";

message User {
  int32  id = 1;
  string name = 2;
  string email = 3;
  bool   active = 4;
}
```

The numbers (`1`, `2`, `3`, `4`) are **field tags** — permanent, part of the
wire format, the source of schema-evolution safety.

## When to use it

- **gRPC APIs** — gRPC is built on protobuf; the `.proto` doubles as the
  service contract.
- **High-throughput internal telemetry** — OTLP, Prometheus remote_write,
  metric exporters. Volume is high, humans don't read payloads.
- **Polyglot internal services** — one schema, generated Java + Go + Rust +
  Python classes all interoperate.
- **Message queues with schema registry** — Kafka + Confluent Schema Registry
  use protobuf or Avro.

Don't use protobuf for:

- **Public REST APIs** — consumers expect JSON; debugging with curl/DevTools is
  critical. Some shops wrap protobuf with gRPC-Web or generate OpenAPI from
  the `.proto`; still friction.
- **Config files** — human-edited. Use YAML/TOML/HCL.
- **Browser ↔ server** unless you're using gRPC-Web; otherwise plain JSON wins.

## Alternatives

| Format | Strength | When |
|---|---|---|
| **JSON** | Human-readable, ubiquitous, native in browsers | Public APIs, config, ad-hoc |
| **Avro** | Similar to protobuf, stronger schema evolution, schema embedded in data | Data lakes, Kafka with Confluent |
| **MessagePack** | Binary JSON substitute, no schema | Lightweight RPC, caching |
| **Thrift** | Protobuf's older cousin (Facebook) | Legacy Thrift ecosystems |
| **Cap'n Proto / FlatBuffers** | Zero-copy reads (no parse step) | Real-time games, hot-path latency |
| **CBOR** | Schema-less binary JSON | IoT, constrained devices |

## Tradeoffs

- **+** 2–5× smaller than JSON, 5–10× faster to encode/decode.
- **+** Schema = machine-checked contract. Breaking changes caught in CI
  (`buf breaking`).
- **+** Forward and backward compatibility by design (unknown fields ignored,
  missing fields default).
- **+** Strongly typed across languages.
- **−** Needs a codegen step. CI integration, IDE support, build tooling.
- **−** Not human-readable on the wire; debugging needs tooling (`grpcurl`,
  `protoc --decode`).
- **−** Requires discipline: never reuse a tag number after removing a field
  (`reserved` keyword).
- **−** Smaller ecosystem of generic tools than JSON.

## How it works — general

A `.proto` file defines message types and field tags. A compiler generates
language-specific classes. At runtime, those classes encode field values
as a sequence of `(tag, wire_type, value)` tuples in a compact binary format.
Decoders read tuples, populate fields by tag number, ignore unknown tags.

## How it works — detailed

### Wire format

Each field is encoded as:

```
tag_and_wire_type_varint | value
```

- **`tag`**: the field number from the `.proto` (e.g. `3` for `email`).
- **`wire_type`** (3 bits): indicates encoding family:
  - `0` varint (int32, int64, bool, enum)
  - `1` 64-bit fixed
  - `2` length-delimited (string, bytes, embedded message, repeated)
  - `5` 32-bit fixed
- The combined byte is `(tag << 3) | wire_type`, itself varint-encoded.

Example for `id = 42`:

- Field tag 1, wire type 0 (varint) → first byte `0x08` (= `(1<<3)|0`).
- Value 42 → varint `0x2A`.
- Result: `08 2A` (2 bytes).

### Varints

Variable-length integer encoding. Small numbers take 1 byte, large numbers
up to 10 bytes (64-bit). Most real-world IDs are small → varint dominates
the size savings.

### Length-delimited fields

Strings and embedded messages start with a varint length, followed by that
many bytes. Example for `name = "Alice"`:

- Tag 2, wire type 2 → `0x12`.
- Length 5 → `0x05`.
- Bytes `"Alice"` → `0x41 0x6C 0x69 0x63 0x65`.
- Result: `12 05 41 6C 69 63 65` (7 bytes).

### Schema evolution rules

Protobuf's cardinal rule: **never reuse a tag number**. To remove a field:

```protobuf
message User {
  reserved 3;                 // tag is forever dead
  reserved "email";
  int32 id = 1;
  string name = 2;
}
```

Rules of forward/backward compat:

- **Adding** a new field with a new tag → safe. Old clients ignore it; old
  messages deserialize with default values for the new field.
- **Removing** a field → safe if you `reserve` the tag. New clients won't
  emit it; old clients (which still expect it) get the default.
- **Renaming** a field (keeping the tag) → safe on the wire. Only source code
  changes. Old code compiled against the old name still reads the same bytes.
- **Changing a field's type** → generally unsafe. Specific int widenings
  (`int32 → int64`) work; others break.

### proto3 vs proto2

Most new schemas use **proto3**. Key differences:

- proto3 drops `required` (learned lesson: "required" makes schema evolution
  impossible).
- proto3 has implicit defaults (no explicit "not set" distinction for
  scalars unless you use `optional` keyword, added in proto3.15).

### Code generation

`protoc` + language plugin emits:

- A class per message with builder pattern.
- `toByteArray()` / `parseFrom(byte[])`.
- Field accessors (getters).
- `equals`, `hashCode`, `toString`.
- JSON serialization hooks (some languages).

Build systems: Gradle/Maven protoc plugins, `buf` (modern linter + registry),
`rules_proto` for Bazel.

## How it works — under the hood

### Parsing

1. Read a varint → split into `tag` and `wire_type`.
2. Based on wire_type, read the value (varint / fixed 8 bytes / length + bytes
   / fixed 4 bytes).
3. Look up the tag in the generated code's dispatch table → set the
   corresponding field on the message object.
4. If tag unknown → either drop, or collect into an `unknown_fields` buffer
   (proto3 does the latter, preserving unknowns across round trips — crucial
   for forward compat).
5. Repeat until end of buffer.

Parsing is a tight byte-level loop. No string tokenization, no tree
construction. On modern hardware: hundreds of MB/s per core.

### Where you already use it (without writing any)

- **OTLP**: every trace batch from Micronaut to Alloy is
  `ExportTraceServiceRequest` protobuf bytes over gRPC.
- **gRPC itself**: HTTP/2 framing + protobuf payloads. Every gRPC request is
  a protobuf message.
- **Prometheus remote_write**: Alloy → Mimir is snappy-compressed protobuf
  `WriteRequest`. One schema for the whole ecosystem.
- **Faro remote write**: similar.

### Debugging on the wire

- `grpcurl -plaintext <host>:<port> list` — reflection-based gRPC exploration,
  decodes protobuf to JSON for humans.
- `protoc --decode=Package.Message schema.proto < binary.dat` — manual decode
  when you have the schema.
- Wireshark has dissectors for gRPC/protobuf.

### Compression

Protobuf payloads are often further compressed by transport layers:

- gRPC has per-RPC gzip compression.
- Prometheus remote_write uses snappy (fast, modest ratio).
- OTLP over HTTP often uses gzip.

Compression is orthogonal to protobuf — you'd compress JSON too. Protobuf's
density is pre-compression.
