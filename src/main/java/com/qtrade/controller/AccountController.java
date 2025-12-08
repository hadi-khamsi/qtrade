package com.qtrade.controller;

import com.qtrade.dto.*;
import com.qtrade.entity.broker.Account;
import com.qtrade.service.broker.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request.getUsername(), request.getInitialBalance());
        AccountResponse response = new AccountResponse(account.getId(), account.getUsername(), account.getBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts().stream()
                .map(acc -> new AccountResponse(acc.getId(), acc.getUsername(), acc.getBalance()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        AccountResponse response = new AccountResponse(account.getId(), account.getUsername(), account.getBalance());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> depositFunds(@PathVariable Long id, @RequestBody DepositRequest request) {
        Account account = accountService.depositFunds(id, request.getAmount());
        AccountResponse response = new AccountResponse(account.getId(), account.getUsername(), account.getBalance());
        return ResponseEntity.ok(response);
    }
}