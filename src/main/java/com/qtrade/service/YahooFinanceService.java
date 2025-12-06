package com.qtrade.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtrade.dto.StockDataResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class YahooFinanceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, CachedStockData> cache;
    private static final int CACHE_DURATION_MINUTES = 15;

    public YahooFinanceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.cache = new HashMap<>();
    }

    public StockDataResponse fetchStockData(String ticker) {
        String upperTicker = ticker.toUpperCase();

        // Check cache first
        CachedStockData cached = cache.get(upperTicker);
        if (cached != null && cached.isValid()) {
            System.out.println("Using cached data for " + upperTicker);
            return cached.data;
        }

        // Fetch fresh data
        try {
            Thread.sleep(2000); // 2 second delay between API calls

            String url = String.format(
                    "https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=1d",
                    upperTicker
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode meta = result.path("meta");
            JsonNode quote = result.path("indicators").path("quote").get(0);

            String symbol = meta.path("symbol").asText();
            String name = meta.path("longName").asText(symbol);
            double currentPrice = meta.path("regularMarketPrice").asDouble();
            double previousClose = meta.path("chartPreviousClose").asDouble();
            double changePercent = ((currentPrice - previousClose) / previousClose) * 100;

            long volume = 0;
            JsonNode volumeNode = quote.path("volume");
            if (volumeNode.isArray() && volumeNode.size() > 0) {
                volume = volumeNode.get(0).asLong();
            }

            StockDataResponse stockData = new StockDataResponse(symbol, name, currentPrice, previousClose, changePercent, volume);

            // Cache the result
            cache.put(upperTicker, new CachedStockData(stockData));
            System.out.println("Fetched and cached fresh data for " + upperTicker + " at $" + currentPrice);

            return stockData;

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

    private static class CachedStockData {
        StockDataResponse data;
        LocalDateTime timestamp;

        CachedStockData(StockDataResponse data) {
            this.data = data;
            this.timestamp = LocalDateTime.now();
        }

        boolean isValid() {
            return LocalDateTime.now().isBefore(timestamp.plusMinutes(CACHE_DURATION_MINUTES));
        }
    }
}