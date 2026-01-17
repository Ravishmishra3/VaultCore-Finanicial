package com.incapp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class HoldingDTO {

	private Long id;
    private String assetName;
    private BigDecimal quantity;
}
