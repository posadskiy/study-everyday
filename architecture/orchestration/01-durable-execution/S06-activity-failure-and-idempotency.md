# Session 6 — Activity failure, timeouts, idempotency (2 h)

| Block | Time | What |
|---|---|---|
| Read | 35 min | Sections 1–3 |
| Lab 8 | 25 min | `Lab08RetriesAndFailuresTest` |
| Read | 20 min | Sections 4–5 |
| Do | 30 min | Exercises (including the idempotent PSP) |
| Check | 10 min | Checkpoint questions |

**Goal.** Know what the engine does when an activity throws or hangs, choose the four timeouts for
a real activity, and write an activity that is safe to run twice. (Module 1.2 goes deeper on
failure *semantics*; this session gives the foundation that module assumes.)

## 1. The central problem: the unknown outcome

Session 5 showed `submitToPsp` running twice. Now think about *why no engine can prevent this*.

```
worker                                PSP
  │ ── POST /refunds (key r-1) ─────▶ │
  │                                   │  money moved
  │ ◀──── (response lost / timeout) ──│
  X  worker waits... times out
```

From the worker's side, three different worlds look identical:

1. The request never arrived — nothing happened.
2. The request arrived and was processed; the response was lost — money moved.
3. The request is still being processed.

The caller **cannot tell which**. Only two policies exist:

- **Do not retry** -> in world 1 the customer is never refunded (*at-most-once*).
- **Retry** -> in world 2 the customer is refunded twice (*at-least-once*).

Payments choose at-least-once *plus idempotency*: retry, but make the repeat harmless. That is the
only honest answer, and it is why the activity side of the table in session 2 says "idempotent".

## 2. Idempotency, concretely

An operation is **idempotent** if doing it N times has the same effect as doing it once. Reading is
idempotent. "Set status = REFUNDED" is idempotent. "Add 1999 to the balance" is **not**.

How to make a non-idempotent effect idempotent: attach an **idempotency key** that identifies the
*intent*, and let the receiver deduplicate on it.

```java
@Override
public String submitToPsp(RefundRequest request, String reservationId) {
    // The key identifies the business intent "refund r-1", not the attempt.
    return pspClient.refund(request.paymentId(), request.amountMinor(), /* idempotencyKey */ request.refundId());
}
```

Properties of a good key:

- **Stable across attempts**: the same key on attempt 1 and attempt 2. A random UUID generated *inside*
  the activity is wrong (a new key each attempt = no protection). Derive it from the business
  object (`refundId`) or pass it in from the workflow, where `Workflow.randomUUID()` is
  deterministic.
- **Unique per intent**: two different refunds of the same payment must have different keys. Use the
  refund ID, not the payment ID.
- **Remembered by the receiver**: Stripe-style PSPs remember keys for a window (commonly 24 h) and
  return the original response for a repeated key. A map in *your* JVM is a toy (session 2/5) — it
  does not survive the process.
- **Checked on the receiving side**: your own DB can enforce it with a unique constraint on
  `(refund_id)`, so the second `INSERT` fails or is ignored.

The same applies to the other steps of the refund:

| Activity | Naive | Idempotent version |
|---|---|---|
| `reserveFunds` | `balance -= amount` | Insert reservation with unique `(refund_id)`; second insert is a no-op returning the existing row |
| `submitToPsp` | POST | POST with idempotency key `refundId` |
| `postLedger` | insert 3 rows | Insert in one transaction with unique `(refund_id, leg)`; or "if exists, return" |
| `notifyCustomer` | send e-mail | Dedup key `refundId:notification`; or accept a rare duplicate e-mail (harmless) |

> Not every step needs rigorous idempotency. Match the rigour to the harm: a duplicate e-mail
> irritates; a duplicate refund costs money.

## 3. What the engine does when an activity fails

Run Lab 8 and read it:

```bash
mvn -q test -Dtest=Lab08RetriesAndFailuresTest
```

| Test | What it shows |
|---|---|
| `transientFailuresAreRetried...` | The PSP stub throws `IllegalStateException("503")` on the first two calls. The engine retries *without any code of yours*; the activity sees `getAttempt()` = 1, 2, 3; the workflow receives the successful third result. The history has **one** `ActivityTaskScheduled` and **no** `ActivityTaskFailed`. |
| `nonRetryableFailure...` | `ApplicationFailure.newNonRetryableFailure("card closed", "CardClosed")` is **not** retried (1 call). The history now contains `ActivityTaskFailed` and the workflow itself fails with `WorkflowFailedException -> ActivityFailure -> ApplicationFailure(type=CardClosed)`. |
| `retriesStopAtMaximumAttempts` | With `maximumAttempts=4` and a permanently failing activity, there are exactly 4 calls (attempts 1..4), then the failure surfaces. |
| `workflowCodeCanCatchTheActivityFailure...` | Inside workflow code, the failure arrives as an `ActivityFailure` you can catch, inspect (`getCause()`), and turn into a business decision — here, return `REFUND_FAILED:CardClosed`. |

### Retryable vs non-retryable: a design decision, not a default

- **Transient** (503, timeout, connection reset, lock timeout): retry.
- **Permanent** (card closed, amount exceeds capture, validation error): do **not** retry; it will
  never work, and retries just delay the answer. Throw a non-retryable `ApplicationFailure`, or
  list the types in `RetryOptions.setDoNotRetry(...)`.
- **Ambiguous** ("PSP says refund pending"): that is *domain state*, not an exception. Model it as a
  result and use a wait (timers, signals) — session 7/8 and module 1.3.

Default retry policy (documented): if you set none, activities retry **forever** with an initial
interval of 1 s, backoff 2.0, capped at 100 s. A bug that always throws will retry for days,
silently. Always set a deliberate policy and a maximum.

### Retry intervals

Our workflow uses `initial 200 ms, backoff 2.0, max 5 attempts`: waits of 0.2 s, 0.4 s, 0.8 s,
1.6 s — total about 3 s. That is fine for a flaky 503 and useless against a 10-minute PSP outage.
Retry policy is a *model of the outage you expect to survive*. If the PSP could be down for 15
minutes, you need either longer backoff (cap at 1 min, up to 20 attempts or an overall deadline), or
a workflow-level decision after failure (park the refund, alert, wait for a signal).

## 4. The four timeouts

Every activity call must have `StartToCloseTimeout` **or** `ScheduleToCloseTimeout`. The four:

```
  ┌─ ScheduleToClose ───────────────────────────────────────────────┐
  │ ┌─ ScheduleToStart ─┐ ┌─ StartToClose (attempt 1) ┐ ... retries  │
  │ │ waiting in queue  │ │ running on a worker       │              │
  │ └───────────────────┘ └───────────────────────────┘              │
  └──────────────────────────────────────────────────────────────────┘
          Heartbeat: "I'm alive" pings inside StartToClose
```

| Timeout | Measures | Fires when | Retry? |
|---|---|---|---|
| **StartToClose** | One attempt: from a worker taking it until it finishes | The attempt runs too long (hung call, dead worker) | Yes, a new attempt |
| **ScheduleToStart** | Time in the queue before any worker takes it | No worker, queue backlog | No (a new attempt would wait in the same queue); usually leave unset, alert instead |
| **ScheduleToClose** | The whole thing across all attempts, from scheduling to final result | Overall deadline for the step | Ends retries |
| **Heartbeat** | Silence between heartbeats from a long activity | The activity stopped heartbeating (dead worker) | Yes |

How to choose for a real PSP refund:

- **StartToClose** = a bit above the worst *normal* call (p99.9 say 10 s -> 30 s). Too small: healthy
  calls get cut off and repeated. Too large: **a worker crash stalls the step for this long** —
  Lab 3 measured it: a 30 s timeout meant a 31 s recovery.
- **ScheduleToClose** = how long you are willing to keep trying *in total*, e.g. 15 minutes for
  the PSP step. Beyond that the workflow should decide something else.
- **Heartbeat** only for long activities (a 20-minute export); there it replaces a large
  StartToClose as the crash detector (heartbeat timeout 10 s -> recovery in about 10 s, not 20 min).

### What a timeout does *not* do

A timeout makes the **engine** give up on the attempt. It does **not** stop the work if the
activity is still running somewhere (or its request is in flight at the PSP). The activity may
still complete the refund after the engine declared the attempt dead. That is why the retry that
follows must be idempotent: *the old attempt may still be finishing while the new one starts*.

## 5. Putting it together: the three layers

| Layer | Failure | Responsibility |
|---|---|---|
| Activity code | One call fails | Throw the right kind of exception; be idempotent |
| Retry policy | Transient failure | Engine retries with backoff, bounded |
| Workflow code | Retries exhausted / non-retryable | Business decision: compensate, escalate, wait |

Module 1.2 maps this to the full failure taxonomy; module 1.3 builds the compensation logic.

## Exercises

1. **Make `submitToPsp` really idempotent in the lab.** Currently the "PSP" dedupe map is in the
   activity object. Move it into a new class `FakePsp` that your activity calls, and make `FakePsp`
   *survive* between worker restarts by writing to a file in `/tmp` (a stand-in for the PSP's
   own memory). Re-run Lab 3 (`crash-lab.sh`). How many *refunds* now exist at the "PSP" after the
   crash? How many *calls*? Explain the difference.
2. **Watch the retries.** Start the real server and worker. Add a property `refund.psp-fail-first=3`
   so the activity throws on the first 3 attempts (write the code like in Lab 8's `FlakyPsp`).
   Start a workflow and run, repeatedly, `temporal workflow describe --workflow-id ...`. Find
   "Pending Activities", the attempt number and the last failure. Then `temporal workflow show`:
   how many `ActivityTask*` events for `SubmitToPsp` at the end?
3. **Pick the timeouts.** For each, give StartToClose / ScheduleToClose / Heartbeat (or "none") and
   one line of justification:
   - validate (a local DB read, p99 20 ms)
   - submitToPsp (p99 4 s, PSP outages up to 10 min)
   - nightly settlement export (20 min, writes a large file)
   - notifyCustomer (e-mail provider, p99 2 s, duplicates are acceptable)
4. **Spot the non-idempotent step.** Which of these is safe to retry blindly? Explain what would
   need to change in the others.
   - `UPDATE refunds SET status='SENT' WHERE id=?`
   - `INSERT INTO ledger (refund_id, amount) VALUES (?, ?)` (no unique constraint)
   - `POST /v1/refunds` without a key
   - `redis.incr("refund-count")`
5. **The finished-but-timed-out attempt.** Lab 8 does not cover this, so reason it through: an
   attempt exceeds its StartToClose by 1 s but finishes at second 31 with success, after the engine
   already scheduled attempt 2 which is also running. Which result does the workflow see? How many
   effects happened at the PSP with and without a key? (Answer: whichever completion the server
   accepts first; without a key the PSP got two requests, with a key it deduplicated.)
6. **Argue for a limit.** Your teammate sets `maximumAttempts` to unlimited "so that refunds never
   fail". Write three sentences on what goes wrong at 3 a.m. Include the phrase "poison input".

## Checkpoint questions

1. In the lost-response scenario, why can't the caller tell whether the PSP refunded?
2. What makes an idempotency key good? Why is a UUID created in the activity bad?
3. What does the history contain for an activity that failed twice and then succeeded?
4. Which timeout bounds crash recovery for an activity without heartbeats?
5. Why must the activity be idempotent even if you use timeouts perfectly?

<details>
<summary>Answers</summary>

1. The network failure hides both outcomes (never arrived / processed but response lost); the
   caller sees only silence.
2. Stable across attempts, unique per intent, remembered by the receiver. A UUID generated per
   attempt differs each time, so the receiver cannot recognise the repeat.
3. One `ActivityTaskScheduled`, one `ActivityTaskStarted` (the last attempt), one
   `ActivityTaskCompleted`. The failed attempts are not events (Lab 8, Lab 3).
4. StartToClose (and ScheduleToClose as an overall cap). With heartbeats, the heartbeat timeout.
5. Because a timeout abandons an attempt, not the work: the abandoned attempt may still finish
   while the next one starts, and a crash can leave the effect done but unrecorded.
</details>
