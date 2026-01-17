package com.incapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incapp.dto.AccountCreateRequest;
import com.incapp.entity.Account;
import com.incapp.entity.LedgerEntry;
import com.incapp.entity.Transaction;
import com.incapp.entity.TransactionType;
import com.incapp.entity.User;
import com.incapp.exception.BadRequestException;
import com.incapp.exception.InsufficientBalanceException;
import com.incapp.exception.ResourceNotFoundException;
import com.incapp.repository.AccountRepository;
import com.incapp.repository.LedgerEntryRepository;
import com.incapp.repository.LedgerRepository;
import com.incapp.repository.TransactionRepository;
import com.incapp.repository.UserRepository;

@Service
@Transactional
public class AccountService {

	private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;


    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,TransactionRepository transactionRepository,LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

 // ✅ ADMIN creates account for USER
    public Account createAccount(AccountCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🚫 One user → One account
        if (accountRepository.existsByUser(user)) {
            throw new com.incapp.exception.BadRequestException("User already has a bank account");
        }

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(
                request.getBalance() != null ? request.getBalance() : BigDecimal.ZERO
        );
        account.setUser(user);

        return accountRepository.save(account);
    }
    
    
 //  USER can see own account
    @Transactional(readOnly = true)		
    public Account getMyAccount(String username) {
        return accountRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
    
    public Account credit(Long accountId, BigDecimal amount, String username) {

    	if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
    	
        Account account = accountRepository
                .findByIdAndUserUsername(accountId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        
        Transaction tx = new Transaction();
        tx.setAmount(amount);
        tx.setType(TransactionType.CREDIT);
        tx.setTransactionTime(LocalDateTime.now());
        tx.setAccount(account);

        transactionRepository.save(tx);
        
     // 🔹 Ledger entry (MANDATORY)
        LedgerEntry ledger = new LedgerEntry();
        ledger.setTransactionId(UUID.randomUUID().toString());
        ledger.setAmount(amount);
        ledger.setType("CREDIT");
        ledger.setTimestamp(LocalDateTime.now());
        ledger.setAccount(account);
        ledgerEntryRepository.save(ledger);
        
        return accountRepository.save(account);
    }
    
    
    public Account debit(Long accountId, BigDecimal amount, String username) {

    	if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Debit amount must be greater than zero");
        }
    	
        Account account = accountRepository
                .findByIdAndUserUsername(accountId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        
        Transaction tx = new Transaction();
        tx.setAmount(amount);
        tx.setType(TransactionType.DEBIT);
        tx.setTransactionTime(LocalDateTime.now());
        tx.setAccount(account);

        transactionRepository.save(tx);
        
        
        LedgerEntry ledger = new LedgerEntry();
        ledger.setTransactionId(UUID.randomUUID().toString());
        ledger.setAmount(amount);
        ledger.setType("DEBIT");
        ledger.setTimestamp(LocalDateTime.now());
        ledger.setAccount(account);
        ledgerEntryRepository.save(ledger);

        
        return accountRepository.save(account);
    }
    

    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public BigDecimal getBalance(Long id) {
        return getAccount(id).getBalance();
    }
}
