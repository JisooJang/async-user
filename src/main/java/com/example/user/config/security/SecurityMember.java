package com.example.user.config.security;

import com.example.user.entity.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class SecurityMember extends User {
    private final long id;

    public SecurityMember(Member member) {
        super(member.getEmail(), member.getPassword(), Collections.emptyList());
        this.id = member.getId();
    }
}
