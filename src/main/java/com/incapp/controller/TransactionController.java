package com.incapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.dto.TransferRequest;
import com.incapp.entity.Transaction;
import com.incapp.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	
	@PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest req) {
		transactionService.transfer(req.getFromAccountId(),
                         req.getToAccountId(),
                         req.getAmount());
        return "Transfer Successful";
    }
	
	
	// USER → view own transactions
    @GetMapping("/my")
    public List<Transaction> myTransactions(Authentication auth) {
        return transactionService.getMyTransactions(auth.getName());
    }

    // ADMIN → view transactions of any account
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/account/{accountId}")
    public List<Transaction> accountTransactions(@PathVariable Long accountId) {
        return transactionService.getByAccount(accountId);
    }
}
