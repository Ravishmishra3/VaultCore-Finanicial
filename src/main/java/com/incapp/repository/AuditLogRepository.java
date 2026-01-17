package com.incapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
