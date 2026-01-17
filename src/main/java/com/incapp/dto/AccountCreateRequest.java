package com.incapp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AccountCreateRequest {

	private String accountNumber;
    private BigDecimal balance;
    private Long userId;
}
