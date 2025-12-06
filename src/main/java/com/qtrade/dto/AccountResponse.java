package com.qtrade.dto;

public class AccountResponse {
    private Long id;
    private String username;
    private double balance;

    public AccountResponse(Long id, String username, double balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }
}
