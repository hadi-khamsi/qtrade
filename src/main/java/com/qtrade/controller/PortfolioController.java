package com.qtrade.controller;

import com.qtrade.dto.PortfolioResponse;
import com.qtrade.dto.PortfolioSummaryResponse;
import com.qtrade.entity.Account;
import com.qtrade.entity.Portfolio;
import com.qtrade.service.AccountService;
import com.qtrade.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final AccountService accountService;

    public PortfolioController(PortfolioService portfolioService, AccountService accountService) {
        this.portfolioService = portfolioService;
        this.accountService = accountService;
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<PortfolioSummaryResponse> getPortfolioSummary(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        List<Portfolio> holdings = portfolioService.getPortfolioByAccountId(accountId);

        List<PortfolioResponse> portfolioResponses = holdings.stream()
                .map(p -> new PortfolioResponse(
                        p.getId(),
                        p.getStock().getTicker(),
                        p.getStock().getName(),
                        p.getQuantity(),
                        p.getAverageCost(),
                        p.getStock().getCurrentPrice()
                ))
                .collect(Collectors.toList());

        PortfolioSummaryResponse summary = new PortfolioSummaryResponse(
                account.getId(),
                account.getUsername(),
                account.getBalance(),
                portfolioResponses
        );

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/account/{accountId}/holdings")
    public ResponseEntity<List<PortfolioResponse>> getHoldings(@PathVariable Long accountId) {
        List<Portfolio> holdings = portfolioService.getPortfolioByAccountId(accountId);

        List<PortfolioResponse> responses = holdings.stream()
                .map(p -> new PortfolioResponse(
                        p.getId(),
                        p.getStock().getTicker(),
                        p.getStock().getName(),
                        p.getQuantity(),
                        p.getAverageCost(),
                        p.getStock().getCurrentPrice()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}