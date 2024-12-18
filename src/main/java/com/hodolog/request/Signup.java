package com.hodolog.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Signup {

    private String email;
    private String password;
    private String name;
}
