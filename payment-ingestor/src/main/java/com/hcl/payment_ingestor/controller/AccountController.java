package com.hcl.payment_ingestor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.payment_ingestor.entity.AccountEntity;
import com.hcl.payment_ingestor.exception.AccountNotFoundException;
import com.hcl.payment_ingestor.repository.AccountRepository;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountRepository accountRepository;

	public AccountController(AccountRepository accountRepository) {

		this.accountRepository = accountRepository;
	}

	 @GetMapping
	    public AccountEntity getAccount(@RequestParam String accountId) {
	        return accountRepository.findById(accountId)
	                .orElseThrow(() ->
	                        new AccountNotFoundException("Account not found: " + accountId));
	    }
}
