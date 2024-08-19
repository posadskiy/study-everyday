package com.posadskiy.design.pattern.behavioural.observer;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PrintingToLogsListener implements Listener {
    @Override
    public void update(Event event) {
        log.info(event);
    }
}
