package com.bankflow.outbox;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.bankflow.transfer.TransferCompletedEvent;
import com.bankflow.transfer.TransferEventProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final TransferEventProducer transferEventProducer;
    private final ObjectMapper objectMapper;

    private static final Logger log =
        LoggerFactory.getLogger(OutboxPublisher.class);

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

            log.info(
                "Publishing outbox event: outboxId={}",
                event.getId()
            );
            
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

                log.info(
                    "Outbox event published successfully: outboxId={}",
                    event.getId()
                );             

            } catch (Exception e) {
                // System.out.println(
                //     "Failed to publish outbox event "
                //     + event.getId()
                // );             
                log.error(
                    "Failed to publish outbox event: outboxId={}",
                    event.getId(),
                    e
                );
    }


        }
    }     


}