# Session 8 — Signals, queries, updates (2 h)

| Block | Time | What |
|---|---|---|
| Read | 30 min | Sections 1–3 |
| Lab 6 | 35 min | `Lab06SignalQueryUpdateTest` (7 tests) |
| Read | 15 min | Sections 4–5 |
| Do | 30 min | Exercises |
| Check | 10 min | Checkpoint questions |

**Goal.** For any interaction with a running workflow, pick signal, query or update — and justify it
by what is recorded, what the caller gets back, and what can go wrong.

## 1. The problem: a workflow in the middle of its life

A dispute workflow runs for up to 30 days. During that time the outside world wants to:

- **tell** it something: "the merchant uploaded evidence" (and not wait for an answer);
- **ask** it something: "what stage is dispute d-17 in?" (without changing anything);
- **ask it to do something and get an answer**: "reviewer Alice approves — did it work, and what is
  the new state?"

Each is a different contract. Temporal gives three message types. The lab workflow
(`ReviewWorkflow` in `Lab06`) has one of each:

```java
@WorkflowInterface
public interface ReviewWorkflow {
    @WorkflowMethod  String run(String caseId);

    @SignalMethod    void addEvidence(String note);
    @QueryMethod     String stage();
    @QueryMethod     int evidenceCount();
    @UpdateMethod    String approve(String reviewer);
    @UpdateValidatorMethod(updateName = "approve") void validateApprove(String reviewer);
}
```

## 2. The three, side by side

| | **Signal** | **Query** | **Update** |
|---|---|---|---|
| Direction | Client -> workflow | Client <- workflow | Client <-> workflow |
| Returns a value? | No (`void`) | Yes | Yes (or an error) |
| May change workflow state? | Yes | **No** — read-only | Yes |
| Recorded in history? | **Yes** (`WorkflowExecutionSignaled`) | **No** | **Yes** (`...UpdateAccepted`, `...UpdateCompleted`) |
| Caller waits for processing? | No — returns once the server accepted it | Yes — gets the answer | Yes — until completed (or rejected) |
| Can be rejected up front? | No | n/a | **Yes**, by a validator |
| May run activities / sleep inside the handler? | Yes, in workflow code semantics | **No** (must be fast and pure) | Yes |
| Works on a finished workflow? | No | **Yes** (reads the final state) | No |
| Typical use | "Evidence arrived", "Cancel" | "What stage?", dashboard | "Approve and tell me the result" |

Memorise the three questions: *Does the sender need an answer? Must the message change state? Must it
be durable?* (signal: no/yes/yes; query: yes/no/no; update: yes/yes/yes.)

## 3. What the lab proved

Run it:

```bash
mvn -q test -Dtest=Lab06SignalQueryUpdateTest
```

### Signals

```java
workflow.addEvidence("tracking-123");
workflow.addEvidence("delivery-photo");
assertThat(workflow.evidenceCount()).isEqualTo(2);
// history contains exactly two EVENT_TYPE_WORKFLOW_EXECUTION_SIGNALED
```

A signal is **an event in the history**. On replay, the SDK feeds the signal to the handler at the
same position, so state built from signals is reproduced. Ordering: signals are delivered in the
order they were accepted by the server.

Delivery semantics: if the workflow does not exist, the signal call fails. If it is closed, the call
fails. There is **no response** — if the client crashes after sending, it does not know whether the
server got it. For must-not-lose inputs, retry the signal call (design the handler to tolerate
duplicates, e.g. keyed by an evidence ID) — idempotency again.

### Queries

```java
int eventsBefore = historyTypes().size();
assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW");
assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW");
assertThat(historyTypes()).hasSize(eventsBefore);     // two queries, zero new events
```

A query is answered by **running the workflow code (from cache or by replay) and calling the
handler**. It writes nothing. Consequences:

- It must be **read-only** and fast. Mutating state in a query is a bug the engine will not stop
  (it would be lost anyway on the next replay).
- It needs a worker running right now to answer (a query on a workflow with no available worker
  times out).
- It reads *current in-memory state*, i.e. the state after the last completed workflow task.
- **It fails before the workflow's first task has completed.** The lab's `setUp` had to poll
  `stage()` until it answered `WAITING_FOR_REVIEW`; a query sent in the first milliseconds, before
  any worker had run the workflow once, errors. Real clients should treat "workflow just started"
  as a transient condition.
- **It works after completion**: `queryStillWorksAfterTheWorkflowHasFinished` reads `APPROVED` from a
  closed workflow (as long as the history is retained). Handy for "show me the final state".

### Updates

```java
String answer = workflow.approve("alice");
// "approved by alice with 1 evidence item(s)"
```

Recorded as `WORKFLOW_EXECUTION_UPDATE_ACCEPTED` and `WORKFLOW_EXECUTION_UPDATE_COMPLETED` in our
run. The caller blocks until the handler returns, and gets the **return value** — or an exception.

**Validators.** `@UpdateValidatorMethod` runs *before* the update is accepted. It receives the same
arguments, reads state, and may throw to reject. Rejected updates are **not written to the history**:

```java
assertThatThrownBy(() -> workflow.approve(" ")).isInstanceOf(WorkflowUpdateException.class);
assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW");   // state untouched
// no UPDATE_ACCEPTED / UPDATE_COMPLETED event exists
```

Detail we observed: a rejected update still causes a **workflow task** to run (the validator is
workflow code), so you may see an extra empty `WorkflowTask{Scheduled,Started,Completed}` triplet.
But there is no `UpdateAccepted` and no state change. Validation errors therefore do not bloat the
history with bad input — a real advantage over doing validation *inside* a signal handler, where the
signal is recorded whether or not you act on it.

**After the end.** `updateAfterTheWorkflowFinishedIsRefused` : once the workflow has completed,
`approve("bob")` throws. For "only one approval ever", either the validator checks state while the
workflow is still open (`approvedBy != null`) or you let the finished workflow reject the late update.
Think about which race you want: two reviewers approving at once is decided by the *order* the
updates reach the workflow.

### The timeout path

`ifNobodyApprovesTheTimerWins`: no signal, no update; `Workflow.await(Duration.ofDays(7), ...)`
returns false and the workflow ends with `EXPIRED`. State, wait, deadline: the same pattern as
session 7.

## 4. Choosing — a decision procedure

```
Does the caller need a result back?
├─ no  ─▶ Must it change workflow state or trigger work?
│         ├─ yes ─▶ SIGNAL            "evidence arrived", "customer cancelled"
│         └─ no  ─▶ (nothing to send; maybe log elsewhere)
└─ yes ─▶ Does it change state?
          ├─ no  ─▶ QUERY             "which stage?", "how many evidence items?"
          └─ yes ─▶ UPDATE            "approve and return the new state", "add evidence and return count"
```

The three scenarios of the final check:

| Scenario | Choice | Why |
|---|---|---|
| Customer adds evidence | **Signal** | Fire-and-forget input; must be durable and replayed; no answer needed. (Use an update if the UI must show "accepted, 3 items now".) |
| Show dispute status | **Query** | Read-only; no history cost; works even after the workflow closed. Caveat: needs a worker; for dashboards at scale, project state into a read model (search attributes, a DB). |
| Approve and get the new state | **Update** | State change *and* synchronous answer, with validation. A signal would not return the result; a signal + query would be racy. |

### Why not "signal then query"?

```java
workflow.approve();          // signal
String stage = workflow.stage();   // query immediately
```

The signal returns when the server *accepted* it, not when the workflow *processed* it. The query
may run before the workflow handled the signal and return the old state. The update closes that gap:
it returns after handling.

### Signal-with-start

A client that wants "start the workflow if it does not exist, otherwise signal it" uses
**signal-with-start** — atomic, race-free. Common for "add evidence to dispute d-17; create the
workflow if this is the first message". Mention it in your notes; module 3.1 uses it for event
triggers.

> **Other engines.** BPMN: signals/messages correlate to a waiting catch event by a correlation key
> ("message events"); queries are mostly API reads against the engine DB (Operate/Tasklist);
> there is no direct update equivalent — a user task completion that returns data comes closest.
> Step Functions: `SendTaskSuccess` with a task token for callbacks. The idea of "message the
> running instance" exists everywhere; request–response semantics is the part that differs.

## 5. Handler pitfalls

1. **Handlers run on the workflow thread** — they obey determinism. No I/O inside a signal handler;
   set a field and let the main flow act on it.
2. **Signals before the main method gets to its `await`.** A signal can arrive before `run` reaches
   the code that reads the field. That is fine if handlers only set state; it is wrong if
   `run` initialises the state *after* the signal arrives (overwriting it). Initialise fields at
   declaration, not at the start of `run`.
3. **Unbounded signal growth.** Each signal adds an event. A workflow that receives thousands of
   signals needs continue-as-new (session 9).
4. **Mutable state escaping from queries.** Return copies/immutable values; do not return your internal
   mutable list.
5. **Using queries as a database.** They run workflow code each time; expensive at scale, and
   unavailable without workers. Publish state outward for heavy read use.

## Exercises

1. **Extend the lab.** Add a signal `reject(String reason)` and an update `reject` variant that
   returns the final stage. Write tests proving: (a) the signal is recorded, (b) a second
   `approve` after `reject` is rejected by the validator, (c) `stage()` shows `REJECTED`.
2. **Race it.** Write a test where a signal and the 7-day timer compete: use `env.sleep` so the
   signal arrives exactly at the deadline minus 1 s, then plus 1 s. Which wins in each case?
3. **Idempotent signal.** Make `addEvidence(String evidenceId, String note)` ignore duplicate IDs
   (state: a `Map`). Send the same evidence twice and assert `evidenceCount() == 1`. Why does the
   map need a deterministic iteration order if you later loop over it to issue activities?
4. **Decide.** Choose signal, query or update and justify in one line each:
   - Operator pauses a refund workflow for investigation.
   - A UI shows the progress bar for a payout batch.
   - A customer-service agent changes the refund amount and needs the recalculated fee back.
   - The PSP webhook says "refund settled".
   - A compliance job checks whether the dispute is still open.
5. **Failure modes.** A client sends a signal and the HTTP request times out. What do you do? What
   if the signal handler is not idempotent? What if the workflow has already completed?

## Checkpoint questions

1. Fill the table from memory: returns a value / changes state / recorded in history, for the three.
2. Why does a rejected update not bloat the history, and what does still appear?
3. Why is "signal then query" racy?
4. Which of the three works on a finished workflow?
5. What must a signal handler not do?

<details>
<summary>Answers</summary>

1. Signal: no / yes / yes. Query: yes / no / no. Update: yes / yes / yes (accepted + completed).
2. The validator rejects before acceptance, so there is no `UpdateAccepted`/`UpdateCompleted`;
   an empty workflow-task triplet may still appear because the validator runs as workflow code.
3. The signal returns when accepted, not processed; the query may read old state.
4. Query only.
5. No I/O or non-determinism; it only changes state, the main flow acts. It should be fast and
   tolerate duplicates.
</details>
