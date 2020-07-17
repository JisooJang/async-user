package com.example.user.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// https://docs.toast.com/ko/Notification/Email/ko/api-guide/
@Data
@Builder
public class EmailSend {
    private String senderAddress;
    private String senderName;
    private String title;
    private String body;
    private String receiveMailAddr;
    private String receiveType; //  (MRT0 : 받는사람 , MRT1 : 참조, MRT2 : 숨은참조)
}
