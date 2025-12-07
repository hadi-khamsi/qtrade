package com.qtrade.service;

import com.qtrade.dto.FactorBreakdown;
import com.qtrade.dto.ProbabilisticRangeResponse;
import com.qtrade.dto.StockDataResponse;
import org.springframework.stereotype.Service;

@Service
public class ProbabilisticPriceService {

    private final YahooFinanceService yahooFinanceService;

    // Reference volume for highly liquid large-cap stocks (e.g., AAPL, MSFT)
    private static final double VOLUME_REFERENCE_THRESHOLD = 10_000_000.0;

    // Maximum contribution of HFT activity to uncertainty (20% additional)
    private static final double HFT_SCALING_FACTOR = 0.2;

    // Amplifies tiny bid-ask spreads to meaningful multipliers
    private static final double SPREAD_SCALING_FACTOR = 1000.0;

    // Converts daily price change percentage to uncertainty multiplier
    private static final double MOMENTUM_SCALING_FACTOR = 100.0;

    // Industry standard baseline uncertainty for liquid markets
    private static final double BASE_UNCERTAINTY_PERCENT = 0.01;

    // Maximum allowed uncertainty to prevent unrealistic ranges
    private static final double MAX_UNCERTAINTY_PERCENT = 0.03;

    // Simulates bid price at 0.02% below current (typical for liquid stocks)
    private static final double SIMULATED_BID_MULTIPLIER = 0.9998;

    // Simulates ask price at 0.02% above current (typical for liquid stocks)
    private static final double SIMULATED_ASK_MULTIPLIER = 1.0002;

    public ProbabilisticPriceService(YahooFinanceService yahooFinanceService) {
        this.yahooFinanceService = yahooFinanceService;
    }

    public ProbabilisticRangeResponse calculateProbabilisticRange(String ticker) {
        StockDataResponse stockData = yahooFinanceService.fetchStockData(ticker);
        double currentPrice = stockData.getCurrentPrice();

        // Simulate bid/ask prices with typical 0.04% spread for liquid stocks
        // Yahoo Finance does not consistently provide real-time bid/ask in their free API
        double bidPrice = currentPrice * SIMULATED_BID_MULTIPLIER;
        double askPrice = currentPrice * SIMULATED_ASK_MULTIPLIER;

        // FACTOR 1: HFT/AI Trading Activity
        // Uses trading volume as a proxy for algorithmic trading intensity
        // Higher volume indicates more HFT/bot activity, which increases price uncertainty
        double volumeMultiplier = Math.min(stockData.getVolume() / VOLUME_REFERENCE_THRESHOLD, 1.0);
        // 10M shares is the reference point for highly liquid large-cap stocks
        // Division normalizes volume: 5M shares = 0.5, 10M shares = 1.0, 50M shares = capped at 1.0
        // The cap prevents extremely high-volume stocks from dominating the calculation

        double hftMultiplier = 1.0 + (volumeMultiplier * HFT_SCALING_FACTOR);
        // Scaling factor of 0.2 means volume can contribute up to 20% additional uncertainty
        // Examples: 5M volume → 1.1×, 10M volume → 1.2×, 50M volume → 1.2× (capped)

        // FACTOR 2: Bid-Ask Spread
        // Measures market disagreement on the "true" price
        // Wider spreads indicate greater uncertainty about fair value
        double spreadPercent = (askPrice - bidPrice) / ((askPrice + bidPrice) / 2);
        // Calculate spread as percentage of midpoint price
        // For our simulated 0.04% spread on $245 stock: ($245.049 - $244.951) / $245 = 0.0004

        double spreadMultiplier = 1.0 + (spreadPercent * SPREAD_SCALING_FACTOR);
        // Scaling factor of 1000 converts tiny decimal spreads into meaningful multipliers
        // Without scaling: 0.0004 would barely affect uncertainty
        // With scaling: 0.0004 × 1000 = 0.4, giving a 1.4× multiplier
        // Typical spreads: 0.01% → 1.1×, 0.04% → 1.4×, 0.1% → 2.0×

        // FACTOR 3: Heisenberg Uncertainty (Price Momentum)
        // Faster price movement creates greater uncertainty in exact price determination
        // Analogous to Heisenberg's principle: high momentum → low position certainty
        double priceChangePercent = Math.abs(stockData.getChangePercent());
        // Uses daily price change as momentum indicator
        // Absolute value because direction doesn't matter, only magnitude of movement

        double momentumMultiplier = 1.0 + (priceChangePercent / MOMENTUM_SCALING_FACTOR);
        // Scaling factor of 100 converts percentage to multiplier
        // Examples: 0.5% change → 1.005×, 2% change → 1.02×, 5% change → 1.05×

        // ROOT MEAN SQUARE COMBINATION
        // Combines factors using RMS to prevent any single factor from dominating
        // Mathematical technique from physics for combining independent uncertainties
        double hftExcess = hftMultiplier - 1.0;
        double spreadExcess = spreadMultiplier - 1.0;
        double momentumExcess = momentumMultiplier - 1.0;
        // Extract the "excess" contribution above baseline 1.0 for each factor

        double rmsValue = Math.sqrt(
                (Math.pow(hftExcess, 2) + Math.pow(spreadExcess, 2) + Math.pow(momentumExcess, 2)) / 3.0
        );
        // RMS formula: sqrt(average of squared values)
        // Prevents multiplicative explosion while giving all factors meaningful weight

        double combinedMultiplier = 1.0 + rmsValue;
        // Add RMS value back to baseline of 1.0

        // BASE UNCERTAINTY
        // 1% represents typical short-term price uncertainty in liquid markets
        // Industry standard for intraday volatility estimation
        double baseUncertainty = currentPrice * BASE_UNCERTAINTY_PERCENT;

        double totalUncertainty = baseUncertainty * combinedMultiplier;

        // MAXIMUM UNCERTAINTY CAP
        // 3% cap prevents unrealistic ranges even under extreme market conditions
        // Ensures ranges remain within bounds of realistic execution scenarios
        double maxUncertainty = currentPrice * MAX_UNCERTAINTY_PERCENT;
        totalUncertainty = Math.min(totalUncertainty, maxUncertainty);

        double lowerBound = currentPrice - totalUncertainty;
        double upperBound = currentPrice + totalUncertainty;
        double uncertaintyPercent = (totalUncertainty / currentPrice) * 100;

        FactorBreakdown factors = new FactorBreakdown(
                hftMultiplier, spreadMultiplier, momentumMultiplier, combinedMultiplier
        );

        return new ProbabilisticRangeResponse(
                ticker.toUpperCase(), currentPrice, lowerBound, upperBound, uncertaintyPercent, factors
        );
    }
}