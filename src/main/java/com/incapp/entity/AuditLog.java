package com.incapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AuditLog {

	@Id
    @GeneratedValue
    private Long id;

    private String action;
    private String details;
    private LocalDateTime timestamp;
}

