package com.qtrade.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtrade.dto.StockDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YahooFinanceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public YahooFinanceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public StockDataResponse fetchStockData(String ticker) {
        try {
            String url = String.format(
                    "https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=1d",
                    ticker.toUpperCase()
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode meta = result.path("meta");
            JsonNode quote = result.path("indicators").path("quote").get(0);

            String symbol = meta.path("symbol").asText();
            String name = meta.path("longName").asText(symbol);
            double currentPrice = meta.path("regularMarketPrice").asDouble();
            double previousClose = meta.path("chartPreviousClose").asDouble();
            double changePercent = ((currentPrice - previousClose) / previousClose) * 100;
            long volume = quote.path("volume").get(0).asLong();

            return new StockDataResponse(symbol, name, currentPrice, previousClose, changePercent, volume);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock data for: " + ticker + " - " + e.getMessage());
        }
    }

    public double fetchCurrentPrice(String ticker) {
        return fetchStockData(ticker).getCurrentPrice();
    }
}