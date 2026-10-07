# Session 2 — Workflow vs activity: the split and why it exists (2 h)

| Block | Time | What |
|---|---|---|
| Setup | 30 min | Install tools, run the existing tests, start the dev server |
| Read | 35 min | Sections 1–4 |
| Lab | 25 min | Run `RefundWorkflow` for real, read the result |
| Think | 20 min | Exercises |
| Check | 10 min | Checkpoint questions |

**Goal.** Look at the real `RefundWorkflow` and say, for every line, whether it is a *decision* or
a *side effect* — and why the engine forces you to separate them.

## 1. Setup

```bash
temporal --version                                   # CLI installed?
cd architecture/orchestration/payment-disputes
mvn -q test                                          # all green? (about 15 s)
```

Terminal 1 — the server (state is in memory; stopping it forgets everything, which is fine for
labs):

```bash
temporal server start-dev
```

Terminal 2 — your worker (the Spring Boot app):

```bash
cd architecture/orchestration/payment-disputes
mvn -q spring-boot:run
```

Open <http://localhost:8233> — the Temporal Web UI. Leave it open for the whole module. It is the
window into the engine; you will use it more than your IDE.

## 2. Read the real code

[`RefundWorkflowImpl`](../payment-disputes/src/main/java/com/posadskiy/orchestration/payment/refund/RefundWorkflowImpl.java):

```java
@WorkflowImpl(taskQueues = RefundWorkflowImpl.TASK_QUEUE)
public class RefundWorkflowImpl implements RefundWorkflow {

    private final RefundActivities activities = Workflow.newActivityStub(
            RefundActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(30))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(5)
                            .setInitialInterval(Duration.ofMillis(200))
                            .setBackoffCoefficient(2.0)
                            .build())
                    .build());

    @Override
    public RefundResult process(RefundRequest request) {
        activities.validate(request);
        String reservationId = activities.reserveFunds(request);
        String pspReference = activities.submitToPsp(request, reservationId);
        activities.postLedger(request, pspReference);
        activities.notifyCustomer(request, pspReference);
        return new RefundResult(request.refundId(), RefundResult.RefundStatus.COMPLETED, pspReference);
    }
}
```

Read it as a human first. It says: validate, reserve, call the PSP, post the ledger, notify,
return. Almost exactly the five steps from session 1. **That is the point of durable execution:
the code reads like the naive version, but survives crashes.**

Now read it as an engineer, asking four questions.

**Q1. What is `activities`?** Not the real implementation. `Workflow.newActivityStub` returns a
*proxy*. Calling `activities.validate(request)` does **not** run `validate`. It tells the SDK
"I want activity `Validate` to run with this argument". The SDK records that request in the event
history, the server puts it on a task queue, a worker somewhere runs the real method, and the result
comes back through the history. Your workflow code simply *looks* like it made a method call.

**Q2. Where does the workflow code run?** On a worker, inside a sandboxed workflow thread. The
workflow code never runs "on the server".

**Q3. Where do the activities run?** On a worker too — possibly a different one, possibly on
another machine, possibly a different language. In this repository both live in one Spring Boot app,
but that is a deployment choice, not part of the model.

**Q4. What do the `ActivityOptions` mean?** They are the *contract* for how the engine babysits each
call: how long one attempt may run (`startToCloseTimeout` 30 s), how many attempts (5), how long
before the second attempt (200 ms), and the growth factor (x2: 200 ms, 400 ms, 800 ms, 1.6 s).
Session 6 goes deep. Notice they are options on the *call*, not code you wrote — the engine does the
retrying, not a `for` loop of yours.

## 3. The split

| | Workflow | Activity |
|---|---|---|
| What it is | The **plan**: order, decisions, waiting | One **action** on the outside world |
| Example | "Reserve, then PSP, then ledger; if X wait 7 days" | "POST /refunds to the PSP", "INSERT ledger row" |
| May do I/O? | **No** — not directly | **Yes** — that is its job |
| May read the clock / random? | Only via `Workflow.currentTimeMillis()` / `Workflow.randomUUID()` / `Workflow.newRandom()` | Freely |
| Runs how often? | **Many times** — re-executed on every replay | Once per *attempt*; possibly several attempts |
| Fails by | Throwing a business exception (rare, deliberate) | Throwing; the engine retries per policy |
| Persisted as | Its *decisions*, in the history | Its *result* (or failure), in the history |
| Must be | **Deterministic** | **Idempotent** (as far as possible) |

Memorise the last row: **workflow = deterministic, activity = idempotent.** Nearly every bug in this
module is a violation of one of these two words.

### Why the split exists

Imagine the engine's job: after a crash, rebuild your process's exact state. There are two
strategies:

1. **Snapshot** the whole memory (heap, stack, threads) periodically. Heavy, language specific,
   and can never be right between snapshots.
2. **Re-run the code**, feeding it the recorded results of everything non-deterministic. If the code
   is a pure function of "its input plus those results", re-running gives back the identical state.

Temporal does (2). It only works if the workflow code is *a pure function of its recorded inputs*.
So the engine must split the program into:

- the **pure part** (decisions) — re-runnable at will -> *workflow*
- the **impure part** (anything that touches the world, and therefore can give a different answer
  each time) -> *activity*, whose answer is recorded once and replayed forever.

Read that again; the whole module is a footnote to it. An activity is a **recorded
non-determinism**. The workflow reads "what happened last time" from the history instead of asking
the world again.

### Rule of thumb for any line of code

Ask: *if I ran this line twice, an hour apart, could the answer differ or a second effect occur?*

- `LocalDate.now()` — answer differs -> not allowed in workflow; use `Workflow.currentTimeMillis()`.
- `UUID.randomUUID()` — differs -> use `Workflow.randomUUID()`, or generate it in an activity.
- `restClient.post(...)` — a second effect occurs -> activity.
- `if (amount > 10_000) {...}` — same answer from the same input -> workflow, fine.
- Reading a config value or feature flag — can change between runs -> read it in an activity (or
  pass it in as input) so the *value* is recorded. (Versioning in module 1.4 covers code changes.)
- `Thread.sleep(...)`, `new Random()`, reading a file, a static mutable counter -> not in workflow.

## 4. Activities in this repository

[`RefundActivitiesImpl`](../payment-disputes/src/main/java/com/posadskiy/orchestration/payment/refund/RefundActivitiesImpl.java)
is a plain Spring bean. Look at `submitToPsp`:

```java
return pspByRefundId.computeIfAbsent(request.refundId(), id -> {
    log.info("submitToPsp refundId={} reservationId={}", id, reservationId);
    sleep(pspDelay);
    return "psp-" + UUID.randomUUID();
});
```

It keys the "PSP" by `refundId` — a toy version of an *idempotency key*. A real PSP accepts the
same key twice and returns the same refund. Note the weakness we will exploit in session 5: the map
lives **in the JVM**, so a different worker process does not know about it. In real life the key goes
to the PSP, which remembers it for you.

Also note what is *not* in the workflow: no try/catch around the calls, no retry loop. If
`submitToPsp` throws, the engine retries it. If it keeps failing, the exception surfaces from
`activities.submitToPsp(...)` into the workflow, wrapped in an `ActivityFailure`. Whether to catch
it is a business decision (module 1.2/1.3).

## Lab — run a refund on the real server

With the server and worker from section 1 running (Terminal 3):

```bash
temporal workflow start \
  --type RefundWorkflow \
  --task-queue refund-task-queue \
  --workflow-id refund-s2-1 \
  --input '{"refundId":"r-1","paymentId":"p-1","amountMinor":1999,"currency":"EUR"}'

temporal workflow show --workflow-id refund-s2-1
```

You should see the worker log lines `validate`, `reserveFunds`, `submitToPsp`, `postLedger`,
`notifyCustomer` in Terminal 2, and in Terminal 3 the history (next session decodes it) and the
result `{"pspReference":"psp-...","refundId":"r-1","status":"COMPLETED"}`.

Then in the Web UI: open the workflow `refund-s2-1`, click the **History** tab, switch between
*Timeline*, *Compact* and *JSON* views. Click on an `ActivityTaskCompleted` event and find the
recorded result.

### Prediction questions (write the answer first, then run)

1. Start a second workflow with the **same** `--workflow-id`. What happens? (Try it, once after the
   first has finished and once while a slow one — session 5's `--refund.psp-delay` — is still
   running. Measured here: after the first one **completed**, the second start was accepted and
   created a new run with a new run ID. While one is **running**, the default is to refuse the start
   (documented). The CLI flags `--id-reuse-policy` and `--id-conflict-policy` change this. Think about
   which behaviour you want for "start refund r-1": you want *at most one* refund per refund ID.
   Using the refund ID as the workflow ID is a second layer of idempotency.)
2. Start a workflow with a *new* ID but the **same** `refundId`. What does `submitToPsp` do? Why is
   that not enough protection in production?
3. Stop the worker (Ctrl+C), start another workflow, look at it in the UI. What state is it in? Start
   the worker again. (Session 5 explains; predict now.)

## Exercises

1. **Classify.** For each, say *workflow* or *activity* and why:
   - Compute `fee = amount * 0.029 + 30` for a refund
   - Look up the merchant's refund policy in a database
   - Send a Slack message when a refund exceeds 5 000 EUR
   - Decide to skip the notification when the customer has opted out (the opt-out *value* came from
     an earlier activity)
   - Generate a refund ID
2. **Wrong on purpose.** In a scratch copy of `RefundWorkflowImpl.process`, add
   `log.info("started at {}", java.time.Instant.now())` and run the workflow. It works. Why is that
   still a bad habit? (You will prove it in session 4, where a non-deterministic *decision* breaks
   replay. A log line is mostly harmless, but `if (Instant.now().getHour() > 17)` is not.)
3. **Move a step.** Merge `validate` and `reserveFunds` into one activity `validateAndReserve`. What
   did you gain (fewer round trips, atomic in one DB transaction)? What did you lose (visibility of
   which one failed; independent retry)? When would you choose each?
4. **Granularity.** `postLedger` writes several rows (debit, credit, fee). One activity or three?
   Think about what a crash between the first and second row means. (Answer sketch: one activity
   that writes all rows in one DB transaction — activities should be *atomic units of the local
   system*.)

## Checkpoint questions

1. What is the object returned by `Workflow.newActivityStub` and what does calling a method on it do?
2. Give the one-word property of workflows and the one-word property of activities.
3. Why can workflow code not call a REST API directly?
4. Where do workflow code and activity code run, and does the server ever run them?
5. State the "run it twice an hour apart" rule and apply it to `Instant.now()`.

<details>
<summary>Answers</summary>

1. A proxy. Calling a method does not execute the activity: it records a request to schedule that
   activity in the history and waits for the result recorded there.
2. Workflow: deterministic. Activity: idempotent.
3. The workflow is re-executed on replay; a direct call would repeat the HTTP request each time,
   and might return different data, breaking determinism. The call belongs in an activity, whose
   result is recorded once.
4. Both run on your workers. The server only stores history and queues tasks; it never executes
   your code.
5. If running the line twice, an hour apart, could give a different answer or repeat an effect, it
   does not belong in a workflow. `Instant.now()` differs, so use `Workflow.currentTimeMillis()`.
</details>
