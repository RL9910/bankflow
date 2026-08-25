package com.bankflow;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferEventConsumer {

    private final NotificationService notificationService;


    public TransferEventConsumer(NotificationService notificationService) {

        this.notificationService = notificationService;

    }

    @KafkaListener(
        topics = "transfer-events",
        groupId = "bankflow"
    )
    public void handleTransferCompleted(
        TransferCompletedEvent event
    ) {
        notificationService.sendTransferNotification(event);
    }

}