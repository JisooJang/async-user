package com.example.user.controller;

import com.example.user.payload.GitHubLookupUser;
import com.example.user.payload.UserModel;
import com.example.user.service.GitHubLookupService;
import com.example.user.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class MemberController {
    private final GitHubLookupService gitHubLookupService;
    private final MemberService memberService;

    public MemberController(GitHubLookupService gitHubLookupService, MemberService memberService) {
        this.gitHubLookupService = gitHubLookupService;
        this.memberService = memberService;
    }

    @CrossOrigin(origins = {"http://localhost:18080"}) // 명시한 origin에서 현재 서버의 아래 URI 요청을 허용한다. Controller 레벨에서도 설정 가능.
    @PostMapping("/github/async")
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

    @PostMapping("/github/sync")
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

    @PostMapping("/test")
    public ResponseEntity<UserModel> testUser(@RequestBody UserModel user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login/oauth2/github")
    public ResponseEntity<?> getGithubLoginURL(HttpServletRequest request) {
        // 1. Request a user's GitHub identity
        String sessionId = request.getSession().getId();
        return ResponseEntity.ok(gitHubLookupService.getGithubLoginURL(sessionId));
    }

    @GetMapping("/login/oauth2/github/callback")
    public ResponseEntity<?> githubLoginCallback(HttpServletRequest request, @RequestParam String code, @RequestParam String state) {
        String sessionId = request.getSession().getId();
        String accessToken = gitHubLookupService.getGithubAccessToken(sessionId, code, state);
        return ResponseEntity.ok(accessToken);
    }


}
