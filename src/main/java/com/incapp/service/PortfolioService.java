package com.incapp.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incapp.dto.HoldingDTO;
import com.incapp.dto.PortfolioDTO;
import com.incapp.entity.Account;
import com.incapp.entity.Holding;
import com.incapp.entity.Portfolio;
import com.incapp.repository.AccountRepository;
import com.incapp.repository.HoldingRepository;
import com.incapp.repository.PortfolioRepository;

@Service
@Transactional
public class PortfolioService {

	
	private final PortfolioRepository portfolioRepo;
    private final HoldingRepository holdingRepo;
    private final AccountRepository accountRepo;

    public PortfolioService(PortfolioRepository portfolioRepo,
                            HoldingRepository holdingRepo,
                            AccountRepository accountRepo) {
        this.portfolioRepo = portfolioRepo;
        this.holdingRepo = holdingRepo;
        this.accountRepo = accountRepo;
    }

    // Get or create portfolio
    public Portfolio getPortfolio(Long accountId) {

        return portfolioRepo.findByAccountId(accountId)
                .orElseGet(() -> {
                    Account account = accountRepo.findById(accountId)
                            .orElseThrow(() -> new RuntimeException("Account not found"));

                    Portfolio portfolio = new Portfolio();
                    portfolio.setAccount(account);
                    return portfolioRepo.save(portfolio);
                });
    }

    // BUY asset
    public void buyAsset(Long accountId, String asset, BigDecimal qty) {

        Portfolio portfolio = getPortfolio(accountId);

        Holding holding = holdingRepo
                .findByPortfolioAndAssetName(portfolio, asset)
                .orElseGet(() -> {
                    Holding h = new Holding();
                    h.setAssetName(asset);
                    h.setQuantity(BigDecimal.ZERO);
                    h.setPortfolio(portfolio);
                    return h;
                });

        holding.setQuantity(holding.getQuantity().add(qty));
        holdingRepo.save(holding);
    }

    // SELL asset
    public void sellAsset(Long accountId, String asset, BigDecimal qty) {

        Portfolio portfolio = getPortfolio(accountId);

        Holding holding = holdingRepo
                .findByPortfolioAndAssetName(portfolio, asset)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (holding.getQuantity().compareTo(qty) < 0) {
            throw new RuntimeException("Insufficient asset quantity");
        }

        holding.setQuantity(holding.getQuantity().subtract(qty));
        
        if (holding.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            holdingRepo.delete(holding);
        } else {
            holdingRepo.save(holding);
        }
        
        holdingRepo.save(holding);
    }
    
    @Transactional(readOnly = true)
    public PortfolioDTO getPortfolioDTO(Long accountId) {

        Portfolio portfolio = portfolioRepo.findByAccountId(accountId)
            .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        PortfolioDTO dto = new PortfolioDTO();
        dto.setAccountId(portfolio.getAccount().getId());
        dto.setAccountNumber(portfolio.getAccount().getAccountNumber());

        dto.setHoldings(
            portfolio.getHoldings().stream().map(h -> {
                HoldingDTO hd = new HoldingDTO();
                hd.setId(h.getId());
                hd.setAssetName(h.getAssetName());
                hd.setQuantity(h.getQuantity());
                return hd;
            }).toList()
        );

        return dto;
    }
}
