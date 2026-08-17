package com.bankflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    public AccountController(AccountService accountService, AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id, Authentication authentication) {

        String email = authentication.getName();

        User user = authService.getUserByEmail(email);

        return accountService.getAccountForUser(id, user);
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody CreateAccountRequest request, Authentication authentication) {

        // String ownerName = request.getOwnerName();

        // return accountService.createAccount(ownerName);

        String email = authentication.getName();

        User user = authService.getUserByEmail(email);

        return accountService.createAccount(
            request.getOwnerName(),
            user
        );

    }

    @PostMapping("/accounts/{id}/deposit")
    public Account deposit(
            @PathVariable Long id,
            @Valid @RequestBody AmountRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = authService.getUserByEmail(email);

        return accountService.depositForUser(
            id,
            request.getAmount(),
            user
        );
    }

    @PostMapping("/accounts/{id}/withdraw")
    public Account withdraw(
            @PathVariable Long id,
            @Valid @RequestBody AmountRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = authService.getUserByEmail(email);

        return accountService.withdrawForUser(
            id,
            request.getAmount(),
            user
        );
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionRecord> getTransactions(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = authService.getUserByEmail(email);

        return accountService.getTransactionsForUser(id, user);
    }


}