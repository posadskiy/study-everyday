# Session 7 — Durable timers and long waits (2 h)

| Block | Time | What |
|---|---|---|
| Read | 30 min | Sections 1–3 |
| Lab 5 | 30 min | `Lab05TimersTest` (3 tests) + a real 60-second timer |
| Read | 20 min | Sections 4–5 |
| Do | 30 min | Exercises |
| Check | 10 min | Checkpoint questions |

**Goal.** Model "wait 24 hours", "wait up to 30 days for evidence, whichever comes first", and
"do this every month" correctly, and explain why none of it holds a thread, a connection or memory.

## 1. Why timers are special

Payments are full of waiting:

- a **cooling-off period** of 24 h before releasing a reservation;
- a **chargeback evidence deadline** of 30 days (card schemes set the dates);
- a **retry of a PSP outage** after 15 minutes;
- a **settlement** that happens on day T+2;
- a **reminder** to the merchant after 3 days of silence.

Done with plain code (`Thread.sleep(30 days)`), the process would hold a thread for 30 days and
die on the first deploy. Done with a cron job + status column (session 1, option A), you write the
engine again. In durable execution a wait is a **first-class command**:

```java
Workflow.sleep(Duration.ofHours(24));
```

What happens:

1. The worker returns the command "start timer 24 h" to the server.
2. The server writes `TimerStarted` and **owns the timer** from then on.
3. The workflow has nothing to do; the worker may drop it from memory. *No thread is held*, nothing
   is polling, a million sleeping workflows cost storage, not CPU.
4. After 24 h the server writes `TimerFired` and schedules a workflow task. Whatever worker is
   available replays the code (if not cached), reaches the line after the `sleep`, and continues.

The timer survives anything the workers do: crashes, deploys, being stopped for a week. If workers
were down when the timer fired, the workflow task simply waits in the queue (session 5).

## 2. What it looks like in the history (Lab 5)

`CoolingOffWorkflow` reserves, sleeps 24 h, then releases:

```java
activities.step("reserve");
Workflow.sleep(Duration.ofHours(24));
activities.step("release");
```

Test `sleepWritesTimerStartedThenTimerFiredBetweenTwoActivities` filters the history to the
interesting events and asserts:

```
ACTIVITY_TASK_SCHEDULED   (reserve)
TIMER_STARTED
TIMER_FIRED
ACTIVITY_TASK_SCHEDULED   (release)
```

Notice how the timer sits in the history *exactly* like an activity: a command, then an event that
says it finished. During replay `Workflow.sleep` returns immediately if `TimerFired` is already
recorded — no real waiting, same as an activity result.

### Time in tests

The in-process test environment uses **time skipping**: when every workflow is just waiting on a
timer, the test clock jumps ahead. The `thirtyDayDeadlinePassesInMillisecondsOfRealTime` test:

```
TEST TIME ELAPSED: 30 days; WALL TIME: 10 ms
```

Thirty days of workflow time in ten milliseconds. This is why even business flows with
multi-week waits can be unit tested fast. (`env.sleep(Duration)` moves the test clock by hand when
you need to act in the middle of a wait, e.g. send a signal on day 10.)

> Time skipping is a property of the **test server**. A real server waits for real time: if you want
> to see `TimerFired` live, use a short timer.

## 3. Wait for an event *or* a deadline

The most important timer pattern is not a plain sleep; it is **a race between an event and a
deadline**:

```java
boolean arrived = Workflow.await(Duration.ofDays(30), () -> evidence);   // block until true or 30 days
if (arrived) {
    activities.step("represent");
    return "REPRESENTED";
}
activities.step("accept-liability");
return "ACCEPTED_AFTER_DEADLINE";

@Override
public void evidenceReceived() { evidence = true; }       // signal handler
```

`Workflow.await(timeout, condition)` blocks until the condition is true **or** the timeout passes
and returns which one happened. The condition is re-evaluated whenever workflow state may have
changed (after each signal/update/activity completion). Two tests in the lab show both outcomes:

- **No evidence:** the 30-day timer fires; the history contains `TimerStarted` and `TimerFired`;
  the workflow accepts liability. (Test 1.)
- **Evidence on day 10:** the test sleeps 10 days, sends the signal, the condition becomes true,
  the workflow represents. History: `TimerStarted`, `WorkflowExecutionSignaled`, ... and **no
  `TimerFired`**. (Test 2.)

> **Observation, honestly:** in our run the history for the second case contains **no
> `TimerCanceled` event** either, although conceptually the timer is cancelled. How and whether
> the cancel is recorded depends on SDK internals and on whether the workflow completes in the same
> task; do not write code or assertions that depend on seeing `TimerCanceled` for `await` timers.

This pattern is the template for a large part of the dispute domain: *wait for the merchant, but
not forever*. Every long wait in a business workflow should have a deadline, with a defined action
at expiry. A workflow without one is a leak.

### Two clocks

- **Workflow time**, `Workflow.currentTimeMillis()`: deterministic, replay-safe. Use for decisions
  ("is it past the deadline?").
- **Wall time**, `System.currentTimeMillis()`: not allowed in workflow code (session 4).

Computing deadlines: store the *deadline instant* (`start + 30 days`) in a field rather than
recomputing from "now", so that the semantics stay clear after replay and signals.

## 4. Long-lived workflows, loops and "cron"

A workflow that loops with sleeps — a merchant's monthly cycle:

```java
while (true) {
    activities.generateStatement(merchantId);
    Workflow.sleep(Duration.ofDays(30));
}
```

is legitimate, and *dangerous*: each iteration adds events (2 timer + 3 activity + workflow-task
events about 10), so after years the history grows to the hard limit (session 9). The fix is
**continue-as-new** at the end of an iteration (or every N iterations). For simple schedules,
Temporal also provides **Schedules** (the server starts a *new* workflow per tick), which avoid
long loops altogether; module 3.1 covers them.

Other timer tools:

| Need | Tool |
|---|---|
| Wait N time | `Workflow.sleep(d)` |
| Wait for condition or deadline | `Workflow.await(d, cond)` |
| Timeout on a promise | `Promise.get(timeout, unit)` / race a timer against an activity |
| Per-activity limits | The activity timeouts (session 6) — not a workflow timer |
| Overall execution limit | `WorkflowOptions.setWorkflowExecutionTimeout` — a blunt last resort |

> **Other engines.** Camunda/BPMN has *timer events* (intermediate catch, boundary, start) and
> expresses "evidence or 30 days" as an **event-based gateway** or a **boundary timer** on a user task
> or message wait — a diagram rather than `await`. Step Functions has `Wait` states and
> task `TimeoutSeconds`. The *pattern* — race an event against a deadline — is universal.

## 5. Traps

1. **Using the wrong clock.** `Thread.sleep`/`Instant.now()` in a workflow. Fix: `Workflow.*`.
2. **A wait without a deadline.** "Wait for the merchant's evidence" with no timeout: the workflow
   waits forever when the merchant never answers.
3. **Huge sleep-loops.** See section 4.
4. **Timer precision.** Timers are not real-time: the server fires them *at or after* the deadline,
   and the next step needs a worker. Do not use them for sub-second accuracy.
5. **Local time zones.** Business deadlines ("end of the business day in Berlin") are calendar
   logic: compute the target instant in an activity or in a deterministic helper with a fixed
   `ZoneId` and pass the duration to `sleep`.
6. **Test blind spot.** Time skipping hides performance and ordering problems that real
   time reveals. Do one smoke test on the real server.

## Lab 5

```bash
mvn -q test -Dtest=Lab05TimersTest
```

Read the three tests. Predict before running; the interesting assertions are the *absent* events.

### Real-timer lab (15 min)

With the dev server and the refund worker running (session 2), add a **temporary** workflow in a
scratch file `CoolingOffWorkflowImpl` (same shape as the lab's `CoolingOffWorkflow` but
`Workflow.sleep(Duration.ofSeconds(60))`), register it in `application.yaml`, restart the worker,
and start it:

```bash
temporal workflow start --type CoolingOffWorkflow --task-queue refund-task-queue \
  --workflow-id cool-1 --input '"r-1"'
temporal workflow show --workflow-id cool-1 --follow
```

While it sleeps, **stop the worker** (Ctrl+C), wait 70 seconds so the timer fires with no worker
alive, then start the worker again. Observe: `TimerFired` has a timestamp ~60 s after
`TimerStarted`; the workflow continues within a second of the worker coming back. Write one
sentence on what that means for a deploy during a long wait.

## Exercises

1. **Model the dispute timeline.** A chargeback arrives. Rules: the merchant has 10 days to confirm
   they will contest; if they confirm, they have 20 more days to upload evidence; if either deadline
   passes, accept the chargeback. Sketch the workflow with `await` calls and signals
   (`confirmContest`, `evidenceReceived`). How many timers are live at once? (One at a time.)
   Write the pseudo-code and the expected history for "confirm on day 3, evidence on day 12".
2. **Spot the bug.**
   ```java
   Instant deadline = Instant.now().plus(Duration.ofDays(30));
   while (Instant.now().isBefore(deadline) && !evidence) { Workflow.sleep(Duration.ofHours(1)); }
   ```
   Two problems: non-deterministic clock; 720 timers instead of one. Rewrite with `Workflow.await`.
3. **Reminder inside a wait.** The merchant should receive a reminder after 3 days of silence but
   the deadline stays at 30 days. Write it with one workflow thread (hint: two sequential awaits:
   3 days, remind, then 27 days) and discuss what you would do if the evidence arrives during the
   first wait (the first `await` returns true, skip the reminder).
4. **Break the test.** In `Lab05TimersTest` replace `env.sleep(Duration.ofDays(10))` with
   `Thread.sleep(100)`. What changes? Why does the test (still) pass or fail?
5. **History cost.** Estimate the events produced by the monthly loop (activity + sleep) over three
   years: (activity triplet 3 + workflow-task triplet 3 + timer 2 + task 3 = about 11) x 36 = ~400.
   Not scary. Now the same loop at every *minute* for a year: ~5.8 million. What is the lesson?

## Checkpoint questions

1. What owns a durable timer and what does the workflow hold while waiting?
2. What does `Workflow.await(Duration, condition)` return and when?
3. In the "evidence on day 10" test, which event is absent and why does it matter?
4. Why should every business wait have a deadline?
5. Why is a `while(true) { sleep; work }` workflow a problem and what is the fix?

<details>
<summary>Answers</summary>

1. The server owns it. The workflow holds nothing — no thread, no connection; it may not even be in
   memory.
2. `true` if the condition became true before the timeout, `false` if the timeout elapsed first.
3. `TimerFired` is absent: the timer never fired because the event won the race. (We also saw no
   `TimerCanceled`.)
4. Otherwise a missing reply leaves the workflow open forever, holding state and blocking a
   decision the business needs (accept/decline).
5. History grows without bound (limits are about 50 000 events / 50 MB); continue-as-new or a
   Schedule.
</details>
