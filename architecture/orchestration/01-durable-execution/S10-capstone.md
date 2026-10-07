# Session 10 — Capstone: build, explain, defend (2 h)

| Block | Time | What |
|---|---|---|
| Build | 55 min | Add `getStage()` and `cancel` to `RefundWorkflow`, with tests |
| Final check A | 20 min | Draw the crash history and narrate the replay |
| Final check B | 15 min | Which code may run twice / which must be deterministic |
| Final check C | 10 min | Signal / query / update scenarios |
| Review | 15 min | Score yourself, write the log entry |
| Buffer | 5 min | |

**Goal.** Prove the module. No new material: you build with what you know, then answer the three
final-check items under time pressure, *without notes*.

## Part 1 — Build (55 min)

Starting point: [`RefundWorkflow`](../payment-disputes/src/main/java/com/posadskiy/orchestration/payment/refund/RefundWorkflow.java)
runs validate -> reserve -> PSP -> ledger -> notify.

### Requirements

1. **Query `getStage()`** returns one of `STARTED`, `VALIDATED`, `RESERVED`, `PSP_SUBMITTED`,
   `LEDGER_POSTED`, `COMPLETED`, `CANCELLED`. It is read-only and updated by the workflow as each
   step completes.
2. **Signal `cancel(String reason)`**. If the signal arrives **before** the PSP step starts, the
   refund stops cleanly: remaining steps are skipped, stage becomes `CANCELLED`, and the result
   has status `CANCELLED`. If it arrives **after** the PSP step started, the signal is recorded
   but ignored — money may have moved and cancelling here is the saga problem of module 1.3.
   Write down in a comment *why* you chose the cut-off at the PSP call.
3. **Where to check.** The check happens between steps (at defined points), not inside an activity.
   Use a plain field such as `boolean cancelRequested` (workflow code runs on one thread).
4. Do not change activity code. Do not break the existing test.

### Hints (read only if stuck)

- Add to `RefundWorkflow`: `@QueryMethod String getStage();` and `@SignalMethod void cancel(String reason);`
- The check between steps is just `if (cancelRequested) return cancelled();`
- To let a test send the signal *between* steps, start the workflow asynchronously and use an
  activity implementation that blocks on a latch, or use `env.sleep` plus a `Workflow.await` gate
  — design this deliberately; it is part of the exercise. (A cleaner design: add a
  `Workflow.await(Duration.ofSeconds(5), () -> cancelRequested)` "grace window" before the PSP call —
  discuss what this costs: every refund now takes at least 5 s unless cancelled. Is that acceptable? Why
  might a *cooling-off* step in a real product exist?)

### Tests to write (acceptance)

| Test | Proves |
|---|---|
| `stageAdvancesThroughTheSteps` | After completion `getStage()` is `COMPLETED` (query on a finished workflow works — Lab 6). |
| `cancelBeforePspStopsTheRefund` | PSP activity was **never called**; result `CANCELLED`; `getStage()` is `CANCELLED`. |
| `cancelAfterPspIsIgnored` | PSP and ledger ran; result `COMPLETED`; the history still contains the `WorkflowExecutionSignaled` event. |
| `queryLeavesNoTrace` | History length is unchanged by calling `getStage()` twice. |
| `replayStillWorks` | Run the workflow, export the history, replay with `WorkflowReplayer` against the new code: green. |
| `breakingChangeIsDetected` | Swap the order of `reserveFunds` and `validate` in a copy of the class; replaying the old history fails with `NonDeterministicException`. |

Definition of done: `mvn -q test` green; each test has a one-line comment stating what it proves;
`learning-log.md` has your entry.

## Part 2 — Final check A: the crash history (20 min)

On paper, from memory. Scenario: a refund runs; the worker is killed during `submitToPsp`; a new
worker finishes the refund.

1. Draw the **event history** up to the moment of the crash. Number the events. (There are 17.)
2. Mark the event that is the *last thing written before the crash*.
3. Draw what the **new worker** does, line by line, using the format of session 4 section 1: for
   each line of `process()`, say "served from history" or "live".
4. State: which activities ran **once**, which ran **twice**, and which of the two kinds of code
   (workflow / activity) ran again after the crash.
5. State how long the recovery takes and which setting controls it.
6. State what you would put into the PSP call to make the repeat harmless.

Scoring (1 point each, 6 total):
- [ ] History structure is correct: a workflow-task triplet before each activity triplet.
- [ ] The result of `Validate` and `ReserveFunds` appears as completed events.
- [ ] `SubmitToPsp` is scheduled but has no completion.
- [ ] Replay is described as "same code, results from history, no activity re-run for finished ones".
- [ ] Recovery time = start-to-close timeout (about 31 s in the lab), without heartbeats.
- [ ] Idempotency key stable per refund (e.g. `refundId`) given to the PSP.

## Part 3 — Final check B: twice vs same-every-time (15 min)

Answer in two lists, from memory.

1. **May run more than once** (give at least four examples, and the reason each can repeat):
   activities (retries, timeouts, crash recovery); the PSP HTTP call inside the activity;
   *workflow code itself* (every replay); a signal handler (every replay); ...
2. **Must give the same answer each time** (give at least four): workflow control flow; the order
   and types of commands; values derived from `Workflow.currentTimeMillis`/`randomUUID`; the
   iteration order of any collection used to issue commands; ...
3. For each of these five snippets, say "fine" or "breaks determinism" and give the fix:
   - `if (request.amountMinor() > 100_000) activities.review(request);`
   - `String id = UUID.randomUUID().toString();`
   - `new Thread(() -> activities.notifyCustomer(...)).start();`
   - `Workflow.sleep(Duration.ofDays(30));`
   - `for (var e : new HashSet<>(merchantIds)) activities.settle(e);`

   (fine; breaks -> `Workflow.randomUUID()`; breaks -> `Async.function`; fine; breaks -> sorted collection.)

## Part 4 — Final check C: signal, query, update (10 min)

For each, choose and give **two reasons** (what is recorded; whether the caller needs an answer):

| Scenario | Your choice | Reasons |
|---|---|---|
| Customer adds evidence | | |
| Show dispute status in a dashboard | | |
| Reviewer approves; UI must show the new state | | |
| PSP webhook "refund settled" arrives | | |
| Support agent asks "has the customer been notified yet?" | | |

(Signal; query; update; signal (keyed by refund ID, idempotent); query.)

## Part 5 — Self-review (15 min)

Write in `learning-log.md` (one line per session you did, plus this):

```
2026-..-.. | 2 | Module 1.1 capstone: getStage + cancel, replay test | <what surprised me>
```

Then answer, honestly, in your notes:

1. Which session do I understand least? Re-read it and redo its lab tomorrow.
2. What would I say about replay in an interview in 90 seconds? (Write it, then say it aloud.)
3. What did the labs show that I did **not** predict? (There should be at least three. If there are none,
   you probably skipped the "predict first" step — redo two labs properly.)
   Typical surprises: the failed attempt is absent from the history; recovery time equals the
   timeout; swapped arguments pass replay; appending a step at the end breaks old histories; a
   rejected update leaves no update event; a query on a finished workflow works.

### Interview-style questions to answer aloud (60 seconds each)

1. "A worker crashed in the middle of a payment step. What happens?"
2. "Why can't I call an HTTP API from workflow code?"
3. "How do you change a deployed workflow safely?" (You do not know the full answer yet — module 1.4
   — but you can say *why it is a problem* and name replay tests.)
4. "Signal or update — when?"
5. "What is the difference between a child workflow and an activity?"
6. "How would you wait 30 days for a customer reply?"
7. "How do you prevent a double refund?"

## When this module is done

You are ready for [1.2 Failure semantics](../README.md#12-failure-semantics--15-h) when:

- all ten sessions' labs are green and logged;
- Final check A scores at least 5 of 6, B and C at least 80%, **without notes**;
- the capstone tests exist and pass.

If not, repeat the weakest session — do not move on. Everything in module 1.2–1.5 assumes this model.
