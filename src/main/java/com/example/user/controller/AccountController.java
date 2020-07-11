package com.example.user.controller;

import com.example.user.entity.Account;
import com.example.user.entity.CreditCard;
import com.example.user.service.AccountService;
import com.example.user.service.AddressService;
import com.example.user.service.CreditCardService;
import com.example.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/account")
public class AccountController {
    private final MemberService memberService;
    private final AccountService accountService;
    private final AddressService addressService;
    private final CreditCardService creditCardService;

    @Autowired
    public AccountController(MemberService memberService, AccountService accountService,
                             AddressService addressService, CreditCardService creditCardService) {
        this.memberService = memberService;
        this.accountService = accountService;
        this.addressService = addressService;
        this.creditCardService = creditCardService;
    }

    @GetMapping("/")
    public ResponseEntity<Account> getAccountInfo(@RequestAttribute Long memberId) {
        Account account = memberService.findById(memberId).getAccount();
        return ResponseEntity.ok(account);
    }

    @PostMapping("/credit-card")
    public ResponseEntity<CreditCard> saveCreditCard() {
        return null;
    }
}
