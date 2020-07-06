package com.example.user.controller;

import com.example.user.config.security.filters.JwtAuthorizationFilter;
import com.example.user.service.GitHubLookupService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//http://blog.devenjoy.com/?p=524
@RunWith(SpringRunner.class)
@WebMvcTest(value = {MemberController.class}, useDefaultFilters = false)
@OverrideAutoConfiguration(enabled=true) // security config loading
public class ControllerTest {
    @Rule
    OutputCaptureRule outputCapture = new OutputCaptureRule(); // 로그나 콘솔에 print 찍히는 내용도 테스트 가능

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubLookupService service;

    @MockBean
    private JwtAuthorizationFilter filter;

    @Test
    public void testUser() throws Exception {
        // TODO: filter 인증로직 안타도록 해야함.
        String json = "{\"email\": \"jisoo@naver.com\", \"password\": \"1234\"}";
        mockMvc.perform(post("/api/v1/user/test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(equalTo("jisoo@naver.com"))))
                .andExpect(jsonPath("$.password", is(equalTo("1234"))));
    }
}
