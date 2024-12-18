package com.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.domain.Session;
import com.hodolog.domain.User;
import com.hodolog.repository.SessionRepository;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Login;
import com.hodolog.request.Signup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {
        // given
        userRepository.save(User.builder()
                .name("호돌맨")
                .email("abc@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공후 세션 1개 성공")
    void test2() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("호돌맨")
                .email("abc@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공후 세션 응답")
    void test3() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("호돌맨")
                .email("abc@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .name("호돌맨")
                .email("abc@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .name("호돌맨")
                .email("abc@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-other")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        // given
        Signup signup = Signup.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("hodolman")
                .build();

        // expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}