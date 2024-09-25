package com.example.springsessionredis.service;

import org.springframework.stereotype.Component;

@Component  
public class LoggingBasePathHandler implements ParameterHandler {

    @Override
    public Object handle(String paramName, Object paramValue) {
        return "Logging Base Path: " + paramValue;
    }
}
