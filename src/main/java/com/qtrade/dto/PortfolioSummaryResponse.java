package com.qtrade.dto;

import java.util.List;

public class PortfolioSummaryResponse {
    private Long accountId;
    private String username;
    private double cashBalance;
    private double totalMarketValue;
    private double totalCost;
    private double totalProfitLoss;
    private double totalProfitLossPercent;
    private double accountValue;
    private List<PortfolioResponse> holdings;

    public PortfolioSummaryResponse(Long accountId, String username, double cashBalance,
                                    List<PortfolioResponse> holdings) {
        this.accountId = accountId;
        this.username = username;
        this.cashBalance = cashBalance;
        this.holdings = holdings;

        this.totalMarketValue = holdings.stream().mapToDouble(PortfolioResponse::getMarketValue).sum();
        this.totalCost = holdings.stream().mapToDouble(PortfolioResponse::getTotalCost).sum();
        this.totalProfitLoss = holdings.stream().mapToDouble(PortfolioResponse::getProfitLoss).sum();
        this.totalProfitLossPercent = totalCost > 0 ? (totalProfitLoss / totalCost) * 100 : 0;
        this.accountValue = cashBalance + totalMarketValue;
    }

    public Long getAccountId() { return accountId; }
    public String getUsername() { return username; }
    public double getCashBalance() { return cashBalance; }
    public double getTotalMarketValue() { return totalMarketValue; }
    public double getTotalCost() { return totalCost; }
    public double getTotalProfitLoss() { return totalProfitLoss; }
    public double getTotalProfitLossPercent() { return totalProfitLossPercent; }
    public double getAccountValue() { return accountValue; }
    public List<PortfolioResponse> getHoldings() { return holdings; }
}