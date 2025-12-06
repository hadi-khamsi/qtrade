package com.qtrade.controller;

import com.qtrade.dto.CreateStockRequest;
import com.qtrade.dto.StockDataResponse;
import com.qtrade.dto.StockResponse;
import com.qtrade.dto.UpdatePriceRequest;
import com.qtrade.entity.Stock;
import com.qtrade.service.StockService;
import com.qtrade.service.YahooFinanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final YahooFinanceService yahooFinanceService;

    public StockController(StockService stockService, YahooFinanceService yahooFinanceService) {
        this.stockService = stockService;
        this.yahooFinanceService = yahooFinanceService;
    }

    @PostMapping
    public ResponseEntity<StockResponse> addStock(@RequestBody CreateStockRequest request) {
        Stock stock = stockService.addStock(request.getTicker(), request.getName(), request.getPrice());
        StockResponse response = mapToStockResponse(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        List<StockResponse> stocks = stockService.getAllStocks().stream()
                .map(this::mapToStockResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long id) {
        Stock stock = stockService.getStockById(id);
        return ResponseEntity.ok(mapToStockResponse(stock));
    }

    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<StockResponse> getStockByTicker(@PathVariable String ticker) {
        Stock stock = stockService.getStockByTicker(ticker);
        return ResponseEntity.ok(mapToStockResponse(stock));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<StockResponse> updateStockPrice(@PathVariable Long id,
                                                          @RequestBody UpdatePriceRequest request) {
        Stock stock = stockService.updatePrice(id, request.getPrice());
        return ResponseEntity.ok(mapToStockResponse(stock));
    }

    @PostMapping("/import/{ticker}")
    public ResponseEntity<StockResponse> importStockFromYahoo(@PathVariable String ticker) {
        Stock stock = stockService.addStockFromYahoo(ticker);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToStockResponse(stock));
    }

    @GetMapping("/quote/{ticker}")
    public ResponseEntity<StockDataResponse> getStockQuote(@PathVariable String ticker) {
        StockDataResponse data = yahooFinanceService.fetchStockData(ticker);
        return ResponseEntity.ok(data);
    }

    @PutMapping("/refresh/{ticker}")
    public ResponseEntity<StockResponse> refreshStockPrice(@PathVariable String ticker) {
        Stock stock = stockService.refreshStockPrice(ticker);
        return ResponseEntity.ok(mapToStockResponse(stock));
    }

    @PutMapping("/refresh-all")
    public ResponseEntity<String> refreshAllStockPrices() {
        stockService.refreshAllStockPrices();
        return ResponseEntity.ok("All stock prices refreshed successfully");
    }

    private StockResponse mapToStockResponse(Stock stock) {
        return new StockResponse(stock.getId(), stock.getTicker(), stock.getName(), stock.getCurrentPrice());
    }
}