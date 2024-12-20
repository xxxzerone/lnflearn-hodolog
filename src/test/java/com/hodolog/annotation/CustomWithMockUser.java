package com.hodolog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserFactory.class)
public @interface CustomWithMockUser {

    String username() default "hodol@gmail.com";

    String password() default "1234";

    int level() default 5;

    String mobilNumber = "01000000000";
}
