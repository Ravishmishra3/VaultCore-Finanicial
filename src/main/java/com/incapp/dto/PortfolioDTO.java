package com.incapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class PortfolioDTO {

	private Long accountId;
    private String accountNumber;
    private List<HoldingDTO> holdings;
}
