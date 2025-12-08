package com.qtrade.entity.broker;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double executionPrice;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    public Order() {
        this.timestamp = LocalDateTime.now();
    }

    public Order(String type, int quantity, double executionPrice, Account account, Stock stock) {
        this.type = type;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.account = account;
        this.stock = stock;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getExecutionPrice() { return executionPrice; }
    public void setExecutionPrice(double executionPrice) { this.executionPrice = executionPrice; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
}