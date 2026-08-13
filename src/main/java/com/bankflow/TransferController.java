package com.bankflow;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfers")
    public void transfer(@RequestBody TransferRequest transferRequest) {

        transferService.transfer(
            transferRequest.getFromAccountId(),
            transferRequest.getToAccountId(),
            transferRequest.getAmount()
        );
    }

}