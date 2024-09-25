package com.example.springsessionredis.service;

public interface ParameterHandler {
    // Process the parameter value and return a meaningful result (depending on type)
    Object handle(String paramName, Object paramValue);
}
