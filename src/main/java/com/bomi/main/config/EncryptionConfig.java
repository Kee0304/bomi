package com.bomi.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class EncryptionConfig {

    @Value("${spring.security.jwt.encryption}")
    private String encryption;

    @Value("${spring.security.jwt.salt}")
    private String salt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(encryption, salt);
    }
}
