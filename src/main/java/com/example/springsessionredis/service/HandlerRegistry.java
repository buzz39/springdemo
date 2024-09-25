package com.example.springsessionredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HandlerRegistry {

    private final Map<String, ParameterHandler> handlerMap = new HashMap<>();

    @Autowired
    public HandlerRegistry(LoggingBasePathHandler loggingBasePathHandler, 
                           ParameterMapHandler parameterMapHandler,
                           ServiceReferenceHandler serviceReferenceHandler,
                           ClientClassConfigurationHandler clientClassConfigurationHandler) {
        handlerMap.put("logging_base_path", loggingBasePathHandler);
        handlerMap.put("dataaccess.configuration", parameterMapHandler);
        handlerMap.put("module.configuration", parameterMapHandler);
        handlerMap.put("sms", serviceReferenceHandler);
        handlerMap.put("brandcolor", serviceReferenceHandler);
        handlerMap.put("exittags", serviceReferenceHandler);
        handlerMap.put("ClientClassConfiguration", clientClassConfigurationHandler); // Register the new handler

    }

    public ParameterHandler getHandler(String paramName) {
        return handlerMap.get(paramName);
    }
}
