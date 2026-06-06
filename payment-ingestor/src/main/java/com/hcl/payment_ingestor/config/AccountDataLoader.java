package com.hcl.payment_ingestor.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.payment_ingestor.entity.AccountEntity;
import com.hcl.payment_ingestor.repository.AccountRepository;

@Component
public class AccountDataLoader implements CommandLineRunner {

    public AccountDataLoader(AccountRepository accountRepository, ObjectMapper objectMapper) {
		super();
		this.accountRepository = accountRepository;
		this.objectMapper = objectMapper;
	}



	private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    
    

    @Override
    public void run(String... args) throws Exception {

        if(accountRepository.count() > 0) {
            return;
        }

        InputStream is =
                new ClassPathResource("accounts.json")
                        .getInputStream();

        List<AccountEntity> accounts =
                objectMapper.readValue(
                        is,
                        new TypeReference<List<AccountEntity>>() {}
                );

        accountRepository.saveAll(accounts);
    }
}
