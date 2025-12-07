package com.qtrade.dto;

public class ProbabilisticRangeResponse {
    private final String ticker;
    private final double referencePrice;
    private final double lowerBound;
    private final double upperBound;
    private final double totalUncertaintyPercent;
    private final FactorBreakdown factors;

    public ProbabilisticRangeResponse(String ticker, double referencePrice, double lowerBound,
                                      double upperBound, double totalUncertaintyPercent, FactorBreakdown factors) {
        this.ticker = ticker;
        this.referencePrice = referencePrice;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.totalUncertaintyPercent = totalUncertaintyPercent;
        this.factors = factors;
    }

    public String getTicker() { return ticker; }
    public double getReferencePrice() { return referencePrice; }
    public double getLowerBound() { return lowerBound; }
    public double getUpperBound() { return upperBound; }
    public double getTotalUncertaintyPercent() { return totalUncertaintyPercent; }
    public FactorBreakdown getFactors() { return factors; }
}