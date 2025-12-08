package com.qtrade.service.broker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtrade.dto.StockDataResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YahooFinanceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final long API_DELAY_MS = 2000;
    private static final String YAHOO_FINANCE_CHART_URL =
            "https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=1d";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    public YahooFinanceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public StockDataResponse fetchStockData(String ticker) {
        String upperTicker = ticker.toUpperCase();

        try {
            // Rate limiting delay to prevent HTTP 429 errors from Yahoo Finance
            Thread.sleep(API_DELAY_MS);

            String url = String.format(YAHOO_FINANCE_CHART_URL, upperTicker);

            // User-Agent header required to avoid request blocking
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            // Parse Yahoo Finance JSON response structure
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode meta = result.path("meta");
            JsonNode quote = result.path("indicators").path("quote").get(0);

            // Extract stock metadata
            String symbol = meta.path("symbol").asText();
            String name = meta.path("longName").asText(symbol);
            double currentPrice = meta.path("regularMarketPrice").asDouble();
            double previousClose = meta.path("chartPreviousClose").asDouble();
            double changePercent = ((currentPrice - previousClose) / previousClose) * 100;

            // Extract volume data
            // Yahoo returns volume as first element in array for current day
            long volume = 0;
            JsonNode volumeNode = quote.path("volume");
            if (volumeNode.isArray() && volumeNode.size() > 0) {
                volume = volumeNode.get(0).asLong();
            }

            System.out.println("[API CALL] Fetched data for " + upperTicker + " at $" + currentPrice);

            return new StockDataResponse(symbol, name, currentPrice, previousClose, changePercent, volume);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrupted");
        } catch (Exception e) {
            System.err.println("Error fetching " + upperTicker + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch stock data for: " + upperTicker + " - " + e.getMessage());
        }
    }

    public double fetchCurrentPrice(String ticker) {
        return fetchStockData(ticker).getCurrentPrice();
    }
}