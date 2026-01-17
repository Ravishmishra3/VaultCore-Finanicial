package com.incapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long>{

	 Optional<Portfolio> findByAccountId(Long accountId);
}
