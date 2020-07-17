package com.example.user.service;

import com.example.user.payload.EmailSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotiService {
    private final KafkaTemplate<String, EmailSend> kafkaTemplate;

    @Autowired
    public NotiService(KafkaTemplate<String, EmailSend> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendAlarmTalkToUser(EmailSend emailSend) {
        // 테스트용으로 로그만 찍음. 향후 알림톡을 보내거나 메시지 큐 구조로 메시지를 쏘는 등의 작업이 될 수 있다.
        log.info("sendAlarmTalkToUser. thread : " + Thread.currentThread().getName());
        kafkaTemplate.send("email.notification", emailSend);
    }
}
