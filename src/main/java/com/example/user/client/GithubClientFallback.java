package com.example.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GithubClientFallback implements GithubClient {
    @Override
    public String getAccessToken(Map<String, String> request) {
        log.debug("getAccessToken fallback called for GithubClient.");
        return null;
    }

    @Override
    public String getUserProfile() {
        log.debug("getUserProfile fallback called for GithubClient.");
        return null;
    }
}
