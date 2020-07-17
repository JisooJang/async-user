package com.example.user.payload;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Getter
@Setter
public class SignUpUser {
    @NotEmpty(message ="email should be not empty.")
    private String email;
    private String lastName; // 이름
    private String firstName; // 성
    private String password;
    private String phoneNumber;

    private int birthMonth; // 생일 월
    private int birthDay; // 생일 일
    private int birthYear; // 생일 연도

    private Boolean marketing; // 마케팅 메시지 수신 여부

    public boolean validateNullCheck() {
        if(Strings.isBlank(email) || Strings.isBlank(lastName) || Strings.isBlank(firstName) || Strings.isBlank(password)
        || Strings.isBlank(phoneNumber)) {
            return false;
        }
        if(marketing == null) {
            return false;
        }
        return true;
    }
}
