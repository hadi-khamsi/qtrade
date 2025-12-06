package com.qtrade.dto;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private String type;
    private int quantity;
    private double executionPrice;
    private double totalCost;
    private LocalDateTime timestamp;
    private Long accountId;
    private String stockTicker;

    public OrderResponse(Long id, String type, int quantity, double executionPrice,
                         LocalDateTime timestamp, Long accountId, String stockTicker) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
        this.totalCost = executionPrice * quantity;
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.stockTicker = stockTicker;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getExecutionPrice() { return executionPrice; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Long getAccountId() { return accountId; }
    public String getStockTicker() { return stockTicker; }
}