package com.posadskiy.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumer {

    @KafkaListener(topics = "quickstart-events")
    public void processMessage(String message) {
        log.info(message);
    }
}
