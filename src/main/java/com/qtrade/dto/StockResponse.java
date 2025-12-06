package com.qtrade.dto;

public class StockResponse {
    private Long id;
    private String ticker;
    private String name;
    private double currentPrice;

    public StockResponse(Long id, String ticker, String name, double currentPrice) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public String getName() { return name; }
    public double getCurrentPrice() { return currentPrice; }
}