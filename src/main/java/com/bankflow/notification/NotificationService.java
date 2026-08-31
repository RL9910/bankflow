package com.bankflow.notification;

import org.springframework.stereotype.Service;

import com.bankflow.transfer.TransferCompletedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger log =
        LoggerFactory.getLogger(NotificationService.class);

    public void sendTransferNotification(TransferCompletedEvent event) {

        // System.out.println(
        //     "Notification: transfer from account "
        //     + event.getFromAccountId()
        //     + " to account "
        //     + event.getToAccountId()
        //     + " for "
        //     + event.getAmount()
        // );

        log.info(
            "Transfer notification: fromAccount={}, toAccount={}, amount={}",
            event.getFromAccountId(),
            event.getToAccountId(),
            event.getAmount()
        );

    }

}