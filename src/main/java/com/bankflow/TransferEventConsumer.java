package com.bankflow;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferEventConsumer {

    private final NotificationService notificationService;
    private final ProcessedEventRepository processedEventRepository;

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

            System.out.println(
                "Duplicate event ignored: "
                + event.getEventId()
            );

            return;
        }

        System.out.println(
            "Processing event: " + event.getEventId()
        );

        // 2. Process it
        notificationService.sendTransferNotification(event);

        // 3. Remember that we processed it
        processedEventRepository.save(
            new ProcessedEvent(event.getEventId())
        );
    }

}