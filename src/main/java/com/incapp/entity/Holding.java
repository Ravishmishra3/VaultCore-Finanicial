package com.incapp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Holding {

	@Id
    @GeneratedValue
    private Long id;

    private String assetName;   // GOLD, BTC, STOCK_X

    private BigDecimal quantity;

    @ManyToOne
    private Portfolio portfolio;
}
