package com.posadskiy.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(topics = "quickstart-events", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class MessageConsumerTest {
    
    @Autowired
    private MessageConsumer messageConsumer;
    
    @Test
    public void consumeMessage() {
        messageConsumer.processMessage("test message");
    }

}
