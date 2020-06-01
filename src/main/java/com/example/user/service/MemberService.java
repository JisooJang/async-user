package com.example.user.service;

import com.example.user.domain.Member;
import com.example.user.exception.InvalidPayloadException;
import com.example.user.payload.UserModel;
import com.example.user.repository.MemberRepository;
import com.example.user.utils.ValidationRegex;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

    public void validationPassword(String password) {
        if(!Pattern.matches(ValidationRegex.PASSWORD, password)) {
            throw new InvalidPayloadException(
                    "password should be use alphabet, number, special-character at least 1 time each." +
                            "And length should be over 8 characters.");
        }
    }

    @Transactional
    public Member signUp(UserModel model) {
        // password 암호화 저장
        // 트랜잭션 레벨 설정
        if(memberRepository.findByMediaId(model.getId()) != null) {
            throw new InvalidPayloadException("user id already exists.");
        }
        validationPassword(model.getPassword());
        Member member = new Member(model.getId(), model.getPassword());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        try {
            return memberRepository.save(member);
        } catch(ConstraintViolationException ex) {
            throw new InvalidPayloadException("Invalid arguments.");
        }

    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
}
