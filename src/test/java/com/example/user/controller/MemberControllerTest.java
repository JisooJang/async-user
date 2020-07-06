package com.example.user.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    @Autowired
    WebTestClient webTestClient;

    public void test() throws Exception {
        webTestClient.post().uri("/api/v1/user/test").accept(MediaType.APPLICATION_JSON)
                .attribute("email", "jisoo@naver.com").attribute("password", "1234qwer!")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("");
    }
}
