package com.qtrade.service;

import com.qtrade.dto.StockDataResponse;
import com.qtrade.entity.Stock;
import com.qtrade.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final YahooFinanceService yahooFinanceService;


    public StockService(StockRepository stockRepository, YahooFinanceService yahooFinanceService) {
        this.stockRepository = stockRepository;
        this.yahooFinanceService = yahooFinanceService;
    }
    @Transactional
    public Stock addStockFromYahoo(String ticker) {
        StockDataResponse data = yahooFinanceService.fetchStockData(ticker);

        if (stockRepository.findByTicker(ticker.toUpperCase()).isPresent()) {
            throw new IllegalArgumentException("Stock with ticker " + ticker + " already exists");
        }

        Stock stock = new Stock(data.getTicker(), data.getName(), data.getCurrentPrice());
        return stockRepository.save(stock);
    }
    @Transactional
    public Stock refreshStockPrice(String ticker) {
        Stock stock = getStockByTicker(ticker);
        double newPrice = yahooFinanceService.fetchCurrentPrice(ticker);
        stock.setCurrentPrice(newPrice);
        return stockRepository.save(stock);
    }
    @Transactional
    public void refreshAllStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            try {
                double newPrice = yahooFinanceService.fetchCurrentPrice(stock.getTicker());
                stock.setCurrentPrice(newPrice);
                stockRepository.save(stock);
            } catch (Exception e) {
                System.err.println("Failed to update price for " + stock.getTicker());
            }
        }
    }

    @Transactional
    public Stock addStock(String ticker, String name, double price) {
        if (stockRepository.findByTicker(ticker).isPresent()) {
            throw new IllegalArgumentException("Stock with ticker " + ticker + " already exists");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Stock price must be positive");
        }
        Stock stock = new Stock(ticker.toUpperCase(), name, price);
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + id));
    }

    public Stock getStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ticker: " + ticker));
    }

    @Transactional
    public Stock updatePrice(Long stockId, double newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Stock price must be positive");
        }
        Stock stock = getStockById(stockId);
        stock.setCurrentPrice(newPrice);
        return stockRepository.save(stock);
    }
}
