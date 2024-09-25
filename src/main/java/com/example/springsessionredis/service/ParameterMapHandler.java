package com.example.springsessionredis.service;

import com.example.springsessionredis.model.MapParameter;
import com.example.springsessionredis.model.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ParameterMapHandler implements ParameterHandler {

	private final StringRedisTemplate redisTemplate;

	@Autowired
    public ParameterMapHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
    @Override
    public Object handle(String paramName, Object paramValue) {
    	
        // Handle complex map structure with nested parameters
        MapParameter mapParameter = (MapParameter) paramValue;  // Cast to MapParameter
        List<Parameter> nestedParameters = mapParameter.getParameters();  // Get the nested parameters
        
        StringBuilder result = new StringBuilder("Configuration for " + paramName + ":\n");
        for (Parameter nestedParam : nestedParameters) {
            result.append(nestedParam.getName()).append(" = ").append(nestedParam.getValue()).append("\n");
        }
        
        Map<String, String> serviceDetails = ((MapParameter) paramValue).getParameters().stream()
                .collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
        
        redisTemplate.opsForHash().putAll("service:"+paramName, serviceDetails);
        
        
        
        return result.toString();
    }
}
