package com.qtrade.service;

import com.qtrade.entity.Account;
import com.qtrade.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(String username, double initialBalance) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        Account account = new Account(username, initialBalance);
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));
    }

    @Transactional
    public Account updateBalance(Account account, double newBalance) {
        if (newBalance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    @Transactional
    public Account depositFunds(Long accountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }
}