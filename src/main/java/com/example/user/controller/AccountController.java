package com.example.user.controller;

import com.example.user.client.CouponClient;
import com.example.user.config.security.JWTSecurityConstants;
import com.example.user.entity.Account;
import com.example.user.entity.CreditCard;
import com.example.user.payload.Coupon;
import com.example.user.service.AccountService;
import com.example.user.service.AddressService;
import com.example.user.service.CreditCardService;
import com.example.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/account")
public class AccountController {
    private final MemberService memberService;
    private final AccountService accountService;
    private final AddressService addressService;
    private final CreditCardService creditCardService;
    private final CouponClient couponClient;

    @Autowired
    public AccountController(MemberService memberService, AccountService accountService,
                             AddressService addressService, CreditCardService creditCardService,
                             CouponClient couponClient) {
        this.memberService = memberService;
        this.accountService = accountService;
        this.addressService = addressService;
        this.creditCardService = creditCardService;
        this.couponClient = couponClient;
    }

    @GetMapping
    public ResponseEntity<Account> getAccountInfo(@RequestAttribute Long memberId, @RequestAttribute String accessToken) {
        Account account = memberService.findById(memberId).getAccount();
        List<Coupon> coupons = couponClient.getUserCoupons(accessToken);
        account.setCoupons(coupons);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/credit-card")
    public ResponseEntity<CreditCard> saveCreditCard() {
        return null;
    }
}
