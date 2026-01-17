package com.incapp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferRequest {

	private Long fromAccountId;
	private Long toAccountId;
	private BigDecimal amount;
}
