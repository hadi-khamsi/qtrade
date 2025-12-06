package com.qtrade.dto;

public class CreateAccountRequest {
    private String username;
    private double initialBalance;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public double getInitialBalance() { return initialBalance; }
    public void setInitialBalance(double initialBalance) { this.initialBalance = initialBalance; }
}