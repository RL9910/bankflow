package com.bankflow.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankflow.notification.event.TransferCompletedEvent;

@Service
public class NotificationService {
    
    private static final Logger log =
        LoggerFactory.getLogger(NotificationService.class);

    public void sendTransferNotification(TransferCompletedEvent event) {

        log.info(
            "Transfer notification: fromAccount={}, toAccount={}, amount={}",
            event.getFromAccountId(),
            event.getToAccountId(),
            event.getAmount()
        );
    }


}
