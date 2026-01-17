package com.incapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incapp.entity.LedgerEntry;
import com.incapp.repository.LedgerRepository;

@Service
public class LedgerService {

	
	private final LedgerRepository ledgerRepo;

    public LedgerService(LedgerRepository ledgerRepo) {
        this.ledgerRepo = ledgerRepo;
    }

    public List<LedgerEntry> getMyLedger(String username) {
        return ledgerRepo.findByAccountUserUsernameOrderByTimestampDesc(username);
    }
}
