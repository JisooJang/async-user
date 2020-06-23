package com.example.user.client;

import com.example.user.payload.GithubAccessTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "githubClient", url = "https://github.com/login/oauth")
public interface GithubClient {
    @PostMapping(value="/access_token", consumes= "application/json")
    public String getAccessToken(@RequestBody Map<String, String> request);

    @GetMapping(value = "/user", headers="Authorization=token ")
    public String getUserProfile();
}
