package com.qtrade.dto;

public class StockDataResponse {
    private String ticker;
    private String name;
    private double currentPrice;
    private double previousClose;
    private double changePercent;
    private long volume;

    public StockDataResponse(String ticker, String name, double currentPrice,
                             double previousClose, double changePercent, long volume) {
        this.ticker = ticker;
        this.name = name;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.changePercent = changePercent;
        this.volume = volume;
    }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public double getPreviousClose() { return previousClose; }
    public void setPreviousClose(double previousClose) { this.previousClose = previousClose; }
    public double getChangePercent() { return changePercent; }
    public void setChangePercent(double changePercent) { this.changePercent = changePercent; }
    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }
}