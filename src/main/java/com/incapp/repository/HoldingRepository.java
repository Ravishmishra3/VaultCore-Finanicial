package com.incapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.Holding;
import com.incapp.entity.Portfolio;

public interface HoldingRepository extends JpaRepository<Holding, Long>{

	Optional<Holding> findByPortfolioAndAssetName(Portfolio portfolio, String assetName);
}
