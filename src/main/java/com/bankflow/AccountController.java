package com.bankflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id) {

        return accountService.getAccount(id);
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody CreateAccountRequest request) {

        String ownerName = request.getOwnerName();

        return accountService.createAccount(ownerName);

    }

    @PostMapping("/accounts/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestBody AmountRequest amount) {

        return accountService.deposit(id, amount.getAmount());

    }

    @PostMapping("/accounts/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestBody AmountRequest amount) {

        return accountService.withdraw(id, amount.getAmount());

    }


}