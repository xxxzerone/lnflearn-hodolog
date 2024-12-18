package com.hodolog.service;

import com.hodolog.crypto.ScryptPasswordEncoder;
import com.hodolog.domain.User;
import com.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.exception.InvalidSignInInformation;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Login;
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
        assertNotNull(user.getPassword());
        assertEquals("1234", user.getPassword());
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

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("abc@gmail.com")
                .password(encryptedPassword)
                .name("hodol")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signIn(login);

        // then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인 비밀번호 틀림")
    void test4() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        Signup signup = Signup.builder()
                .email("abc@gmail.com")
                .password(encryptedPassword)
                .name("hodolman")
                .build();
        authService.signup(signup);

        Login login = Login.builder()
                .email("abc@gmail.com")
                .password("5678")
                .build();

        // expected
        assertThrows(InvalidSignInInformation.class, () -> authService.signIn(login));
    }
}