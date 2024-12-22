package com.hodolog.config;

import com.hodolog.domain.User;
import com.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class HodologMockSecurityContext implements WithSecurityContextFactory<HodologMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(HodologMockUser annotation) {
        User user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();
        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_ADMIN");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                user.getPassword(),
                List.of(role));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
