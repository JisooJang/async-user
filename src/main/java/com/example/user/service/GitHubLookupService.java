package com.example.user.service;

import com.example.user.payload.GitHubLookupUser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@PropertySource("classpath:rest_client.properties")
public class GitHubLookupService {
    private final RestTemplate restTemplate;

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.callback_uri}")
    private String callbackUri;

    @Value("${github.authorize_uri}")
    private String authorize_uri;

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

    public String getUniqueState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString();
    }

    public String getGithubLoginURL(String sessionId) {
        // TODO: state값 저장시 cache key를 어떻게 저장할지? (key : github-state-{request_session_id}, value=state 값), ttl 10분
        String state = getUniqueState();
        System.out.println("request session_id1 : " + sessionId);
        return String.format(authorize_uri + "?client_id=%s&redirect_uri=%s&state=%s",
                clientId, callbackUri, state);
    }
}
