package com.incapp.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.service.TradingService;

@RestController
@RequestMapping("/trade")
public class TradingController {

	@Autowired
	TradingService tradingService;
	
	@PostMapping("/buy")
	public String buy(@RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam String asset,
            Authentication auth) {

       tradingService.buy(accountId, amount, asset, auth.getName());
       return "Trade BUY executed successfully";
}

	@PostMapping("/sell")
	public String sell(@RequestParam Long accountId,
	                   @RequestParam BigDecimal amount,
	                   @RequestParam String asset,
	                   Authentication auth) {

	    tradingService.sell(accountId, amount, asset, auth.getName());
	    return "Trade SELL executed successfully";
	}
}
