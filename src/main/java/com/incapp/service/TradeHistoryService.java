package com.incapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incapp.entity.LedgerEntry;
import com.incapp.entity.Trade;
import com.incapp.repository.LedgerRepository;
import com.incapp.repository.TradeRepository;



@Service
//@Transactional(readOnly = true)
public class TradeHistoryService {

	 private final LedgerRepository ledgerRepository;

	    public TradeHistoryService(LedgerRepository ledgerRepository) {
	        this.ledgerRepository = ledgerRepository;
	    }

	    // USER → own trades
	    public Page<LedgerEntry> getMyTradeHistory(String username, Pageable pageable) {
	        return ledgerRepository.findByAccountUserUsername(username, pageable);
	    }

	    // ADMIN → all trades
	    public Page<LedgerEntry> getAllTradeHistory(Pageable pageable) {
	        return ledgerRepository.findAll(pageable);
	    }
	
	
	
//	private final TradeRepository tradeRepo;
//
//    public TradeHistoryService(TradeRepository tradeRepo) {
//        this.tradeRepo = tradeRepo;
//    }
//
// // USER → own trades only
//    public Page<Trade> getMyTrades(String username, Pageable pageable) {
//        return tradeRepo.findByAccount_User_Username(username, pageable);
//    }
//
// // ADMIN → all trades
//    public Page<Trade> getAllTrades(Pageable pageable) {
//        return tradeRepo.findAll(pageable);
//    }
}
