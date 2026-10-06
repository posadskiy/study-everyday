# Handoff — orchestration track implementation

Use this file to **continue in a new Cursor chat** opened on the `study-everyday` repo.

## What this is

Implementation of the **320 h learning plan** from the `personal-career` repo:

- **Track:** engine-neutral orchestration engineer, **payments** as domain.
- **Pitch:** payment processes that cannot lose money or skip approval — including bounded AI agent steps (later phases).
- **Full plan with topic explanations:** [README.md](README.md) in this folder. The original plan
  is `docs/career/job/learning-plan.md` in `posadskiy/personal-career`.

## Decisions already made (career chat, Oct 2026)

| Topic | Decision |
|---|---|
| Employer | Not Camunda-specific — skills apply to banks, PSPs, Temporal/Camunda/Conductor customers |
| Engine for Phase 1 | **Temporal Java SDK** (open source, local dev server) |
| Portfolio repo name | `payment-disputes-orchestration` → Maven module `architecture/orchestration/payment-disputes` |
| Java stack | study-everyday defaults: **Java 25**, Spring Boot **4.0.1** |
| Time budget | 8 h/week, Oct 2026 → Jul 2027; apply from May 2027 |

## Where code lives

```
architecture/orchestration/
  README.md           ← learning guide: every topic explained, final checks
  HANDOFF.md          ← this file
  START_HERE.md       ← current module + commands
  learning-log.md     ← one line per study session
  payment-disputes/   ← Spring Boot + Temporal (Phase 1.1 started)
```

## Current status

| Module | State | Next |
|---|---|---|
| **1.1 Durable execution model** | **Started** — `RefundWorkflow` + activities + one test | Add signal/query exercise, event-history notes, crash-recovery demo |
| 1.2 Failure semantics | Not started | Fault-injecting PSP mock, retry tuning |
| 1.3 Sagas / ledger | Not started | Compensation path, ledger invariant tests |
| 1.4 Determinism / versioning | Not started | `getVersion` exercise, replay tests |
| 1.5 Workflow testing | Partial | Expand failure-path tests |

## Prompt for a new agent chat

Copy into the first message:

```
Continue the orchestration career track in study-everyday.
Read architecture/orchestration/HANDOFF.md, START_HERE.md and README.md.
Follow README.md module by module (current: 1.1 final check).
Do not re-plan the track — implement and teach module by module.
```

## Constraints (unchanged)

- JVM career track; remote from Ljubljana; €85k floor; permit until mid-2027 (see personal-career).
- Production credit: Pronet agent platform as durable workflow + approval (at work, not in 320 h).

## History transferred from personal-career chat

1. Chose **Track A** — orchestration, payments domain (not agentic-checkout-as-title).
2. Rewrote `positions.md` engine-neutral; pay and employer types global.
3. Wrote detailed `learning-plan.md` (four advisers: market, evidence, AI teacher, JVM tutor).
4. User asked to **implement in study-everyday** and continue in a **new chat** there.
