package com.incapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Trade {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asset;          // AAPL, BTC
    private BigDecimal quantity;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TradeType type;        // BUY / SELL

    private BigDecimal amount;     // quantity × price

    private LocalDateTime createdAt;

    @ManyToOne
    private Account account;
}
