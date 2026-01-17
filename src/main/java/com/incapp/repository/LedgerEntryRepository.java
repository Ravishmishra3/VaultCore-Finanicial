package com.incapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.LedgerEntry;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long>{

}
