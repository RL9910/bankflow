package com.bankflow.outbox;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.bankflow.transfer.TransferCompletedEvent;
import com.bankflow.transfer.TransferEventProducer;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final TransferEventProducer transferEventProducer;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
            OutboxEventRepository outboxEventRepository,
            TransferEventProducer transferEventProducer,
            ObjectMapper objectMapper) {

        this.outboxEventRepository = outboxEventRepository;
        this.transferEventProducer = transferEventProducer;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
            outboxEventRepository.findByPublishedFalse();

        for (OutboxEvent event : events) {
            
            try {

                TransferCompletedEvent transferEvent =
                    objectMapper.readValue(
                        event.getPayload(),
                        TransferCompletedEvent.class
                    );

                transferEventProducer
                    .publish(transferEvent)
                    .get();

                event.markPublished();

                outboxEventRepository.save(event);                

            } catch (Exception e) {
                System.out.println(
                    "Failed to publish outbox event "
                    + event.getId()
                );             
            }


        }
    }     


}