package com.example.springsessionredis.interceptor;

import com.example.springsessionredis.listener.CustomSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    @Bean
    public ServletListenerRegistrationBean<CustomSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new CustomSessionListener());
    }
}
