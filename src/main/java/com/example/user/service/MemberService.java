package com.example.user.service;

import com.example.user.entity.Member;
import com.example.user.exception.InvalidPayloadException;
import com.example.user.exception.MemberNotFoundException;
import com.example.user.payload.SignUpUser;
import com.example.user.repository.MemberRepository;
import com.example.user.utils.ValidationRegex;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validationPayload(SignUpUser model) {
        // 1. 비밀번호 검사
        if(!Pattern.matches(ValidationRegex.USEREMAIL, model.getEmail())) {
            throw new InvalidPayloadException("Invalid user email format.");
        }
        if(!Pattern.matches(ValidationRegex.PASSWORD, model.getPassword())) {
            throw new InvalidPayloadException(
                    "password should be use alphabet, number, special-character at least 1 time each." +
                            "And length should be over 8 characters.");
        }
    }

    @Transactional
    public Member signUp(SignUpUser model) {
        // 트랜잭션 레벨 설정
        if(memberRepository.findByEmail(model.getEmail()) != null) {
            throw new InvalidPayloadException("user email already exists.");
        }
        validationPayload(model);
        Member member;
        try {
            member = Member.builder()
                    .email(model.getEmail())
                    .password(passwordEncoder.encode(model.getPassword()))
                    .lastName(model.getLastName())
                    .firstName(model.getFirstName())
                    .birthDate(LocalDate.of(model.getBirthYear(), model.getBirthMonth(), model.getBirthDay()))
                    .phoneNumber(model.getPhoneNumber())
                    .build();
        } catch(DateTimeException e) {
            throw new InvalidPayloadException("Invalid birth date time arguments.");
        }
        try {
            return memberRepository.save(member);
        } catch(ConstraintViolationException ex) {
            throw new InvalidPayloadException("Invalid arguments.");
        }
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

}
