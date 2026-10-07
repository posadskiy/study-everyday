package com.posadskiy.orchestration.payment.lab11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RecordingStepActivities implements StepActivities {

    public final List<String> calls = new CopyOnWriteArrayList<>();

    @Override
    public void step(String name) {
        calls.add(name);
    }

    @Override
    public void audit(String name) {
        calls.add("audit:" + name);
    }
}
