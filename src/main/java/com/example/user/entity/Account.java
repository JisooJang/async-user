package com.example.user.entity;

import com.example.user.payload.Coupon;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 신용 카드, 주소, 주문 내역을 보유
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Transient
    private List<Coupon> coupons = new ArrayList<>();

    @Builder
    public Account(Long id) {
        this.id = id;
    }
}
