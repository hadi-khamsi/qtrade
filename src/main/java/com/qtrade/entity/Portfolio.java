package com.qtrade.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double averageCost;

    public Portfolio() {}

    public Portfolio(Account account, Stock stock, int quantity, double averageCost) {
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
        this.averageCost = averageCost;
    }

    public Long getId() { return id; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getAverageCost() { return averageCost; }
    public void setAverageCost(double averageCost) { this.averageCost = averageCost; }
}