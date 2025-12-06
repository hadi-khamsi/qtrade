package com.qtrade.service;

import com.qtrade.entity.Account;
import com.qtrade.entity.Order;
import com.qtrade.entity.Stock;
import com.qtrade.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final StockService stockService;
    private final Random random = new Random();
    private final PortfolioService portfolioService;

    public OrderService(OrderRepository orderRepository, AccountService accountService,
                        StockService stockService, PortfolioService portfolioService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
        this.stockService = stockService;
        this.portfolioService = portfolioService;
    }

    @Transactional
    public Order placeOrder(Long accountId, Long stockId, int quantity, String type) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!type.equalsIgnoreCase("BUY") && !type.equalsIgnoreCase("SELL")) {
            throw new IllegalArgumentException("Order type must be BUY or SELL");
        }

        Account account = accountService.getAccountById(accountId);
        Stock stock = stockService.getStockById(stockId);

        double executionPrice = calculateQuantumPrice(stock.getCurrentPrice());
        double totalCost = executionPrice * quantity;

        if (type.equalsIgnoreCase("BUY")) {
            if (account.getBalance() < totalCost) {
                throw new IllegalArgumentException("Insufficient funds for this purchase");
            }
            account.setBalance(account.getBalance() - totalCost);
        } else {
            account.setBalance(account.getBalance() + totalCost);
        }

        Order order = new Order(type.toUpperCase(), quantity, executionPrice, account, stock);
        accountService.updateBalance(account, account.getBalance());

        // Update portfolio
        portfolioService.updatePortfolio(account, stock, quantity, executionPrice, type);

        return orderRepository.save(order);
    }

    private double calculateQuantumPrice(double basePrice) {
        double minPrice = basePrice * 0.97;
        double maxPrice = basePrice * 1.03;
        return minPrice + (maxPrice - minPrice) * random.nextDouble();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByAccountId(Long accountId) {
        return orderRepository.findByAccountId(accountId);
    }
}