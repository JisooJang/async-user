package com.example.user.service;

import com.example.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class GitHubLookupService {
    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        log.info("finding GitHub User..." + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        User lookupResult = this.restTemplate.getForObject(url, User.class);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture(lookupResult);
    }

    public User findUserSync(String user) throws InterruptedException {
        log.info("finding GitHub User..." + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        User lookupResult = this.restTemplate.getForObject(url, User.class);
        Thread.sleep(2000);
        return lookupResult;
    }
}
