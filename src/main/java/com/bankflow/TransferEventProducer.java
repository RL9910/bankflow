package com.bankflow;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferEventProducer {

    private final KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate;

    public TransferEventProducer(
        KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate) {

            this.kafkaTemplate = kafkaTemplate;
        }

    public void publish(TransferCompletedEvent event) {
        kafkaTemplate.send(
            "transfer-events",
            event
        );
    }

}