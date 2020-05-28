package com.example.user.controller;

import com.example.user.domain.User;
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
    public ResponseEntity<List<User>> getUsers(@RequestBody Set<String> users) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<CompletableFuture<User>> futureList = new ArrayList<>();
        for(String user : users) {
            CompletableFuture<User> userResult = gitHubLookupService.findUser(user);
            futureList.add(userResult);
        }

        List<User> usersResult= futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("time : " + (System.currentTimeMillis() - start));

        return ResponseEntity.ok(usersResult);
    }

    @PostMapping("/api/v1/users/github/sync")
    public ResponseEntity<List<User>> getUsersSync(@RequestBody Set<String> users) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<User> usersResult = new ArrayList<>();
        for(String user : users) {
            User userResult = gitHubLookupService.findUserSync(user);
            usersResult.add(userResult);
        }
        log.info("time : " + (System.currentTimeMillis() - start));
        return ResponseEntity.ok(usersResult);
    }
}
