package com.example.user.service;

import com.example.user.client.GithubClient;
import com.example.user.exception.InvalidStateException;
import com.example.user.payload.GitHubLookupUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@PropertySource("classpath:rest_client.properties")
public class GitHubLookupService {
    private final RestTemplate restTemplate;

    private final StringRedisTemplate redisTemplate;

    private final GithubClient githubClient;

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.callback_uri}")
    private String callbackUri;

    @Value("${github.authorize_uri}")
    private String authorizeUri;

    @Value("${github.state.cache.prefix}")
    private String cachePrefix;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder, StringRedisTemplate redisTemplate, GithubClient githubClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.redisTemplate = redisTemplate;
        this.githubClient = githubClient;
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
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String state = getUniqueState();
        log.info("request session_id1 : " + sessionId);

        values.set(cachePrefix + sessionId, state, 10, TimeUnit.MINUTES); // timeout: 10분
        return String.format(authorizeUri + "?client_id=%s&redirect_uri=%s&state=%s",
                clientId, callbackUri, state);
    }

    public String getGithubAccessToken(String sessionId, String code, String state) {
        // 1. 캐시에 있는 state값과 응답으로 온 state값 비교하기. (ttl 10분) (request_session_id 키로 찾음)
        log.info("request session_id2 (in callback) : " + sessionId);
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        String cacheState = values.get(cachePrefix + sessionId);
        log.info(cacheState + " : " + state);
        if(!state.equals(cacheState)) {
            throw new InvalidStateException("Invalid state value.");
        }

        // 2. state값이 일치하면, csrf 공격이 아닌걸로 간주하고, oauth server에 access token 요청
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("code", code);
        requestBody.put("state", state);

        // TODO: POJO로 requestbody 설정시 HttpConverter 에러남. (encode error)
//        GithubAccessTokenRequest requestBody = GithubAccessTokenRequest.builder()
//                .client_id(clientId)
//                .client_secret(clientSecret)
//                .code(code)
//                .state(state)
//                .build();

        String responseBody = githubClient.getAccessToken(requestBody);
        return Objects.requireNonNull(responseBody).split("&")[0].split("=")[1];
    }
}
