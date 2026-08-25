package com.bankflow;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;

@Service
public class TransferEventProducer {

    private final KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate;

    public TransferEventProducer(
        KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate) {

            this.kafkaTemplate = kafkaTemplate;
        }

    public CompletableFuture<SendResult<String, TransferCompletedEvent>> publish(TransferCompletedEvent event) {
        return kafkaTemplate.send(
            "transfer-events",
            event
        );
    }

}