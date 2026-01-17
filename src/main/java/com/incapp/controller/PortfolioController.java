package com.incapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.dto.PortfolioDTO;
import com.incapp.entity.Portfolio;
import com.incapp.repository.PortfolioRepository;
import com.incapp.service.PortfolioService;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

	private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/{accountId}")
    public PortfolioDTO getPortfolio(@PathVariable Long accountId) {
        return portfolioService.getPortfolioDTO(accountId);
    }
}
