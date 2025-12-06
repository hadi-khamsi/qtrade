package com.qtrade.repository;

import com.qtrade.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByAccountId(Long accountId);
    Optional<Portfolio> findByAccountIdAndStockId(Long accountId, Long stockId);
}