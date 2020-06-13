package com.example.user.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpUser {
    private String email;
    private String lastName; // 이름
    private String firstName; // 성
    private String password;

    private String birthMonth; // 생일 월
    private String birthDay; // 생일 일
    private String birthYear; // 생일 연도

    private boolean marketing; // 마케팅 메시지 수신 여부

}
