package com.incapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.incapp.entity.Account;
import com.incapp.entity.LedgerEntry;
import com.incapp.entity.Transaction;
import com.incapp.exception.ResourceNotFoundException;
import com.incapp.repository.AccountRepository;
import com.incapp.repository.LedgerRepository;
import com.incapp.repository.TransactionRepository;



@Service
public class TransactionService {

	@Autowired
	AccountRepository accRepo;
	
	@Autowired
	LedgerRepository ledgerRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	//or
	
//	private final AccountRepository accRepo;
//    private final LedgerRepository ledgerRepository;
//
//    public TransactionService(AccountRepository accRepo,
//                              LedgerRepository ledgerRepository) {
//        this.accRepo = accRepo;
//        this.ledgerRepository = ledgerRepository;
//    }

	@Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        // ✅ Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Account from = accRepo.findById(fromId)
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));

        Account to = accRepo.findById(toId)
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        // ✅ Balance check
        if (from.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        // ✅ Update balances
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        // ✅ Explicit save (clarity + safety)
        accRepo.save(from);
        accRepo.save(to);

        String txId = UUID.randomUUID().toString();

        // ✅ Ledger entries
        ledgerRepository.save(new LedgerEntry(
                null, txId, amount, "DEBIT", LocalDateTime.now(), from));

        ledgerRepository.save(new LedgerEntry(
                null, txId, amount, "CREDIT", LocalDateTime.now(), to));

        // ✅ Fraud alert
        if (amount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            System.out.println("⚠ Fraud Alert");
        }
    }
	
	
	// USER
    public List<Transaction> getMyTransactions(String username) {
        var account = accRepo.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByAccountId(account.getId());
    }

    // ADMIN
    public List<Transaction> getByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

}
