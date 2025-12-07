package com.qtrade.dto;

public class FactorBreakdown {
    private final double hftMultiplier;
    private final double spreadMultiplier;
    private final double momentumMultiplier;
    private final double combinedMultiplier;

    public FactorBreakdown(double hftMultiplier, double spreadMultiplier,
                           double momentumMultiplier, double combinedMultiplier) {
        this.hftMultiplier = hftMultiplier;
        this.spreadMultiplier = spreadMultiplier;
        this.momentumMultiplier = momentumMultiplier;
        this.combinedMultiplier = combinedMultiplier;
    }

    public double getHftMultiplier() { return hftMultiplier; }
    public double getSpreadMultiplier() { return spreadMultiplier; }
    public double getMomentumMultiplier() { return momentumMultiplier; }
    public double getCombinedMultiplier() { return combinedMultiplier; }
}