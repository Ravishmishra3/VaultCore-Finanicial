package com.incapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.entity.LedgerEntry;
import com.incapp.entity.Trade;
import com.incapp.service.TradeHistoryService;

@RestController
@RequestMapping("/trades")
public class TradeHistoryController {

	private final TradeHistoryService tradeHistoryService;

    public TradeHistoryController(TradeHistoryService tradeHistoryService) {
        this.tradeHistoryService = tradeHistoryService;
    }

    // 👤 USER → own trade history
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public Page<LedgerEntry> myTrades(
            Authentication authentication,
            Pageable pageable) {

        return tradeHistoryService.getMyTradeHistory(
                authentication.getName(),
                pageable
        );
    }

    // 👮 ADMIN → all trades
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public Page<LedgerEntry> allTrades(Pageable pageable) {
        return tradeHistoryService.getAllTradeHistory(pageable);
    }
    
    
// // USER → own trades
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/my")
//    public Page<Trade> myTrades(Authentication auth, Pageable pageable) {
//        return tradeHistoryService.getMyTrades(auth.getName(), pageable);
//    }
//
//    // ADMIN → all trades
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/all")
//    public Page<Trade> allTrades(Pageable pageable) {
//        return tradeHistoryService.getAllTrades(pageable);
//    }
}
