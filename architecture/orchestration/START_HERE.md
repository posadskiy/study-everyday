# Start here — Phase 1.1

## Module

**1.1 The durable execution model** (20 h) — see `personal-career/docs/career/job/learning-plan.md`.

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

## Your next exercises (1.1)

1. Add `docs/event-history-walkthrough.md` — sketch history for crash after `reserveFunds`.
2. Add a workflow **query** `getStage()` and **signal** `cancel` (exercise from learning plan).
3. Record progress in `learning-log.md`.

## Final check (module 1.1)

You can explain workflow vs activity, draw replay after a crash, and pick signal vs query vs update for three scenarios — without notes.
