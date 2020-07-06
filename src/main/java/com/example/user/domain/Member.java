package com.example.user.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    // 각 필드 validation 상세 annotation 추가할 것. (size, blank 등)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(name = "join_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime joinDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @Builder
    public Member(String email, String password, LocalDate birthDate, String lastName, String firstName) {
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
