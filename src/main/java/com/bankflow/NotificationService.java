package com.bankflow;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendTransferNotification(TransferCompletedEvent event) {

        System.out.println(
            "Notification: transfer from account "
            + event.getFromAccountId()
            + " to account "
            + event.getToAccountId()
            + " for "
            + event.getAmount()
        );

    }

}