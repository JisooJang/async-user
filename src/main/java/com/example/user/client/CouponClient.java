package com.example.user.client;

import com.example.user.config.security.JWTSecurityConstants;
import com.example.user.payload.Coupon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "mycoupon-service")
public interface CouponClient {
    @GetMapping(value = "/coupon/user", headers = JWTSecurityConstants.HEADER_STRING + "={token}")
    List<Coupon> getUserCoupons(@RequestHeader String token);

}
