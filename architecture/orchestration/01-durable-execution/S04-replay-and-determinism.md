# Session 4 — Replay and determinism (2 h)

| Block | Time | What |
|---|---|---|
| Read | 35 min | Sections 1–3 |
| Lab 2b–d | 35 min | The seven tests in `Lab02HistoryAndReplayTest` |
| Read | 15 min | Sections 4–5 |
| Do | 25 min | Exercises |
| Check | 10 min | Checkpoint questions |

**Goal.** Explain precisely what happens when a worker restarts, predict whether a code change will
break running workflows, and know the one weakness of the safety net.

This is the hardest session of the module. Slow down; do the lab even if the text seems clear.

## 1. Replay, step by step

Take the three-step workflow from Lab 2:

```java
public String run(String refundId) {
    activities.step("validate");
    activities.step("reserve");
    activities.step("psp");
    return "done:" + refundId;
}
```

Suppose the history says the first two activities are **completed** and the third has been
**scheduled but not started** (the worker died). A new worker gets a workflow task and has no memory
of this execution. It does this:

```
Worker (replaying)                                History
────────────────────────────────────────────────  ───────────────────────────────
start run(), refundId = "ref-1"                   input from event 1
call activities.step("validate")
   SDK: what does the history say for the         5 ActivityTaskScheduled(Step)
        1st command? -> Step, scheduled           7 ActivityTaskCompleted -> result
   return recorded result immediately (no call!)
call activities.step("reserve")
   SDK: 2nd command -> Step, completed            11/13 ...
   return recorded result immediately
call activities.step("psp")
   SDK: 3rd command -> Step, scheduled,           17 ActivityTaskScheduled
        no result yet                             (history ends)
   -> nothing more to replay: block, wait for the activity
```

Two phases, one piece of code:

1. **Replaying** — while the code's calls match events already in the history, results are served
   from the history. Fast, no I/O, no side effects.
2. **Live** — at the first command not yet in the history, execution continues for real: new
   commands are sent to the server.

The workflow code **does not know** which phase it is in. It cannot, and must not, be able to tell.
That is why it must be deterministic.

### What it costs

- Workflow code runs **more than once** per execution. Anything it does that is not "pure
  computation plus SDK calls" happens again each time.
- The *activities* do not re-run (their results are in the history). This is how "finished steps are
  not repeated".
- To avoid replaying from event 1 on every task, workers keep a **cache** of live executions and
  receive follow-up tasks on a per-worker **sticky queue** (session 5). Replay from scratch happens
  when the cache is cold: after a worker restart, a deploy, eviction, a worker death, or a first
  contact with an execution.

Lab 2b proves "code reruns, activities do not":

```java
WorkflowReplayer.replayWorkflowExecution(history, ThreeStepWorkflow.Impl.class);

assertThat(activities.calls).hasSize(activityCallsBefore);        // no activity ran again
assertThat(ThreeStepWorkflow.BODY_RUNS.get()).isGreaterThan(bodyRunsBefore); // the code did
```

(`BODY_RUNS` is a static counter, which in real workflow code would be a determinism bug; here it is
only a read-only probe.)

## 2. Determinism: the contract

> Given the same input and the same recorded results, the workflow code must issue the **same
> sequence of commands**, every time.

Not "same output". Same *commands*, in the same order. If the code on replay asks for something the
history does not contain at that position, the SDK throws a **non-deterministic error** and the
execution is stuck until you fix it.

Things that break the contract:

| Violation | Why it breaks |
|---|---|
| `Instant.now()`, `System.currentTimeMillis()` | The time differs on replay, so a branch like `if (now.isAfter(deadline))` goes the other way. Use `Workflow.currentTimeMillis()`. |
| `UUID.randomUUID()`, `new Random()` | A different ID on replay; if it flows into an activity argument or a branch, it diverges. Use `Workflow.randomUUID()` / `Workflow.newRandom()`. |
| Direct HTTP / DB / file read | Different answer; possible repeated side effect. Use an activity. |
| `Thread.sleep`, `new Thread`, `ExecutorService`, `CompletableFuture.supplyAsync` | Real threads escape the engine's scheduler. Use `Workflow.sleep`, `Async.function`, `Promise`. |
| Iterating a `HashMap`/`HashSet` and issuing commands | Iteration order is not guaranteed stable across JVMs/runs for some key types. Use sorted or insertion-ordered collections. |
| Static mutable state, singletons shared between executions | Executions interfere; state differs between replays. |
| `if (featureFlag.isEnabled())` read in workflow | The flag may change between first run and replay. Read in an activity so the value is recorded. |
| **Changing the code** while executions are open | The new code issues different commands than the old history contains. This is the big one — module 1.4. |

Things that are *fine*: loops, `if`s over inputs and activity results, local variables, pure
computation, logging (use `Workflow.getLogger`, which suppresses duplicates on replay), calling
`Workflow.*` APIs, activity stubs, child workflow stubs, `Workflow.await`.

> **Why not just forbid I/O in the type system?** The Java SDK cannot. Some other SDKs run workflow
> code in a sandbox that intercepts clocks and randomness (TypeScript, Python partly). In Java the
> discipline is on you, plus replay tests.

## 3. How the SDK detects a violation

Lab 2c replays the recorded history against code that calls a **different activity** in second
position:

```java
activities.step("validate");
activities.audit("reserve");   // history says the 2nd command was Step, not Audit
activities.step("psp");
```

Result:

```
RuntimeException ... NonDeterministicException ... doesn't match event ...
```

The replayer compares each command the code produces with the next event of the history. On the
second command it finds `Audit` where the history has `Step` — mismatch — error. Note two details we
saw:

- The error is **wrapped** — the SDK throws a `RuntimeException` whose message mentions
  `NonDeterministicException`. When you assert in tests, check the message.
- On a live server a non-deterministic error does **not** fail the workflow by default; the workflow
  task fails and is retried forever, and the workflow sits "stuck" until you deploy fixed code. Your
  data is safe; your process is paused. (Operating workflows, module 3.2.)

### The blind spot: arguments are not compared

Lab 2d is the most important five lines of this session:

```java
activities.step("validate");
activities.step("psp");        // was "reserve"
activities.step("reserve");    // was "psp"
```

It **does not fail**. The SDK compared the command *type* (`Step` -> `Step` -> `Step`) and the
activity *name* and found nothing wrong. The recorded results — which belong to the old
"reserve"/"psp" calls — are now handed to the swapped calls. **The workflow silently continues with
the wrong data.**

Rule: the replay safety net checks the *shape* of the command sequence, not the *meaning* of the
arguments. Therefore:

- Treat any change in the order of calls, even between the same activity type, as breaking.
- Use **different activity methods** for different operations (`reserveFunds`, `submitToPsp`), not one
  generic `step(String)` — then a swap *is* caught. The real `RefundActivities` interface does this
  correctly; the lab's `StepActivities.step(String)` is deliberately the anti-pattern.
- Do not rely on replay tests to find argument-level bugs. Write explicit assertions in normal tests.

## 4. A replay test belongs in CI

`WorkflowReplayer` runs the *current* code against *recorded* histories. That means you can protect
yourself before a deploy:

```java
// pseudo-CI step
WorkflowExecutionHistory history = WorkflowExecutionHistory.fromJson(Files.readString(path));
WorkflowReplayer.replayWorkflowExecution(history, RefundWorkflowImpl.class);
```

Where do histories come from? Export from the UI/CLI (`temporal workflow show --output json`) for
representative executions, commit them to `src/test/resources/histories/`, replay all of them on
every build. Module 1.4 builds this properly: it is the single best defence against deploy-time
incidents, and it is a recurring interview topic.

## 5. Replay and the "second language" of the track

BPMN engines do not replay code. Camunda 8/Zeebe records progress of a **token** along a diagram;
after a restart it resumes the token from its last recorded position. There is no determinism
requirement on the *diagram*, but a *change of the diagram* while instances are running has its own
rules (instance migration). Same problem, different mechanics. Module 3.4/3.5 returns to this; for
now remember: determinism is Temporal's price for "the workflow is ordinary code".

## Lab 2b–d

```bash
mvn -q test -Dtest=Lab02HistoryAndReplayTest
```

Read all four tests. For each, predict pass/fail before running. Then:

1. In `DifferentActivityImpl`, change `audit` back to `step`. The test now fails (replay no longer
   throws). Why? (It is the control group — shows the failure came from your change.)
2. **Append a step.** `appendingAStepFailsAgainstAFinishedHistory` adds `activities.step("notify")`
   at the end and replays a history of a *finished* execution. Predict first. It **fails**:

   ```
   NonDeterministicException: [TMPRL1100] ... Event 23 of type EVENT_TYPE_WORKFLOW_EXECUTION_COMPLETED
   does not match command type COMMAND_TYPE_SCHEDULE_ACTIVITY_TASK.
   ```

   The history says "after `psp` the workflow completed"; the new code says "after `psp`, schedule
   another activity". The surprise to take away: *even adding a step at the end is a breaking
   change for executions that already got past that point.* (For executions still before it, the
   new code would simply run the new step — which may or may not be what you want for money. Module
   1.4 solves this with `Workflow.getVersion`.)
3. **Remove the first step** (`removingTheFirstStepFails`). The message says the history has
   `ACTIVITY_TASK_SCHEDULED` at event 17 but the code issued `COMPLETE_WORKFLOW_EXECUTION`: after
   removing a step, the two-step code reaches `return` while the history still expects a third call.
   Note which event number the SDK reports; it is the first place where history and code disagree.
4. **Insert a timer** (`insertingATimerFails`): history has `ActivityTaskScheduled` at event 11 but the
   code now issues `START_TIMER`. Read the message and the error code `TMPRL1100`; this is the
   string you will search for in logs during incidents.

Observation for all three: the SDK tells you the **event number** and the **command type** that
diverged. That is enough to find the offending line without a debugger.

## Exercises

1. **Explain to a colleague.** In five sentences, without the word "magic", explain why a workflow
   that was running for three days on an old worker continues correctly after that worker is killed.
   Use: history, replay, activity results, commands.
2. **Find the bug.** What is wrong with each?
   ```java
   // a
   String id = UUID.randomUUID().toString();
   activities.submitToPsp(request, id);
   // b
   if (LocalTime.now().isBefore(LocalTime.of(17, 0))) activities.notifyNow(request);
   // c
   for (String merchant : merchantsByRegion.keySet()) activities.settle(merchant); // HashMap
   // d
   boolean eu = configService.isEuMerchant(request.merchantId()); // a REST call
   ```
   Give the fix for each (answers: a -> `Workflow.randomUUID()`; b -> `Workflow.currentTimeMillis()`
   or decide in an activity; c -> sorted keys / `LinkedHashMap` / `TreeMap`; d -> activity).
3. **Silent corruption.** Construct, in the lab, a change that passes replay but changes behaviour
   incorrectly (hint: Lab 2d). Then redesign `StepActivities` so the same change is caught.
4. **What triggers a replay?** List five production events that make a worker replay an execution
   from event 1. Which one happens on every deploy? (Answer: all of them involve a cold cache; a
   rolling deploy restarts every worker.)
5. **Cost thinking.** An execution has 40 000 events. A worker evicts it from cache and receives a
   signal. What happens? Why is a bound on history size (session 9) not only about limits but also
   about the cost of replay?

## Checkpoint questions

1. In one sentence, what does replay do and what does it not do?
2. What does the determinism contract require, exactly?
3. What does the replayer compare, and what does it not compare?
4. Name four sources of non-determinism and the SDK-safe replacement of each.
5. Why do you want a replay test in CI?

<details>
<summary>Answers</summary>

1. It re-runs the workflow code from the start, feeding recorded results to its calls so it
   reaches the same state; it does not re-run activities.
2. Same input + same recorded results -> same sequence of commands in the same order.
3. The command *type and identity* (activity name, timer, child workflow...) against the recorded
   events. It does not compare activity *arguments* or the contents of your state.
4. Time (`Workflow.currentTimeMillis`), randomness (`Workflow.randomUUID`/`newRandom`), I/O
   (activities), threads (`Async`/`Promise`/`Workflow.sleep`), unstable iteration (ordered
   collections), feature flags (read via activity).
5. A deploy that changes the command sequence strands all running executions; replaying real
   histories against the new code finds that before production does.
</details>
