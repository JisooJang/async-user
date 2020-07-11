package com.example.user.entity;

import com.example.user.enums.CreditCardType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number", length = 16)
    private String number;

    private LocalDate expireDate;

    @Column(name = "cvv", length = 3)
    private String cvv; // 3자리

    @Column(name = "post_code", length = 5)
    private String postCode;

    @Enumerated(EnumType.STRING)
    private CreditCardType type;

    @Builder
    public CreditCard(String number, LocalDate expireDate, String cvv, String postCode, CreditCardType type) {
        this.number = number;
        this.expireDate = expireDate;
        this.cvv = cvv;
        this.postCode = postCode;
        this.type = type;
    }
}
