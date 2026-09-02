package com.bankflow.notification;

import org.springframework.stereotype.Component;

import com.bankflow.notification.event.TransferCompletedEvent;
import com.bankflow.notification.processed.ProcessedEvent;
import com.bankflow.notification.processed.ProcessedEventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;

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
        groupId = "notification-service"
    )
    public void consume(TransferCompletedEvent event) {

        log.info("Received event from Kafka: eventId={}", event.getEventId());

        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        notificationService.sendTransferNotification(event);

        processedEventRepository.save(
            new ProcessedEvent(event.getEventId())
        );
    }


}
