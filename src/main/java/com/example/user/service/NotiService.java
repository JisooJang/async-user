package com.example.user.service;

import com.example.user.payload.EmailSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotiService {
    private final KafkaTemplate<String, EmailSend> kafkaTemplate;

    @Autowired
    public NotiService(KafkaTemplate<String, EmailSend> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAlarmTalkToUser(EmailSend emailSend) {
        log.info("sendAlarmTalkToUser. thread : " + Thread.currentThread().getName());
        kafkaTemplate.send("email.notification", emailSend);
    }
}
