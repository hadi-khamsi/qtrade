package com.qtrade.dto;

public class PortfolioResponse {
    private Long id;
    private String stockTicker;
    private String stockName;
    private int quantity;
    private double averageCost;
    private double currentPrice;
    private double marketValue;
    private double totalCost;
    private double profitLoss;
    private double profitLossPercent;

    public PortfolioResponse(Long id, String stockTicker, String stockName, int quantity,
                             double averageCost, double currentPrice) {
        this.id = id;
        this.stockTicker = stockTicker;
        this.stockName = stockName;
        this.quantity = quantity;
        this.averageCost = averageCost;
        this.currentPrice = currentPrice;
        this.marketValue = currentPrice * quantity;
        this.totalCost = averageCost * quantity;
        this.profitLoss = marketValue - totalCost;
        this.profitLossPercent = totalCost > 0 ? (profitLoss / totalCost) * 100 : 0;
    }

    public Long getId() { return id; }
    public String getStockTicker() { return stockTicker; }
    public String getStockName() { return stockName; }
    public int getQuantity() { return quantity; }
    public double getAverageCost() { return averageCost; }
    public double getCurrentPrice() { return currentPrice; }
    public double getMarketValue() { return marketValue; }
    public double getTotalCost() { return totalCost; }
    public double getProfitLoss() { return profitLoss; }
    public double getProfitLossPercent() { return profitLossPercent; }
}