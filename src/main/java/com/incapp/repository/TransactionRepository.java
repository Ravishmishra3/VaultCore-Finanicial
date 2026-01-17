package com.incapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

//	Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
	
	 List<Transaction> findByAccountId(Long accountId);
}
