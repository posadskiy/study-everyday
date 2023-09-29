package com.posadskiy.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(topics = "quickstart-events", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class MessageProducerTest {
    
    @Autowired
    private MessageProducer messageProducer;

    @Test
    void sendMessage() {
        messageProducer.sendMessage("test message");
    }
}
