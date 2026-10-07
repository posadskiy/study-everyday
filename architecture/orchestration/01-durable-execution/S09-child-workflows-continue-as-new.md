# Session 9 — Child workflows and continue-as-new (2 h)

| Block | Time | What |
|---|---|---|
| Read | 35 min | Sections 1–3 |
| Lab 7 | 25 min | `Lab07ChildAndContinueAsNewTest` |
| Read | 20 min | Sections 4–5 |
| Do | 30 min | Exercises |
| Check | 10 min | Checkpoint questions |

**Goal.** Decide when work deserves its own workflow, when a history must be cut with
continue-as-new, and what each choice does to failure handling, visibility and size.

## 1. Why one workflow is sometimes not enough

Two pressures push you away from "one big workflow":

1. **Structure.** A dispute has a main flow and, inside it, N independent sub-processes: collect
   each evidence item (fetch from three systems, validate, upload). They have their own lifecycle,
   retries and waiting. Putting everything in one execution mixes concerns and makes one history
   very long.
2. **Size.** A history that only grows will, in the end, hit a limit. Temporal documents a
   warning at about **10 240 events / 10 MB** and a hard limit at about **51 200 events / 50 MB**
   per execution (check the current docs; I did not hit these in the labs). Long waits, long loops
   and many signals all grow the history. Replay cost grows with it (session 4).

Two tools answer these: **child workflows** for (1), **continue-as-new** for (2).

## 2. Child workflows

A child workflow is a **separate workflow execution** started by a parent. It has its *own* ID, its
*own* history, and its own failure/retry/timeout settings.

```java
List<Promise<String>> pending = new ArrayList<>();
for (int i = 0; i < count; i++) {
    EvidenceItemWorkflow child = Workflow.newChildWorkflowStub(
            EvidenceItemWorkflow.class,
            ChildWorkflowOptions.newBuilder().setWorkflowId("evidence-item-" + i).build());
    pending.add(Async.function(child::collect, i));     // starts in parallel
}
Promise.allOf(pending).get();                           // wait for all
```

Details worth reading twice:

- `Async.function(...)` returns a `Promise` — Temporal's deterministic future. All `count` children
  run **concurrently**. This is fan-out/fan-in. (Do not use `CompletableFuture`; session 4.)
- The parent's history records `ChildWorkflowExecutionStarted` per child (3 in the lab) and the
  results; the **child's work lives in the child's history**. The lab fetches
  `evidence-item-1` separately and finds a complete `Started ... Completed` history of its own.
- Parent and child are replayed independently. A bug in one child does not corrupt the parent's
  history; the parent only sees the result or a `ChildWorkflowFailure`.

### Child workflow vs activity vs plain code

| Use | When |
|---|---|
| **Activity** | One side-effecting action; short; no internal workflow logic. |
| **Child workflow** | A process with its own steps, waits, signals, retries; independent lifecycle; its own history; reuse across parents; or you need to bound the parent's history size. |
| **Inline workflow code** | Pure decisions; small sub-flows that never wait independently. |

Heuristic: if you would draw it as its own box in the diagram and operators would want to look it up
by an ID, it is a child.

### Things a child gives you (and costs)

- **Own ID -> idempotent start.** A deterministic ID like `evidence-item-7` stops a second child from
  being started for the same item: the server rejects a duplicate ID of a running workflow (policy
  configurable). Use IDs derived from the business object, never random ones — a retry of the
  parent's logic after a crash must produce the *same* ID.
- **Own visibility.** Search for it in the UI by its ID.
- **Parent close policy.** What happens to a running child when the parent ends or is cancelled:
  `TERMINATE` (default), `REQUEST_CANCEL` or `ABANDON`. For money flows, choose consciously: a child
  that is mid-refund should usually be allowed to finish (`ABANDON`) or be cancelled gracefully
  (`REQUEST_CANCEL`) — not terminated.
- **Failure propagation.** A failing child surfaces in the parent as an exception you can catch and
  handle, like an activity failure.
- **Cost.** More executions to store and operate; extra round trips. Do not create a child per
  trivial step.
- **Result size.** The child's result goes into the parent's history. Return IDs, not documents.

### A different way to split: independent top-level workflows

If the sub-process outlives the parent, or is triggered by someone else, start it as a **separate
workflow with its own lifecycle** (via an activity that calls the client, or a message) and
correlate by ID. The decision child vs independent: *should the parent's failure or cancellation
affect it?*

## 3. Continue-as-new

`Workflow.continueAsNew(args)` **ends the current run** and starts a **new run** of the same workflow
type with the **same workflow ID** and a **fresh, empty history**, carrying whatever state you pass as
input. Lab 7's `BatchWorkflow` processes eight payouts three at a time:

```java
int end = Math.min(from + BATCH, total);
for (int i = from; i < end; i++) {
    activities.step("payout-" + i);
}
if (end < total) {
    Workflow.continueAsNew(end, total);      // finish this run, start the next from index `end`
}
return "all " + total + " payouts done";
```

Run 1: payouts 0–2, then continue-as-new(3, 8). Run 2: 3–5, continue-as-new(6, 8). Run 3: 6–7, done.
The test shows:

- the client's `getResult` **follows the chain** and returns the last run's result;
- all eight activities ran exactly once, in order;
- the first run's history is **cut off** with the event `WORKFLOW_EXECUTION_CONTINUED_AS_NEW` (23
  events in that run);
- the workflow ID stays the same, but each run has a different **run ID**.

### What carries over, what does not

- **Carries over:** only what you pass as arguments (and the workflow ID; by default also the task
  queue and options). **State in fields is lost.** You must put everything the next run needs into
  the input: counters, cursors, the list of unprocessed items.
- **Does not carry over:** event history, pending signals that are not yet handled (drain them first
  — signals that arrive *during* continue-as-new may be carried to the new run per SDK/server
  behaviour, but design to handle them before you call it), timers (restart in the new run).
- **Pending activities and children** must be finished or resolved before continuing, or you risk
  abandoning them.

### When to call it

- A loop with a known **batch size** (lab).
- A long-lived "entity" workflow (the merchant's lifetime) after N iterations.
- When `Workflow.getInfo().isContinueAsNewSuggested()` turns true — the server tells you the history
  is getting big (check in your SDK version).
- Before any very long wait if the history is already large.

> **Rule of thumb:** a workflow that can run for months or process unbounded items needs a
> continue-as-new plan from day one. Retrofitting it is possible but needs versioning (module 1.4).

### Design: what goes into the new input

A good pattern is a **state object** carried between runs:

```java
record BatchState(int nextIndex, int total, int failedSoFar) {}
...
Workflow.continueAsNew(new BatchState(end, total, failed));
```

Keep it small — it goes into `WorkflowExecutionStarted` of the next run. Large lists belong in a
database; carry a cursor.

## 4. The history budget — a worked estimate

Rule: a normal activity step costs **6 events** (3 activity + 3 workflow task), a timer about **5**
(2 timer + 3 workflow task), a signal about **4** (signal + 3 task), a child workflow about **8**.

Dispute workflow: 12 activities, 3 timers, 20 signals over 30 days:
12x6 + 3x5 + 20x4 + a few = about 170 events. Comfortable; no continue-as-new needed.

Merchant monthly-statement workflow running for 5 years: 60 iterations x 11 events = about 660. Fine.

Per-minute heartbeat workflow for one year: 525 600 x 11 = ~5.8 M events: impossible.

Batch of 100 000 payouts in one workflow: 100 000 x 6 = 600 000 events: impossible. Use
continue-as-new every 500–1 000 items, or a parent that fans out to children that each handle a batch.

## 5. Traps

1. **Random child IDs.** A replay or retry creates a duplicate child, double work.
2. **Unbounded fan-out.** `for (i < 50_000) Async.function(child...)` in one parent makes a huge
   history. Process in waves and/or continue-as-new.
3. **Forgetting to carry state** across continue-as-new — fields reset to default and the new run
   "starts over".
4. **Continuing as new with a pending update/signal handler mid-flight.** Wait until handlers have
   finished (`Workflow.await(Workflow::isEveryHandlerFinished)`, check SDK availability) before you
   call it.
5. **Assuming the client's workflow stub follows runs forever.** `getResult` follows the chain; a
   *describe* or a *signal* targets the latest run if you omit the run ID, but a stub bound to a
   specific run ID points at a closed run.
6. **Parent close policy left at default for a money child.** See section 2.
7. **Mixing the two ideas.** A child does not shrink the *parent's* history by itself; the parent
   only records results. Continue-as-new shrinks *one execution's* history by restarting it. Use
   the one that matches the pressure.

> **Other engines.** BPMN has *call activities* and *sub-processes* (embedded vs reusable) —
> equivalents of child workflows; multi-instance activities for fan-out. Process instance size limits
> exist but are looser. Step Functions has the *Map* state (fan-out) with a hard 25 000-event
> execution history limit and a pattern of "start a new execution" for long runs — the same
> continue-as-new idea under a different name. Conductor has *sub-workflows* and *dynamic fork*.

## Lab 7

```bash
mvn -q test -Dtest=Lab07ChildAndContinueAsNewTest
```

Read both tests and the output line `FIRST RUN: 23 events, last = EVENT_TYPE_WORKFLOW_EXECUTION_CONTINUED_AS_NEW`.
Work out where 23 comes from (1 started + 3 task events + 3 activities x 6 + 1 continued-as-new =
23). Predict the numbers for `BATCH = 5`.

## Exercises

1. **Change the batch.** Set `BATCH = 5` and total 12. Predict the number of runs and the
   events of each. Verify with `client.fetchHistory("wf-batch", runId)`; to list runs, use
   `temporal workflow list --query 'WorkflowId="wf-batch"'` on the real server.
2. **Children with failures.** Make `EvidenceItemWorkflow.collect(1)` throw. What does the parent see?
   Catch `ChildWorkflowFailure` and return "partial: 2 of 3". Decide: should one failed item fail the
   dispute?
3. **Duplicate child IDs.** Start the same parent twice with the same workflow ID prefix while the
   first is still running. What does the server say? Which `WorkflowIdReusePolicy` /
   conflict policy would change it?
4. **Budget exercise.** Estimate the events for: (a) the refund workflow of this repository,
   (b) a dispute with 4 evidence uploads, 2 reminders and a 30-day wait, (c) a "payout batch" of
   2 000 items processed one activity each. For (c) propose a continue-as-new schedule.
5. **Child or not?** Decide for each and justify: "validate the refund"; "collect evidence item N";
   "post three ledger rows"; "notify the customer by e-mail and SMS and push independently";
   "the monthly cycle of a merchant".
6. **State object.** Rewrite `BatchWorkflow` to carry a `BatchState(nextIndex, total,
   failedSoFar)` record, count failures (catch `ActivityFailure` and keep going), and assert the final
   report across three runs.

## Checkpoint questions

1. What does a child workflow have that an activity does not?
2. Why must child workflow IDs be deterministic?
3. What survives continue-as-new, and what does not?
4. Name two events that tell you a workflow needs continue-as-new.
5. Why does a long history hurt even below the hard limit?

<details>
<summary>Answers</summary>

1. Its own ID, history, retries/timeouts and workflow logic (signals, timers, children); it is a
   workflow, not one action.
2. The parent code is replayed or retried; random IDs would create duplicates, deterministic IDs
   make the start idempotent.
3. Workflow ID and the arguments you pass survive. Event history, in-memory state and timers do
   not.
4. A loop or batch with unbounded items/time; `isContinueAsNewSuggested()` / history size warnings
   (about 10 000 events).
5. Replay cost and worker memory grow with history; recovery after a cache eviction gets slower.
</details>
