package com.example.user.service;

import com.example.user.payload.GitHubLookupUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class GitHubLookupService {
    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<GitHubLookupUser> findUserAsync(String user) throws InterruptedException {
        log.info("finding GitHub User async..." + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        GitHubLookupUser lookupResult;
        try {
            lookupResult = this.restTemplate.getForObject(url, GitHubLookupUser.class);
        } catch(HttpClientErrorException e) {
            if(e.getRawStatusCode() == 404) return null;
            throw e;
        }
        Thread.sleep(2000);
        return CompletableFuture.completedFuture(lookupResult);
    }

    public GitHubLookupUser findUserSync(String user) throws InterruptedException {
        log.info("finding GitHub User sybc..." + Thread.currentThread().getName());
        String url = String.format("https://api.github.com/users/%s", user);
        GitHubLookupUser lookupResult;
        try {
            lookupResult = this.restTemplate.getForObject(url, GitHubLookupUser.class);
        } catch(HttpClientErrorException e) {
            if(e.getRawStatusCode() == 404) return null;
            throw e;
        }
        Thread.sleep(2000);
        return lookupResult;
    }
}
