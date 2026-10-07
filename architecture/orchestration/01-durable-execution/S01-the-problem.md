# Session 1 — The problem: why a plain refund breaks (2 h)

| Block | Time | What |
|---|---|---|
| Read | 30 min | Sections 1–3 below |
| Lab 1 | 30 min | Run, predict, break `Lab01NaiveRefundTest` |
| Think | 30 min | Exercises 1–3 |
| Read | 20 min | Section 4 (the four ways to fix it) |
| Check | 10 min | Checkpoint questions |

**Goal.** Before you learn the cure, feel the disease. By the end you can explain, with a concrete
timeline, why "retry the whole thing" is wrong for money, and name the four properties any fix must
have.

## 1. A refund is not one action

A customer asks for a refund of 19.99 EUR. In a real payment system this is at least five steps:

1. **Validate** — is the payment refundable, is the amount within what was captured?
2. **Reserve** — mark the amount as "being refunded" so a second request cannot take the same money.
3. **Call the PSP** — the payment service provider moves money back to the customer's card.
4. **Post to the ledger** — record the movement in your own books (double entry).
5. **Notify** — tell the customer.

Each step is a different system with its own failure modes: your database, the PSP's HTTP API, your
ledger service, the e-mail provider. There is **no transaction that spans all five**. A database
transaction covers step 2 and 4 if they are in the same database, but a PSP will never join your
transaction. This is the core fact of payment engineering: *you are always coordinating systems
that cannot commit together*.

## 2. The simplest code, and where it dies

Open [`Lab01NaiveRefundTest.java`](../payment-disputes/src/test/java/com/posadskiy/orchestration/payment/lab11/Lab01NaiveRefundTest.java).
The service is deliberately naive:

```java
void refund(String refundId, long amountMinor) {
    world.pspRefunds.add(refundId + ":" + amountMinor);      // step 1: money leaves
    if (world.crashAfterPsp) {
        throw new IllegalStateException("process killed");   // <- the process dies here
    }
    world.ledgerEntries.add(refundId + ":" + amountMinor);   // step 2: we record it
}
```

Two tests, two timelines.

**Test 1 — crash between PSP and ledger.**

```
t0  PSP: refund 1999 sent        -> customer has the money
t1  process killed
t2  ledger: (never written)      -> our books say nothing happened
```

Result: `pspRefunds` has one entry, `ledgerEntries` is empty. The company has paid out money it does
not know it paid. At the end of the day reconciliation will show a gap, and a human will spend an
hour finding it. Multiply by thousands of refunds and a bad deploy.

**Test 2 — the "obvious" fix, retry everything.**

```
t0  PSP: refund 1999 sent        -> customer has the money
t1  process killed
t2  restart, retry the whole refund
t3  PSP: refund 1999 sent AGAIN  -> customer refunded twice
t4  ledger: written once
```

Result: `pspRefunds` has two entries, `ledgerEntries` one. Now the customer got **double** the money
and the books are still wrong. Retry made it worse.

Run it:

```bash
cd architecture/orchestration/payment-disputes
mvn -q test -Dtest=Lab01NaiveRefundTest
```

Both tests pass — they *assert the bad outcome*. That is a useful trick: a test that documents a
failure keeps the failure real.

## 3. What a crash really is

"The process crashed" sounds rare. It is not. In production, all of these are the same event from
your code's point of view — execution stops at an arbitrary line and never resumes:

- a deploy (the old pod is terminated, often mid-request)
- an out-of-memory kill
- a node eviction or a spot-instance reclaim
- a network partition (the process runs, but nobody can tell)
- `kill -9`, a power failure, a bug that throws in the wrong place

The dangerous property is **arbitrary position**: a crash can land between *any* two lines. Your
code must be correct for every such position. With five steps there are four dangerous gaps; with a
loop over 200 payouts there are 200.

> **Key idea.** Correctness under crash is not a property of one function. It is a property of the
> *sequence*: for every point where the sequence can stop, there must be a way to continue (or undo)
> that leaves the money and the books consistent.

## 4. Four ways people fix it (and what they cost)

| Approach | How it works | Where it hurts |
|---|---|---|
| **A. Do-it-yourself state machine** | A `refund` table with a `status` column (`RESERVED`, `PSP_SENT`, `LEDGERED`...). A cron job picks up stuck rows. | You are writing a workflow engine, badly: locking, polling, retries, timeouts, visibility. Every team re-invents it. |
| **B. Message queue with consumers** | Each step publishes a message that triggers the next. | The *process* exists only implicitly across queues. "Where is refund 17?" has no good answer. Retries and dead letters are per-queue. |
| **C. Saga with an orchestrator** | One component owns the sequence and the compensations. | This is the right *shape*. The question is only who runs and persists the orchestrator. |
| **D. Durable execution** | You write the sequence as ordinary code; the engine records each step's result and resumes your code after any crash. | New mental model (this module). Constraints on workflow code (determinism). Operating the engine. |

Durable execution is option C with the persistence problem solved: **your sequence is written as
normal code, and the engine makes that code survive crashes.**

Any fix, by whatever name, needs four properties. Learn them — you will meet them in every later
module:

1. **Durable progress** — the fact that step 3 finished is stored outside your process.
2. **Resume** — after a crash, something continues from that stored progress.
3. **No lost step, no silent repeat of a finished step** — except where unavoidable.
4. **Visibility** — you can ask "where is refund 17 right now?" and get a precise answer.

Note the careful wording in 3. Durable execution does **not** promise "each step happens exactly
once". No engine can (session 6 shows why). It promises that finished steps are not repeated and
unfinished steps are retried — which makes *idempotent* steps safe.

> **Other engines.** Step Functions, Conductor, Camunda/Zeebe, Azure Durable Functions all provide
> these four properties. They differ in how you express the sequence (JSON, BPMN, code) and in how
> they resume (replay of code vs. a token moving through a diagram). Module 3.4 covers the second
> family.

## Lab 1 — break it

Edit `Lab01NaiveRefundTest` (do not commit the changes):

1. Add a step "reserve" before the PSP call that writes to `world.reservations`. Crash *after* the
   reservation but *before* the PSP. What state is left? Can a retry get it wrong? (Hint: what if
   the retry reserves again?)
2. Make the crash happen *between the PSP request leaving and its response arriving* — model that as
   `world.pspRefunds.add(...)` happening and then throwing. From the caller's point of view, is
   this different from a crash after the call returned? (This is the "unknown outcome" problem that
   dominates session 6.)
3. Make the naive service idempotent by checking `pspRefunds` for the refund ID before sending.
   Does that fix test 2? Does it fix test 1? What does your check do if the crash happens *during*
   the check?

## Exercises

1. **Enumerate the gaps.** For the five-step refund list every pair of adjacent steps and write down
   what is true in the world if the process dies exactly there (money moved? books updated?
   customer told?). Which gaps are harmless, which lose money, which double-pay?
2. **Cron vs engine.** Option A ("status column + cron") works for small systems. Write down three
   things you would need to add once there are 50 workflows and 3 teams. (Examples to consider:
   two cron instances picking the same row, a step that takes longer than the cron interval, a
   status that never advances.)
3. **Your own system.** Pick one multi-step process you have worked on (onboarding, payout, order
   fulfilment). Draw its steps and mark the gaps. Which of the four approaches did it use? Keep this
   page — you will reuse it as a story in the interview module.

## Checkpoint questions

Answer in writing without looking back.

1. Why can't a database transaction make a refund atomic?
2. In test 2, the retry made things worse. What exactly did the retry not know?
3. Name the four properties of a fix.
4. Why is "exactly once" the wrong thing to promise?

<details>
<summary>Answers</summary>

1. Because the PSP is an external system that does not participate in your transaction. You can
   make your own writes atomic, but "money left the PSP" and "row written in our DB" cannot commit
   together.
2. It did not know that the PSP step had already happened. The only memory of progress lived in the
   process that died. Durable execution's whole purpose is to move that memory out of the process.
3. Durable progress, resume, no lost step / no silent repeat of a finished step, visibility.
4. Because when a call's outcome is unknown (request sent, no response) the engine can only retry or
   give up; retrying may repeat the effect. "Exactly once" can only be approximated by at-least-once
   delivery plus idempotent steps.
</details>

## If you got stuck

- The tests pass but you cannot see why they are "about" failure: the `World` object is the
  stand-in for the outside world (PSP and ledger). The `IllegalStateException` is the stand-in for
  a process dying. Nothing else is happening.
- You think "just wrap it in try/catch and roll back": rollback of a PSP refund is another PSP
  call, which can itself fail. That is the saga problem (module 1.3). For now notice that *any*
  fix needs memory that survives the process.
