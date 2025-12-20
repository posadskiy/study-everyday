## java.util.concurrent.ScheduledExecutorService

#### scheduleWithFixedDelay

Starts after completing the previous run with defined delay.

#### scheduleAtFixedRate

Submits a periodic action that becomes enabled first after the given initial delay, and subsequently with the given
period; that is, executions will commence after initialDelay, then initialDelay + period, then initialDelay + 2 *
period, and so on.

The next task will only start immediately after the current task finishes. Scheduler does not allow overlapping
executions of the same task

#### shutdown

- prevents any new tasks from being submitted
- sends all tasks in queue cancel(false) request
- trying to get shutdown itself

#### shutdownNow

There are no guarantees beyond best-effort attempts to stop processing actively executing tasks. For example, typical
implementations will cancel via Thread.interrupt() for all threads, so any task that fails to respond to interrupts may
never terminate.
