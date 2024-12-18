package com.hodolog.config;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hodolman")
public class AppConfig {

    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Decoders.BASE64.decode(jwtKey);
    }

    public byte[] getJwtKey() {
        return jwtKey;
    }
}
