package com.incapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incapp.entity.Account;
import com.incapp.entity.LedgerEntry;
import com.incapp.exception.ResourceNotFoundException;
import com.incapp.repository.AccountRepository;
import com.incapp.repository.LedgerRepository;

@Service
public class TradingService {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	LedgerRepository ledgerRepository;
	
	@Autowired
	private PortfolioService portfolioService;
	
	@Transactional
	public void buy(Long accountId, BigDecimal amount, String asset, String username) {

	    validateAmount(amount);

	    Account account = accountRepository
	            .findByIdAndUserUsername(accountId, username)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Account not found or access denied"));

	    if (account.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient funds to BUY");
	    }

	    // 1️⃣ Deduct money
	    account.setBalance(account.getBalance().subtract(amount));
	    accountRepository.save(account);

	    // 2️⃣ Add asset to portfolio
	    portfolioService.buyAsset(accountId, asset, amount);

	    // 3️⃣ Ledger entry
	    ledgerRepository.save(new LedgerEntry(
	            null,
	            UUID.randomUUID().toString(),
	            amount,
	            "DEBIT",
	            LocalDateTime.now(),
	            account
	    ));
	}


	@Transactional
	public void sell(Long accountId, BigDecimal amount, String asset, String username) {

	    validateAmount(amount);

	    Account account = accountRepository
	            .findByIdAndUserUsername(accountId, username)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Account not found or access denied"));

	    // 1️⃣ Remove asset from portfolio
	    portfolioService.sellAsset(accountId, asset, amount);

	    // 2️⃣ Add money
	    account.setBalance(account.getBalance().add(amount));
	    accountRepository.save(account);

	    // 3️⃣ Ledger entry
	    ledgerRepository.save(new LedgerEntry(
	            null,
	            UUID.randomUUID().toString(),
	            amount,
	            "CREDIT",
	            LocalDateTime.now(),
	            account
	    ));
	}


    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

}
