package com.hodolog.annotation;

import com.hodolog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    private UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        String username = annotation.username();
        int level = annotation.level();
//        userRepository.save();
        return new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return null;
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        };
    }
}
