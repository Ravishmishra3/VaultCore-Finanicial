package com.incapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.LedgerEntry;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long>{

	// USER → own trade history
    Page<LedgerEntry> findByAccountUserUsername(
            String username,
            Pageable pageable
    );

    // ADMIN → all trade history
    Page<LedgerEntry> findAll(Pageable pageable);
    
    
    List<LedgerEntry> findByAccountUserUsernameOrderByTimestampDesc(String username); //new
}
