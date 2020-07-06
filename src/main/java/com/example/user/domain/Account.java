package com.example.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 신용 카드, 주소, 주문 내역을 보유
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditCard> creditCards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();

    public Account(String accountNumber, List<Address> addresses) {
        this.accountNumber = accountNumber;
        this.addresses = addresses;
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
