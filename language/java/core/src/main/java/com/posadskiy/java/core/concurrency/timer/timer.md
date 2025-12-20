## java.util.Timer

#### schedule(task, delay, period)

If an execution is delayed for any reason (such as garbage collection or other background activity), subsequent
executions will be delayed as well. In the long run, the frequency of execution will generally be slightly lower than
the reciprocal of the specified period

#### scheduleAtFixedRate(task, delay, period)

If an execution is delayed for any reason (such as garbage collection or other background activity), two or more
executions will occur in rapid succession to "catch up." In the long run, the frequency of execution will be exactly the
reciprocal of the specified period

#### cancelling

two options

- cancel() from inside TimerTask
- cancel() on timer outside to cancel all queued tasks
