# Session 3 — Event history: the source of truth (2 h)

| Block | Time | What |
|---|---|---|
| Read | 30 min | Sections 1–3 |
| Lab 2a | 25 min | `historyIsAnOrderedRecordOfEverythingThatHappened` + the real server |
| Read | 20 min | Sections 4–5 |
| Do | 35 min | Draw the history by hand (exercise 1) |
| Check | 10 min | Checkpoint questions |

**Goal.** Read a history like a log file of the *engine's* decisions: for any line, say who wrote
it, why, and what it tells a future replay.

## 1. What the history is

For every workflow execution, the server keeps an **append-only list of numbered events**. It is
not a debug log you may lose. It is the *state* of the workflow. Your workflow object in memory is a
cache that can be rebuilt from this list at any time; if memory and history disagree, history wins.

Three properties matter:

- **Append-only.** Events are never edited or removed while the execution is open. The past is fixed.
- **Ordered.** Event IDs 1, 2, 3, ... define a total order. Replay depends on it.
- **Complete for decisions.** Everything that influenced the workflow's *decisions* is in it: results
  of activities, timer firings, signals received, child workflow results, random/time values taken
  via `Workflow.*`. Everything else (heap state) can be derived.

## 2. A real history, line by line

This is the actual output of `temporal workflow show` for a refund (from the crash lab in session 5,
here simplified by removing the crash; the shape is identical — a normal 5-step run has 35 events):

```
 ID  Type
  1  WorkflowExecutionStarted      <- input recorded here: the RefundRequest
  2  WorkflowTaskScheduled         ┐
  3  WorkflowTaskStarted           │  "run the workflow code up to its next wait"
  4  WorkflowTaskCompleted         ┘  <- the worker's answer: "I want activity Validate"
  5  ActivityTaskScheduled         <- command became an event: Validate, with its input
  6  ActivityTaskStarted           <- a worker picked it up
  7  ActivityTaskCompleted         <- RESULT recorded here
  8  WorkflowTaskScheduled         ┐  the workflow is woken up because its wait is over
  9  WorkflowTaskStarted           │
 10  WorkflowTaskCompleted         ┘  <- answer: "now I want ReserveFunds"
 11  ActivityTaskScheduled         (ReserveFunds)
 12  ActivityTaskStarted
 13  ActivityTaskCompleted
 14-16 workflow task                 -> "SubmitToPsp"
 17  ActivityTaskScheduled         (SubmitToPsp)
 18  ActivityTaskStarted
 19  ActivityTaskCompleted         <- the PSP reference lives here
 20-22 workflow task                 -> "PostLedger"
 23-25 activity                     (PostLedger)
 26-28 workflow task                 -> "NotifyCustomer"
 29-31 activity                     (NotifyCustomer)
 32-34 workflow task                 -> workflow code reaches `return`
 35  WorkflowExecutionCompleted    <- the RefundResult recorded
```

(That is, 1 started event + a first task triplet + 5 times [activity triplet + task triplet] + the
completion: 1 + 3 + 5x6 + 1 = 35. You can verify this in Lab 2a for the three-step variant: 23 events.)

### The workflow-task triplet

`WorkflowTaskScheduled` -> `Started` -> `Completed`. This is the engine's way of saying: "your
workflow code was given the chance to run, once". Whenever something *happens that the workflow
might care about* — it starts, an activity finishes, a timer fires, a signal arrives — the server
schedules a **workflow task**. A worker runs the workflow code until it blocks again (waiting on an
activity, timer, signal...) and replies with a list of **commands** ("schedule activity X", "start
timer", "complete workflow").

So the loop is:

```
event happens  ->  workflow task  ->  worker runs code to next wait  ->  commands
       ^                                                                    |
       └────────────  server turns commands into new events  ───────────────┘
```

**Commands** are what the workflow *asks* for; **events** are what is *recorded*. A command such as
"schedule activity PostLedger" becomes the event `ActivityTaskScheduled`. This distinction is the
key to the non-determinism error in session 4: replay compares the commands your *current code*
generates against the *events* already in the history.

## 3. What is inside an event

Events are not just labels. They carry attributes. Get the real thing:

```bash
temporal workflow show --workflow-id refund-s2-1 --output json | less
```

Find these three and read their attributes:

- `WorkflowExecutionStarted` — `workflowType`, `taskQueue`, **`input`** (the serialised
  `RefundRequest`, encoded as JSON payload), `workflowExecutionTimeout` etc.
- `ActivityTaskScheduled` — **`activityType`** (`SubmitToPsp`: note the capitalised name derived from
  the Java method), `taskQueue`, **`input`**, the timeouts and the **retry policy** you set in
  `ActivityOptions`.
- `ActivityTaskCompleted` — **`result`** (the PSP reference). This is what replay will feed back
  into `activities.submitToPsp(...)`.

Take away: **the history stores both your inputs and your results.** That has three practical
consequences you will meet later:

1. **Size.** Big payloads mean a big history (and slower replay). Pass IDs, not documents.
2. **Privacy.** The history contains your data (card tokens? names?) in readable form, and is visible
   in the UI. A *data converter* can encrypt payloads (module 3.2). Think before putting PII in
   activity arguments.
3. **Serialisation is part of the contract.** If you rename a field in `RefundRequest`, old
   histories still contain the old shape. (Versioning, module 1.4.)

## 4. What is *not* in the history

Experimentation in this repository showed several things that surprise people:

- **Queries leave no trace.** Reading workflow state with a `@QueryMethod` creates no events (Lab 6
  proves it: two queries, zero new events).
- **A failed activity attempt is not an event.** If `SubmitToPsp` fails twice and succeeds on the
  third try, the history shows a single `ActivityTaskScheduled`, `ActivityTaskStarted` (for the last
  attempt) and `ActivityTaskCompleted`. The earlier attempts are tracked by the server as *pending
  activity* state (visible in `temporal workflow describe`, "attempt: 3", with the last failure) but
  are not written to the history until the activity finishes. This keeps histories small, and means
  **you cannot reconstruct retries from the history alone** — you need logs and metrics for that.
  (Module 3.2.)
- **The activity's side effect is not in the history** — only its *return value*. The history says
  "PSP reference was psp-ab3b...", never "money moved".
- **Time taken** is visible through event timestamps, which gives you a free profiler. In the crash
  lab, event 17 `ActivityTaskScheduled` at 13:56:44 and event 18 `ActivityTaskStarted` at 13:57:15
  show a 31-second gap — that is a worker crash, visible without any log (session 5).

## 5. History is also an API

Because the history is complete and ordered, it is useful for more than recovery:

| Use | How |
|---|---|
| **Debugging** | Web UI timeline; `temporal workflow show`. "What happened to refund 17?" has an exact answer. |
| **Audit** | A tamper-evident-ish sequence of what the system decided (retention is configurable). |
| **Replay testing** | Export a history, replay it against new code in CI (session 4). |
| **Metrics** | Event timestamps give per-step latency. |

> **Other engines.** Temporal calls it the *event history* and replays code from it. Zeebe/Camunda
> keeps *process instance state* and an event *log* but moves a token through a diagram instead of
> re-running code. Step Functions stores an execution history of state transitions (similar to
> events). Conductor stores task states. Idea in common: durable record of progress, queryable.
> Difference: whether the engine can *rebuild a code-defined state* from it (Temporal) or just
> continues from a recorded position (token).

## Lab 2a — read the history of a three-step workflow

```bash
mvn -q test -Dtest=Lab02HistoryAndReplayTest#historyIsAnOrderedRecordOfEverythingThatHappened
```

The test prints `EVENT EVENT_TYPE_...` lines (the SDK's full event names, e.g.
`EVENT_TYPE_WORKFLOW_EXECUTION_STARTED`). Check them against your hand-drawn version of the table
in section 2.

Then, on the real server, run `refund-s2-1` again (new ID) and verify that your count is 35 events.

## Exercises

1. **Draw it.** Without looking at section 2, draw the history of the three-step workflow (validate,
   reserve, psp). Mark every workflow-task triplet and every activity triplet. Count the events.
   Compare with the lab (23). Where did you get it wrong?
2. **Where is the crash?** A refund history ends with these events:

   ```
   ... 13 ActivityTaskCompleted      (ReserveFunds)
       14 WorkflowTaskScheduled
       15 WorkflowTaskStarted
       16 WorkflowTaskCompleted
       17 ActivityTaskScheduled      (SubmitToPsp)
       18 ActivityTaskStarted
   ```

   and nothing after. State what is true: did the PSP call finish? Might the PSP have been called?
   Is the workflow dead? (Answer: nothing is recorded for the PSP result; the PSP *may or may not*
   have received the request; the workflow is not dead — it is waiting for the activity, and the
   server will time the attempt out and retry. This single picture is the heart of session 6.)
3. **Predict the events.** Write the event sequence for a workflow that: calls one activity, then
   `Workflow.sleep(Duration.ofMinutes(5))`, then calls another. Which new event types appear? (Check
   in session 7.)
4. **Payload hygiene.** `RefundRequest` currently has `refundId`, `paymentId`, `amountMinor`,
   `currency`. A colleague wants to add the customer's full name and address "so the notification
   activity has it". Argue for or against, using sections 3 and 4. Propose the alternative.
5. **UI scavenger hunt.** In the Web UI find: the workflow's task queue; the duration of the
   `SubmitToPsp` activity; the exact JSON input of the workflow. Then do the same with the CLI
   (`temporal workflow describe`).

## Checkpoint questions

1. Why is the history called the "source of truth" and the worker's memory a cache?
2. What is the difference between a command and an event?
3. Why does a workflow task appear between two activities?
4. Which of these is recorded in the history: the activity's return value / its side effect / a
   failed attempt / a query?
5. Give two reasons to keep activity arguments small.

<details>
<summary>Answers</summary>

1. The memory can be lost at any time and rebuilt from the history; the reverse is impossible.
2. A command is a request produced by workflow code in a workflow task ("schedule activity X"). An
   event is the durable record the server writes. Commands become events; replay compares them.
3. When an activity completes, the workflow must get the chance to run and decide what comes next;
   that is a workflow task.
4. Only the return value (and the input). The side effect itself, failed attempts that were retried,
   and queries are not recorded.
5. History size/replay time and exposure of data in a readable, retained log.
</details>
