package com.example.user.exception;

public class AccountNotFoundException extends RuntimeException {
    private Long accountId;

    public AccountNotFoundException(Long accountId) {
        super("Account not found : " +accountId);
    }
}
