#!/usr/bin/env bash
# Lab 3 driver: kill a worker in the middle of an activity and watch another worker take over.
# Needs: temporal CLI on PATH and Java 25. Run from the repository root; it builds the classpath itself.
set -euo pipefail
MOD=architecture/orchestration/payment-disputes
(cd "$MOD" && mvn -q compile dependency:build-classpath -Dmdep.outputFile=target/classpath.txt >/dev/null)
CP="$MOD/target/classes:$(cat "$MOD/target/classpath.txt")"
worker() { exec java -cp "$CP" com.posadskiy.orchestration.payment.Application --server.port=0 "$@"; }
WF_ID="refund-crash-lab-$(date +%s)"
LOG=/tmp/crash-lab; mkdir -p "$LOG"

temporal server start-dev --headless >"$LOG/server.log" 2>&1 &
SERVER=$!
trap 'kill $SERVER 2>/dev/null || true' EXIT
until temporal operator cluster health >/dev/null 2>&1; do sleep 1; done

worker --refund.psp-delay=PT25S >"$LOG/worker1.log" 2>&1 &
W1=$!
sleep 12
temporal workflow start --type RefundWorkflow --task-queue refund-task-queue \
  --workflow-id "$WF_ID" \
  --input '{"refundId":"r-1","paymentId":"p-1","amountMinor":1999,"currency":"EUR"}'
until grep -q "submitToPsp" "$LOG/worker1.log"; do sleep 1; done
echo ">>> worker 1 is inside submitToPsp. Killing it with SIGKILL."
kill -9 $W1
START=$(date +%s)

worker --refund.psp-delay=PT1S >"$LOG/worker2.log" 2>&1 &
W2=$!
trap 'kill $SERVER $W2 2>/dev/null || true' EXIT
temporal workflow show --workflow-id "$WF_ID" --follow >"$LOG/show.log" 2>&1 || true
echo ">>> finished after $(( $(date +%s) - START )) s"
echo "--- worker 1 (killed) ---"; grep -E "validate|reserveFunds|submitToPsp|postLedger|notifyCustomer" "$LOG/worker1.log" | cut -c1-200
echo "--- worker 2 ---";          grep -E "validate|reserveFunds|submitToPsp|postLedger|notifyCustomer" "$LOG/worker2.log" | cut -c1-200
echo "--- history ---"; cat "$LOG/show.log"
