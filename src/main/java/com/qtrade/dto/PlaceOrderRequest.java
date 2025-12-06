package com.qtrade.dto;

public class PlaceOrderRequest {
    private Long accountId;
    private Long stockId;
    private int quantity;
    private String type;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public Long getStockId() { return stockId; }
    public void setStockId(Long stockId) { this.stockId = stockId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}