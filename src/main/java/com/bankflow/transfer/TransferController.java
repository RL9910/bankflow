package com.bankflow.transfer;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.Authentication;


import com.bankflow.auth.AuthService;
import com.bankflow.user.User;


@RestController
public class TransferController {

    private final TransferService transferService;
    private final AuthService authService;

    public TransferController(TransferService transferService, AuthService authService) {
        this.transferService = transferService;
        this.authService = authService;
    }

    @PostMapping("/transfers")
    public void transfer(@RequestBody TransferRequest transferRequest, Authentication authentication) {

        String email = authentication.getName();
        User user = authService.getUserByEmail(email);


        transferService.transfer(
            transferRequest.getFromAccountId(),
            transferRequest.getToAccountId(),
            transferRequest.getAmount(),
            user
        );
    }

}