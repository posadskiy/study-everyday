#!/usr/bin/env bash
# Lab 3 driver (module 1.1, session 5): kill a worker in the middle of an activity and watch a
# second worker take over.
#
# Needs: temporal CLI, Java 25 and Maven on PATH; port 7233 free (no other dev server running).
# Run from anywhere; takes about 100 s (the first run also builds the classpath).
# Environment: PSP_DELAY (default PT25S) - how long worker 1 sits inside the PSP call;
#              WAIT_LIMIT (default 120) - seconds to wait for the refund to finish.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../../.." && pwd)"
MOD="$ROOT/architecture/orchestration/payment-disputes"
LOG="${LOG_DIR:-/tmp/crash-lab}"
PSP_DELAY="${PSP_DELAY:-PT25S}"
WAIT_LIMIT="${WAIT_LIMIT:-120}"
WF_ID="refund-crash-lab-$(date +%s)"
APP=com.posadskiy.orchestration.payment.Application
PIDS=()

for tool in temporal java mvn; do
  command -v "$tool" >/dev/null || { echo "missing tool: $tool" >&2; exit 1; }
done
if (exec 3<>/dev/tcp/127.0.0.1/7233) 2>/dev/null; then
  echo "port 7233 is already in use - stop the running Temporal server first" >&2
  exit 1
fi

cleanup() {
  for pid in "${PIDS[@]:-}"; do
    [ -n "$pid" ] && kill "$pid" 2>/dev/null || true
  done
}
trap cleanup EXIT

mkdir -p "$LOG"
rm -f "$LOG"/*.log

echo ">>> building classpath"
(cd "$MOD" && mvn -q compile dependency:build-classpath -Dmdep.outputFile=target/classpath.txt >"$LOG/mvn.log" 2>&1) \
  || { echo "build failed, see $LOG/mvn.log" >&2; exit 1; }
CP="$MOD/target/classes:$(cat "$MOD/target/classpath.txt")"

# exec so that $! is the Java process itself; kill -9 must hit the JVM, not a wrapper shell.
start_worker() { # name delay
  (exec java -cp "$CP" "$APP" --server.port=0 "--refund.psp-delay=$2" >"$LOG/$1.log" 2>&1) &
  PIDS+=("$!")
  LAST_PID=$!
}

wait_for_log() { # file pattern seconds
  local deadline=$((SECONDS + $3))
  until grep -q "$2" "$1" 2>/dev/null; do
    [ $SECONDS -ge $deadline ] && { echo "timed out waiting for '$2' in $1" >&2; exit 1; }
    sleep 1
  done
}

echo ">>> starting dev server"
temporal server start-dev --headless >"$LOG/server.log" 2>&1 &
PIDS+=("$!")
until temporal operator cluster health >/dev/null 2>&1; do sleep 1; done

echo ">>> starting worker 1 (PSP delay $PSP_DELAY)"
start_worker worker1 "$PSP_DELAY"; W1=$LAST_PID
wait_for_log "$LOG/worker1.log" "Activity Poller" 90

echo ">>> starting refund $WF_ID"
temporal workflow start --type RefundWorkflow --task-queue refund-task-queue \
  --workflow-id "$WF_ID" \
  --input '{"refundId":"r-1","paymentId":"p-1","amountMinor":1999,"currency":"EUR"}' >/dev/null
wait_for_log "$LOG/worker1.log" "submitToPsp" 30

echo ">>> worker 1 is inside submitToPsp - killing it with SIGKILL"
kill -9 "$W1"
START=$SECONDS

echo ">>> starting worker 2 (PSP delay PT1S); the refund must now finish by itself"
start_worker worker2 PT1S
timeout "$WAIT_LIMIT" temporal workflow show --workflow-id "$WF_ID" --follow >"$LOG/history.log" 2>&1 \
  || { echo "refund did not finish within ${WAIT_LIMIT}s; see $LOG" >&2; exit 1; }
echo ">>> finished after $((SECONDS - START)) s"

show() { grep -E "validate|reserveFunds|submitToPsp|postLedger|notifyCustomer" "$1" | cut -c1-200 || true; }
echo; echo "--- worker 1 (killed) ---"; show "$LOG/worker1.log"
echo; echo "--- worker 2 ---";          show "$LOG/worker2.log"
echo; echo "--- history ---";           cat "$LOG/history.log"
echo
echo "submitToPsp calls: worker1=$(grep -c submitToPsp "$LOG/worker1.log" || true)" \
     "worker2=$(grep -c submitToPsp "$LOG/worker2.log" || true)  (expect 1 + 1: at-least-once)"
echo "logs kept in $LOG"
