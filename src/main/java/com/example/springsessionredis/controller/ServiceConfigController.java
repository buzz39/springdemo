package com.example.springsessionredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsessionredis.brandcolor.BrandColorDataManager;
import com.example.springsessionredis.exittags.ExitTagDataManager;
import com.example.springsessionredis.exittags.ExitTagServiceManagerImpl;
import com.example.springsessionredis.sms.SMSDataManager;

import java.util.Map;

@RestController
public class ServiceConfigController {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public ServiceConfigController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Endpoint to fetch service configuration from Redis
    @GetMapping("/config/{serviceName}")
    public Map<Object, Object> getServiceConfig(@PathVariable String serviceName) {
        // Construct the Redis key using the service name (without profile, as only one is stored)
        String redisKey = "service:" + serviceName;

        // Fetch the configuration from Redis stored as a hash
        Map<Object, Object> serviceConfig = redisTemplate.opsForHash().entries("service:brandcolor");

        SMSDataManager.getInstance().getText("", redisKey);
        BrandColorDataManager.getInstance();
        ExitTagDataManager.getInstance();
        // Return the configuration map
        return serviceConfig;
    }
}
