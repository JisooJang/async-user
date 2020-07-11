package com.example.user.service;

import com.example.user.domain.Account;
import com.example.user.exception.AccountNotFoundException;
import com.example.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.repository = accountRepository;
    }
    public Account getUserAccount(Long id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account save(Account account) {
        return repository.save(account);
    }
}
