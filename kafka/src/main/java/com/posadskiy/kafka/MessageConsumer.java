package com.posadskiy.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MessageConsumer {

    @KafkaListener(topics = "quickstart-events")
    public void processMessage(String message) {
        log.info(message);
    }
}
