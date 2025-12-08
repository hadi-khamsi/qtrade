package com.qtrade.entity.exchange;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long buyOrderId;

    @Column(nullable = false)
    private Long sellOrderId;

    @Column(nullable = false)
    private Long buyerAccountId;

    @Column(nullable = false)
    private Long sellerAccountId;

    public Trade() {
        this.timestamp = LocalDateTime.now();
    }

    public Trade(String ticker, int quantity, double price, Long buyOrderId,
                 Long sellOrderId, Long buyerAccountId, Long sellerAccountId) {
        this.ticker = ticker.toUpperCase();
        this.quantity = quantity;
        this.price = price;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyerAccountId = buyerAccountId;
        this.sellerAccountId = sellerAccountId;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Long getBuyOrderId() { return buyOrderId; }
    public void setBuyOrderId(Long buyOrderId) { this.buyOrderId = buyOrderId; }
    public Long getSellOrderId() { return sellOrderId; }
    public void setSellOrderId(Long sellOrderId) { this.sellOrderId = sellOrderId; }
    public Long getBuyerAccountId() { return buyerAccountId; }
    public void setBuyerAccountId(Long buyerAccountId) { this.buyerAccountId = buyerAccountId; }
    public Long getSellerAccountId() { return sellerAccountId; }
    public void setSellerAccountId(Long sellerAccountId) { this.sellerAccountId = sellerAccountId; }
}