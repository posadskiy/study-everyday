# Session 5 — Workers, task queues, polling — and a real crash (2 h)

| Block | Time | What |
|---|---|---|
| Read | 30 min | Sections 1–3 |
| Lab 4 | 15 min | Tasks wait for workers (test) |
| Lab 3 | 40 min | Kill a worker in the middle of an activity (script) |
| Analyse | 20 min | Section 5, explain every line of the output |
| Do | 10 min | Exercises |
| Check | 5 min | Checkpoint questions |

**Goal.** Know exactly which process does what, see an execution move between workers after a
`kill -9`, and be able to say how long recovery takes and why.

## 1. Who does what

```
 ┌──────────┐   start workflow / signal / query    ┌──────────────────────┐
 │  Client  │ ───────────────────────────────────▶ │  Temporal Server     │
 │ (your    │                                      │  - stores history    │
 │  API)    │ ◀─────────── result ──────────────── │  - holds task queues │
 └──────────┘                                      │  - owns timers       │
                                                   └──────────┬───────────┘
                                          long-poll ▲         │ task
                                                    │         ▼
                                              ┌───────────────────────┐
                                              │  Worker (your JVM)    │
                                              │  - runs workflow code │
                                              │  - runs activity code │
                                              └───────────────────────┘
```

- The **server** never runs your code. It stores event histories, holds **task queues**, runs
  durable timers, and makes sure every task is eventually handed to someone.
- A **worker** is your process (here: the Spring Boot app). On start it connects to the server and
  **long-polls** one or more task queues: "any work for me?". The worker connects *out* to the
  server; the server never connects in. (Firewalls, scaling and zero-downtime deploys all benefit.)
- A **client** starts workflows and talks to them (signal, query, update, cancel).

### Two kinds of task

| Task | Created when | A worker does | Result |
|---|---|---|---|
| **Workflow task** | A workflow starts, or an event it is waiting on happens | Runs workflow code until it blocks, returns commands | Commands -> events |
| **Activity task** | The workflow asked to schedule an activity | Runs the activity method | `ActivityTaskCompleted` / failure |

Both kinds travel through a task queue named in your configuration. In `application.yaml`:

```yaml
workers:
  - task-queue: refund-task-queue
    workflow-classes: [ ...RefundWorkflowImpl ]
    activity-beans:   [ refundActivitiesImpl ]
```

So this worker polls `refund-task-queue` for both workflow tasks and activity tasks.

### Task queues are a design tool

A task queue is just a name. You decide what to put behind it:

- **Scaling:** add more worker processes polling the same queue; tasks are distributed. No code
  change.
- **Isolation:** `refund-task-queue` (fast, cheap) vs `llm-task-queue` (slow, rate limited, needs
  API keys). A flood of slow LLM activities cannot starve refunds.
- **Different hardware or language:** a queue served only by a GPU box; an activity written in
  Python, workflow in Java.
- **Rate limiting** per queue (`maxTaskQueueActivitiesPerSecond`) — protecting a PSP.

## 2. Tasks wait; nothing is lost (Lab 4)

```bash
mvn -q test -Dtest=Lab04TasksWaitForWorkersTest
```

The test starts a workflow **before any worker is running**, then reads its history:

```
BEFORE WORKER EVENT_TYPE_WORKFLOW_EXECUTION_STARTED
BEFORE WORKER EVENT_TYPE_WORKFLOW_TASK_SCHEDULED
```

Two events: the workflow exists, its first task sits in the queue. Then a worker begins polling,
takes the task, and the workflow finishes normally with all three activities executed.

Consequence for operations: **a deploy that takes workers down for two minutes does not fail
workflows.** It delays them. What matters is the *schedule-to-start* latency (how long tasks wait for
a worker); watch it in production (module 3.2).

## 3. Sticky queues — why replay is usually cheap

If every workflow task were handled by a random worker, each would replay the whole history. To
avoid that, after a worker runs a workflow task it **caches** the execution and the server sends the
*next* task for that execution to the same worker through a **sticky queue** (a queue named for that
worker). The worker applies the new event to its cached state: no replay.

If the sticky worker does not pick the task up in time (default **5 seconds** schedule-to-start for
sticky tasks, per Temporal docs — I did not measure this one here), the server falls back to the
normal queue and *any* worker takes it, **replaying from the history**. That is the safety net when
a worker dies *between* activities. When a worker dies *during* an activity, another timer matters
— see next.

Practical implications:

- Replay is the exception on a healthy system, and the norm after deploys.
- A worker's cache has a size limit (`maxCachedWorkflows`): workflows get evicted and replayed.
  Hence determinism must hold *always*, not just after crashes.

## 4. Lab 3 — kill a worker in the middle of an activity

This lab runs real processes. It is a script so you can repeat it; read it first:
[`scripts/crash-lab.sh`](scripts/crash-lab.sh).

What it does:

1. Starts `temporal server start-dev --headless`.
2. Starts **worker 1** with `--refund.psp-delay=PT25S`: its `submitToPsp` takes 25 seconds to give
   you time to kill it. (The property is the lab hook added to `RefundActivitiesImpl`.)
3. Starts a refund workflow.
4. When the log shows `submitToPsp`, runs `kill -9` on worker 1.
5. Starts **worker 2** (fast PSP: 1 s) and follows the workflow to completion.
6. Prints the logs of both workers and the workflow history.

Run it from the repository root (takes about 100 s: it builds the classpath on first run):

```bash
./architecture/orchestration/01-durable-execution/scripts/crash-lab.sh
```

**Before running, write your predictions:** How many times will `validate` run in total? How many
times `submitToPsp`? How long until the refund completes? What will the history look like?

### The real result

This is the output from a run of the script on the lab machine:

```
>>> worker 1 is inside submitToPsp. Killing it with SIGKILL.
>>> finished after 32 s

--- worker 1 (killed) ---
13:56:44.966  validate refundId=r-1 paymentId=p-1
13:56:44.986  reserveFunds refundId=r-1 reservationId=res-r-1
13:56:44.996  submitToPsp refundId=r-1 reservationId=res-r-1
                                                          <- killed here, mid-sleep

--- worker 2 ---
13:57:16.036  submitToPsp refundId=r-1 reservationId=res-r-1
13:57:17.189  postLedger refundId=r-1 pspReference=psp-ab3b...
13:57:17.197  notifyCustomer refundId=r-1 pspReference=psp-ab3b...
```

History (events 14–35; everything before event 14 is the uneventful start, identical to a
normal run):

```
 14  13:56:44  WorkflowTaskScheduled
 15  13:56:44  WorkflowTaskStarted
 16  13:56:44  WorkflowTaskCompleted
 17  13:56:44  ActivityTaskScheduled          <- SubmitToPsp requested
 18  13:57:15  ActivityTaskStarted            <- worker 2's attempt, 31 s later
 19  13:57:17  ActivityTaskCompleted          <- result recorded (PSP delay on worker 2 was 1 s)
 20  13:57:17  WorkflowTaskScheduled          ...and the rest proceeds normally
 ...
 35  13:57:17  WorkflowExecutionCompleted
```

Totals: **35 events, the same count as an uneventful run.** The crash left no marker of its own.

## 5. Read the evidence

Go through it line by line. Everything below is something you can *see*, not an assumption.

1. **`validate` and `reserveFunds` ran once, on worker 1 only.** Worker 2's log has no `validate` or
   `reserveFunds`. This is "finished steps are not repeated": their results were in the history, so
   worker 2 did not call them. (Worker 2 did *replay the workflow code* from the start, but the
   activity stub calls were answered from history.)
2. **`submitToPsp` ran twice** — once on worker 1 (never finished) and once on worker 2. That is
   at-least-once execution. In this lab the "PSP" is a map inside each JVM, so worker 2 had no
   memory of worker 1's attempt. **A real PSP might have already sent the money on the first
   attempt**; we cannot know. The only defence is an idempotency key the PSP remembers — session 6.
3. **Recovery took about 31 seconds** (13:56:44 -> 13:57:15), which is the `startToCloseTimeout` of
   30 s plus a moment. Why? The server cannot tell "worker is slow" from "worker is dead". With no
   heartbeat from the activity, it waits until the attempt's start-to-close timeout expires, then
   schedules the next attempt. **Recovery time from a crash mid-activity is bounded by your timeout
   choice.** A 30-minute timeout means a 30-minute stall after a crash. (Heartbeating — module 1.2 —
   shortens this; it lets the server notice silence within a heartbeat timeout.)
4. **The failed first attempt is not in the history.** Events 17 -> 18 look like one normal
   scheduling and start. If you run `temporal workflow describe` while the activity is waiting for
   its retry, you see the pending activity with `Attempt: 2` and a last-failure of type timeout.
5. **No operator action.** We started worker 2 ourselves to simulate "the orchestrator restarted a
   pod". There was no recovery code, no cron, no manual step. The workflow was merely *waiting*.
6. **The workflow was never "failed".** Its state stayed Running throughout. The crash was an
   activity-level problem that the retry policy absorbed.

> If worker 1 had died between two activities (say after `ActivityTaskCompleted` for `ReserveFunds`
> but before it asked for `SubmitToPsp`), recovery would have been faster: the workflow task would
> time out on the sticky queue (about 5 s) and be re-delivered to any worker, replaying the code and
> continuing. The in-process test server could not reproduce this scenario, so it is documented
> behaviour, not something these labs verified.

## 6. What to take from the lab

- **State lives in the server, not in workers.** Workers are disposable. This is what makes
  rolling deploys and spot instances safe.
- **At-least-once for activities.** You must design activities so that a repeat is harmless.
- **You pay for crash recovery in timeouts.** Choose them deliberately.
- **The history shows *what was decided and completed*, not *what was attempted*.** To see attempts
  you need logs and metrics.

## Exercises

1. **Change the timeout.** In `RefundWorkflowImpl` set `startToCloseTimeout` to 10 s. Re-run the
   script. Predict the recovery time. Then set it to 60 s, predict again. (Remember the PSP delay
   in the lab is 25 s; with a 10 s timeout the activity will **time out even without a crash** —
   what do you see? That is a *different* failure, and it also repeats the PSP call. Use this to
   understand why timeouts must exceed the real duration.)
2. **Two live workers.** Start two workers (different ports, same queue) *without killing anything*,
   and start ten workflows. In the logs, which worker runs which activity? Do the activities of one
   workflow stay on one worker? (They need not; different tasks of the same workflow can go to
   different workers. That is why activities must not rely on local process state.)
3. **Kill workflow-task time.** Stop the only worker, start a workflow, wait 30 s, restart the
   worker. What does the history look like? What is the schedule-to-start latency? (Compare with
   Lab 4.)
4. **Separate queues.** Move `notifyCustomer` to its own activity class served by a different task
   queue (`notification-queue`) and a second worker. Make the notification worker slow. Does the
   refund pipeline for *other* refunds stay fast? Why is that useful? (Hint: you set the queue via
   `ActivityOptions.setTaskQueue`.)
5. **Draw it.** Draw the sequence diagram for the crash lab: client, server, worker 1, worker 2,
   with every event of 11–19 placed on it. This is the "draw a workflow's event history for a
   refund that crashed and explain what replay does" item of the final check — do it now, again in
   session 10.

## Checkpoint questions

1. Who runs the workflow code, who runs the activity code, who runs neither?
2. What happens to a started workflow if no worker is polling its queue?
3. Why does a worker crash during an activity cost about one `startToCloseTimeout`?
4. How many times did `submitToPsp` run in Lab 3 and why does this matter for money?
5. Give two reasons to use more than one task queue.

<details>
<summary>Answers</summary>

1. Workers run both; the server runs neither.
2. It stays Running with a scheduled workflow task waiting; when a worker starts polling, it
   proceeds. Nothing is lost (Lab 4).
3. Without a heartbeat, the server only learns of the loss when the attempt times out, then
   schedules a retry.
4. Twice. At-least-once means a repeat after a crash; the PSP call must be idempotent via a key the
   PSP remembers.
5. Isolation of slow/limited workloads from fast ones; independent scaling and rate limiting;
   different hardware/language.
</details>
