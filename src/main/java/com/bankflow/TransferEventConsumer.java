package com.bankflow;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferEventConsumer {

    @KafkaListener(
        topics = "transfer-events",
        groupId = "bankflow"
    )
    public void handleTransferCompleted(
        TransferCompletedEvent event
    ) {
        System.out.println(
            "Received transfer event: "
            + event.getFromAccountId()
            + " ---> "
            + event.getToAccountId()
            + " amount: "
            + event.getAmount()
        );
    }

}