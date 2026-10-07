# Start here — Phase 1.1

## Module

**1.1 The durable execution model** (20 h) — follow the course in
[01-durable-execution/](01-durable-execution/README.md): ten two-hour sessions, each with lab,
exercises and checkpoint questions. Start with
[Session 1](01-durable-execution/S01-the-problem.md).

## Run locally

```bash
# Terminal 1 — Temporal dev server (needs temporal CLI: brew install temporal)
temporal server start-dev

# Terminal 2 — worker
cd architecture/orchestration/payment-disputes
mvn -q spring-boot:run

# Tests (no server required)
mvn -q test
```

## What is already built

- `RefundWorkflow`: validate → reserve → PSP → ledger → notify.
- `RefundActivitiesImpl` with idempotent PSP mock (`pspByRefundId`).
- `RefundWorkflowTest` using `TestWorkflowEnvironment`.

## Labs

| Lab | Where | Server needed |
|---|---|---|
| 1 naive refund, 2 history/replay, 4 tasks wait, 5 timers, 6 signal/query/update, 7 child/continue-as-new, 8 retries | `payment-disputes/src/test/java/.../lab11/` (`mvn -q test -Dtest=Lab02*`) | No |
| 3 worker crash | `01-durable-execution/scripts/crash-lab.sh` (run from repository root) | Starts its own dev server |

Record progress in `learning-log.md` after every session.

## Final check (module 1.1)

You can explain workflow vs activity, draw replay after a crash, and pick signal vs query vs update for three scenarios — without notes.
