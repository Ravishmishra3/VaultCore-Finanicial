package com.incapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.entity.LedgerEntry;
import com.incapp.service.LedgerService;

@RestController
@RequestMapping("/ledger")
public class LedgerController {

	private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public List<LedgerEntry> myLedger(Authentication auth) {
        return ledgerService.getMyLedger(auth.getName());
    }
}
