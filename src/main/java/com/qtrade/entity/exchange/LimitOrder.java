package com.qtrade.entity.exchange;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "limit_orders")
public class LimitOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String side; // "BUY" or "SELL"

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String status; // "OPEN", "FILLED", "PARTIALLY_FILLED", "CANCELLED"

    @Column(nullable = false)
    private int remainingQuantity;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long accountId; // Reference to broker account (not FK, different DB)

    public LimitOrder() {
        this.timestamp = LocalDateTime.now();
        this.status = "OPEN";
    }

    public LimitOrder(String ticker, String side, int quantity, double price, Long accountId) {
        this.ticker = ticker.toUpperCase();
        this.side = side.toUpperCase();
        this.quantity = quantity;
        this.price = price;
        this.remainingQuantity = quantity;
        this.accountId = accountId;
        this.timestamp = LocalDateTime.now();
        this.status = "OPEN";
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(int remainingQuantity) { this.remainingQuantity = remainingQuantity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
}