package com.bankflow.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bankflow.transfer.TransferCompletedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransferEventConsumer {

    private final NotificationService notificationService;
    private final ProcessedEventRepository processedEventRepository;

    private static final Logger log =
        LoggerFactory.getLogger(TransferEventConsumer.class);

    public TransferEventConsumer(
        NotificationService notificationService,
        ProcessedEventRepository processedEventRepository) {

        this.notificationService = notificationService;
        this.processedEventRepository = processedEventRepository;
    }

    @KafkaListener(
        topics = "transfer-events",
        groupId = "bankflow"
    )
    public void handleTransferCompleted(
        TransferCompletedEvent event
    ) {

        // 1. Check whether we already processed this event
        if (processedEventRepository.existsById(event.getEventId())) {

            log.warn("Skipping already processed event: eventId={}", event.getEventId());

            return;
        }

        log.info("Processing transfer event: eventId={}", event.getEventId());

        // 2. Process it
        notificationService.sendTransferNotification(event);

        // 3. Remember that we processed it
        processedEventRepository.save(
            new ProcessedEvent(event.getEventId())
        );
    }

}