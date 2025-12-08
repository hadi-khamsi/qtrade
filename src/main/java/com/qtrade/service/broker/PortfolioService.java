package com.qtrade.service.broker;

import com.qtrade.entity.broker.Account;
import com.qtrade.entity.broker.Portfolio;
import com.qtrade.entity.broker.Stock;
import com.qtrade.repository.broker.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public void updatePortfolio(Account account, Stock stock, int quantity, double price, String orderType) {
        Optional<Portfolio> existingPortfolio = portfolioRepository.findByAccountIdAndStockId(
                account.getId(), stock.getId());

        if (orderType.equalsIgnoreCase("BUY")) {
            if (existingPortfolio.isPresent()) {
                Portfolio portfolio = existingPortfolio.get();
                int newQuantity = portfolio.getQuantity() + quantity;
                double newAverageCost = ((portfolio.getAverageCost() * portfolio.getQuantity()) +
                        (price * quantity)) / newQuantity;
                portfolio.setQuantity(newQuantity);
                portfolio.setAverageCost(newAverageCost);
                portfolioRepository.save(portfolio);
            } else {
                Portfolio portfolio = new Portfolio(account, stock, quantity, price);
                portfolioRepository.save(portfolio);
            }
        } else if (orderType.equalsIgnoreCase("SELL")) {
            if (existingPortfolio.isPresent()) {
                Portfolio portfolio = existingPortfolio.get();
                if (portfolio.getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient shares to sell");
                }
                int newQuantity = portfolio.getQuantity() - quantity;
                if (newQuantity == 0) {
                    portfolioRepository.delete(portfolio);
                } else {
                    portfolio.setQuantity(newQuantity);
                    portfolioRepository.save(portfolio);
                }
            } else {
                throw new IllegalArgumentException("No shares to sell");
            }
        }
    }

    public List<Portfolio> getPortfolioByAccountId(Long accountId) {
        return portfolioRepository.findByAccountId(accountId);
    }

    public Optional<Portfolio> getPortfolioPosition(Long accountId, Long stockId) {
        return portfolioRepository.findByAccountIdAndStockId(accountId, stockId);
    }
}