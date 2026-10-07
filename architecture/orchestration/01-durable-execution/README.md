# Module 1.1 — The durable execution model (20 h)

This is the full course for module 1.1. The short topic list in the [main README](../README.md#11-the-durable-execution-model--20-h)
says *what* to know; this folder is *how you learn it*: ten sessions of about two hours, each with
explanation, a worked example, a lab you run, exercises and checkpoint questions.

Do not read it like an article. A session is finished when the lab is green, the exercises are
done and you can answer the checkpoint questions **without looking**.

## Why this takes 20 hours and not 20 minutes

You can *read* the whole idea in ten minutes: "the engine stores a history, your code is replayed
from it". Reading it does not give you the thing employers test for. They test whether you can look
at a broken workflow and say *why* it broke. That skill comes from seeing the history of a real run,
killing a real worker, replaying a real history against wrong code, and being surprised at least
once. The labs are built to surprise you in places where intuition is wrong.

## Sessions

| # | Session | Hours | Lab |
|---|---|---|---|
| 1 | [The problem: why a plain refund breaks](S01-the-problem.md) | 2 | Lab 1 (test) |
| 2 | [Workflow vs activity — the split and why it exists](S02-workflow-vs-activity.md) | 2 | run `RefundWorkflow` on the dev server |
| 3 | [Event history — the source of truth](S03-event-history.md) | 2 | Lab 2a (test) |
| 4 | [Replay and determinism](S04-replay-and-determinism.md) | 2 | Lab 2b–g (test) |
| 5 | [Workers, task queues, polling — and a crash](S05-workers-and-the-crash-lab.md) | 2 | Lab 3 (script), Lab 4 (test) |
| 6 | [Activity failure, timeouts, idempotency](S06-activity-failure-and-idempotency.md) | 2 | Lab 8 (test) |
| 7 | [Durable timers and long waits](S07-timers.md) | 2 | Lab 5 (test) |
| 8 | [Signals, queries, updates](S08-signals-queries-updates.md) | 2 | Lab 6 (test) |
| 9 | [Child workflows and continue-as-new](S09-child-workflows-continue-as-new.md) | 2 | Lab 7 (test) |
| 10 | [Capstone: build, explain, defend](S10-capstone.md) | 2 | extend `RefundWorkflow` |

A session is one 2-hour block of the 8 h/week plan, so the module is about two and a half weeks
of calendar time. Do the sessions in order — each uses the previous one.

## Setup (once, 30 minutes counted inside session 2)

- Java 25 (the repository enforces it), Maven 3.9+.
- Temporal CLI: `brew install temporal` or <https://docs.temporal.io/cli>.
- Check: `temporal --version` and, from this repository, `cd architecture/orchestration/payment-disputes && mvn -q test`.
  Everything is green before you start. If not, fix that first.

## The labs

All test labs live in
[`payment-disputes/src/test/java/com/posadskiy/orchestration/payment/lab11/`](../payment-disputes/src/test/java/com/posadskiy/orchestration/payment/lab11/)
and need no server (they use Temporal's in-process test server). Run one with:

```bash
cd architecture/orchestration/payment-disputes
mvn -q test -Dtest=Lab02HistoryAndReplayTest
```

Lab 3 uses a real dev server and real processes — see
[scripts/crash-lab.sh](scripts/crash-lab.sh).

**How to use a lab.** First read the test and *predict* what it will print. Write the prediction in
your notes. Run it. If you were wrong, that is the valuable moment: find out why before moving on.
Then change the lab (every session lists "break it" exercises) and predict again.

## Rules of the course

1. Every claim in these sessions was checked by running the code in this repository (Temporal Java
   SDK 1.35.0, dev server from the Temporal CLI). Where something is documented but was **not**
   checked here, the text says so.
2. Examples use refunds and chargebacks because that is the domain of the track. Every idea is
   engine-neutral in its shape; the "other engines" boxes say where.
3. Keep `learning-log.md` current: one line per session, and always write down *what surprised you*.

## What you can do after the module

The final check from the main README, unchanged:

- Draw a workflow's event history for a refund that crashed after "reserve funds" and explain line
  by line what replay does.
- Say which code may run twice (activities) and which must give the same result each time
  (workflow).
- Choose signal vs query vs update for "customer adds evidence", "show dispute status", "approve and
  get the new state back".

Session 10 turns each of these into a timed exercise.
