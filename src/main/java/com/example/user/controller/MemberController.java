package com.example.user.controller;

import com.example.user.payload.GitHubLookupUser;
import com.example.user.payload.UserModel;
import com.example.user.service.GitHubLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MemberController {
    private final GitHubLookupService gitHubLookupService;

    public MemberController(GitHubLookupService service) {
        this.gitHubLookupService = service;
    }

    @CrossOrigin(origins = {"http://localhost:18080"}) // 명시한 origin에서 현재 서버의 아래 URI 요청을 허용한다. Controller 레벨에서도 설정 가능.
    @PostMapping("/api/v1/users/github/async")
    public ResponseEntity<List<GitHubLookupUser>> getUsers(@RequestBody Set<String> users) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<CompletableFuture<GitHubLookupUser>> futureList = new ArrayList<>();
        for(String user : users) {
            CompletableFuture<GitHubLookupUser> userResult = gitHubLookupService.findUserAsync(user);
            futureList.add(userResult);
        }

        List<GitHubLookupUser> usersResult= futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
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

    @PostMapping("/api/v1/user/test")
    public ResponseEntity<UserModel> testUser(@RequestBody UserModel user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/v1/login/oauth2/github/callback")
    public ResponseEntity<?> githubLogin() {
        return ResponseEntity.ok(null);
    }
}
