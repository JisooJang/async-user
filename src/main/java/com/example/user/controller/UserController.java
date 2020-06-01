package com.example.user.controller;

import com.example.user.payload.GitHubLookupUser;
import com.example.user.service.GitHubLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {
    private final GitHubLookupService gitHubLookupService;

    public UserController(GitHubLookupService service) {
        this.gitHubLookupService = service;
    }

    @PostMapping("/api/v1/users/github/async")
    public ResponseEntity<List<GitHubLookupUser>> getUsers(@RequestBody Set<String> users) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<CompletableFuture<GitHubLookupUser>> futureList = new ArrayList<>();
        for(String user : users) {
            CompletableFuture<GitHubLookupUser> userResult = gitHubLookupService.findUserAsync(user);
            futureList.add(userResult);
        }

        List<GitHubLookupUser> usersResult= futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("time : " + (System.currentTimeMillis() - start));

        return ResponseEntity.ok(usersResult);
    }

    @PostMapping("/api/v1/users/github/sync")
    public ResponseEntity<List<GitHubLookupUser>> getUsersSync(@RequestBody Set<String> users) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<GitHubLookupUser> usersResult = new ArrayList<>();
        for(String user : users) {
            GitHubLookupUser gitHubLookupUserResult = gitHubLookupService.findUserSync(user);
            usersResult.add(gitHubLookupUserResult);
        }
        log.info("time : " + (System.currentTimeMillis() - start));
        return ResponseEntity.ok(usersResult);
    }
}
