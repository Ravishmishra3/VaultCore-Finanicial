package com.incapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long>{

	Page<Trade> findByAccount_User_Username(
	        String username,
	        Pageable pageable
	    );
	
	Page<Trade> findAll(Pageable pageable);
}
