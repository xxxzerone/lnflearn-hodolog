package com.hodolog.service;

import com.hodolog.crypto.PasswordEncoder;
import com.hodolog.crypto.ScryptPasswordEncoder;
import com.hodolog.domain.User;
import com.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        PasswordEncoder encoder = new ScryptPasswordEncoder();
        Signup signup = Signup.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("hodolman")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("abc@gmail.com", user.getEmail());
//        assertNotNull(user.getPassword());
//        assertEquals("1234", user.getPassword());
        assertTrue(encoder.matches("1234", user.getPassword()));
        assertEquals("hodolman", user.getName());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("hodol")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("hodolman")
                .build();

        // when
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }

}