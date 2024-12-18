package com.hodolog.service;

import com.hodolog.crypto.PasswordEncoder;
import com.hodolog.crypto.ScryptPasswordEncoder;
import com.hodolog.domain.User;
import com.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.exception.InvalidSignInInformation;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Login;
import com.hodolog.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signIn(Login login) {
//        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSignInInformation::new);
//        Session session = user.addSession();
//        return session.getAccessToken();

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSignInInformation::new);

        boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
        if (!matches) {
            throw new InvalidSignInInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        User user = User.builder()
                .email(signup.getEmail())
                .password(encryptedPassword)
                .name(signup.getName())
                .build();
        userRepository.save(user);
    }
}
