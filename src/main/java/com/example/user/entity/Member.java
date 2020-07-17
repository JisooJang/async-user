package com.example.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    // 각 필드 validation 상세 annotation 추가할 것. (size, blank 등)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 30)
    private String email;  // TODO: add index

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

//    @Column(name = "join_date", nullable = false, updatable = false)
//    @CreationTimestamp
//    private LocalDateTime joinDate;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Account account;

    @Builder
    public Member(String email, String password, LocalDate birthDate, String lastName, String firstName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }
}
