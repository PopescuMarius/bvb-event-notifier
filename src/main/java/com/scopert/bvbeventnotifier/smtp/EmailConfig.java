package com.scopert.bvbeventnotifier.smtp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public Properties emailProperties() {
        Properties emailProperties = new Properties();
        try (InputStream input = EmailConfig.class.getClassLoader().getResourceAsStream("email.properties")) {
            emailProperties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Application will not start if it is unable to send emails", ex);
        }
        return emailProperties;
    }

}
