package com.incapp.controller;

import java.math.BigDecimal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.dto.AccountCreateRequest;

import com.incapp.entity.Account;
import com.incapp.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping("/debug")
    public String debug(Authentication auth) {
        return auth.getAuthorities().toString();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Account createAccount(@RequestBody AccountCreateRequest request) {
        return accountService.createAccount(request);
    }

    @PreAuthorize("hasRole('USER')")  // Only users with role USER can access
    @GetMapping("/my")
    public Account myAccount(Authentication authentication) {
        // Get logged-in username from Authentication
        String username = authentication.getName();
        return accountService.getMyAccount(username);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountService.getAllAccounts(pageable);
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable Long id) {
        return accountService.getBalance(id);
    }
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/credit")
    public Account credit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            Authentication auth) {

        return accountService.credit(id, amount, auth.getName());
    }
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/debit")
    public Account debit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            Authentication auth) {

        return accountService.debit(id, amount, auth.getName());
    }
    
//    @GetMapping("/{id}/transactions")
//    @PreAuthorize("hasRole('USER')")
//    public Page<Transaction> getTransactions(
//            @PathVariable Long id,
//            Pageable pageable,
//            Authentication auth) {
//
//        return transactionRepository.findByAccountId(id, pageable);
//    }

}
