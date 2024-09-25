package com.example.springsessionredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.example.springsessionredis.brandcolor.BrandColorServiceManagerImpl;
import com.example.springsessionredis.exittags.ExitTagServiceManagerImpl;
import com.example.springsessionredis.model.ApplicationConfig;
import com.example.springsessionredis.model.MapParameter;
import com.example.springsessionredis.model.Parameter;
import com.example.springsessionredis.sms.SMSServiceManagerImpl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Component
public class ServiceReferenceHandler implements ParameterHandler {

    private final Map<String, Object> loadedServices = new HashMap<>(); // Cache for class instances
    private final StringRedisTemplate redisTemplate;
    private final ApplicationContext applicationContext; // Inject the Spring ApplicationContext
    private final ResourceLoader resourceLoader; // To dynamically load XML files
    private final TaskScheduler taskScheduler; // For scheduling cache refresh tasks
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(); // To track service tasks

    @Autowired
    public ServiceReferenceHandler(StringRedisTemplate redisTemplate, ApplicationContext applicationContext, 
                                   ResourceLoader resourceLoader, TaskScheduler taskScheduler) {
        this.redisTemplate = redisTemplate;
        this.applicationContext = applicationContext;
        this.resourceLoader = resourceLoader;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public Object handle(String paramName, Object paramValue) {
        Map<String, String> serviceDetails = ((MapParameter) paramValue).getParameters().stream()
                .collect(Collectors.toMap(Parameter::getName, Parameter::getValue));

        String className = serviceDetails.get("className");
        String configFilePath = serviceDetails.get("serviceFileName");

        if (!loadedServices.containsKey(className)) {
            try {
                // Load the Spring-managed service instance
                Object serviceInstance = loadServiceClassFromContext(className);
                loadedServices.put(paramName, serviceInstance);

                // Dynamically load and process the XML configuration file
                Map<String, Object> configMap = loadAndProcessXML(configFilePath);

                // Process cache refresh based on UpdateFrequency (from config file)
                if (configMap.containsKey("UpdateFrequency")) {
                    Integer updateFrequency = (Integer) configMap.get("UpdateFrequency");
                    scheduleCacheRefresh(paramName, updateFrequency);
                }

                // Store the configuration in Redis
                storeInRedis(paramName, configMap);

                System.out.println("Loaded and ran service configuration for: " + className);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return loadedServices.get(className);
    }

    // Schedule cache refresh for each service based on its UpdateFrequency
    private void scheduleCacheRefresh(String serviceName, int updateFrequency) {
        // If a task is already scheduled for this service, cancel it
        if (scheduledTasks.containsKey(serviceName)) {
            scheduledTasks.get(serviceName).cancel(false);
        }

        long refreshIntervalInMillis = updateFrequency * 1000L;  // Convert seconds to milliseconds
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
            // Logic for refreshing the cache
            System.out.println("Refreshing cache for service: " + serviceName + " at interval: " + updateFrequency + " seconds");
            refreshServiceCache(serviceName);  // Custom method to refresh service-specific cache
        }, refreshIntervalInMillis);

        // Store the scheduled task for future reference
        scheduledTasks.put(serviceName, scheduledTask);
    }

    // Method to refresh cache for a specific service
    private void refreshServiceCache(String serviceName) {
    	if(serviceName.equalsIgnoreCase("sms")) {
    		Object object = loadedServices.get("sms");
    		if(object instanceof SMSServiceManagerImpl) {
    			SMSServiceManagerImpl smsService = (SMSServiceManagerImpl) object;
    			Map<Object, Object> entries = redisTemplate.opsForHash().entries("service:sms");
				smsService.initConfiguration(entries);
    		}
    	} else if(serviceName.equalsIgnoreCase("brandcolor")) {
    		Object object = loadedServices.get("brandcolor");
    		if(object instanceof SMSServiceManagerImpl) {
    			BrandColorServiceManagerImpl smsService = (BrandColorServiceManagerImpl) object;
    			Map<Object, Object> entries = redisTemplate.opsForHash().entries("service:brandcolor");
				smsService.initConfiguration(entries);
    		}
    	} else if(serviceName.equalsIgnoreCase("exittags")) {
    		Object object = loadedServices.get("exittags");
    		if(object instanceof SMSServiceManagerImpl) {
    			ExitTagServiceManagerImpl smsService = (ExitTagServiceManagerImpl) object;
    			Map<Object, Object> entries = redisTemplate.opsForHash().entries("service:exittags");
				smsService.initConfiguration(entries);
    		}
    	}
    	
        System.out.println("Cache refreshed for service: " + serviceName);
    }

    // Dynamically load and process the XML configuration file
    private Map<String, Object> loadAndProcessXML(String configFilePath) throws JAXBException, Exception {
        File configFile = Paths.get("src", "main", "resources", configFilePath).toFile();

        if (!configFile.exists() || !configFile.canRead()) {
            throw new Exception("Configuration file not found or not readable: " + configFile.getAbsolutePath());
        }

        try (InputStream inputStream = new FileInputStream(configFile)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(ApplicationConfig.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ApplicationConfig serviceConfig = (ApplicationConfig) unmarshaller.unmarshal(inputStream);

            Map<String, Object> configMap = new HashMap<>();
            processParameters(serviceConfig.getParameters(), configMap);

            return configMap;
        }
    }

    // Helper method to process parameters and populate the configMap
    private void processParameters(List<Parameter> parameterList, Map<String, Object> configMap) {
        for (Parameter parameter : parameterList) {
            switch (parameter.getName()) {
                case "DataFile":
                    configMap.put("DataFile", parameter.getValue());
                    break;
                case "DataFileExternal":
                    configMap.put("DataFileExternal", parameter.getValue());
                    break;
                case "UpdateFrequency":
                    configMap.put("UpdateFrequency", Integer.parseInt(parameter.getValue()));
                    break;
                default:
                    // Handle other parameters if necessary
                    break;
            }
        }
    }

    // Get the Spring-managed bean from the ApplicationContext
    private Object loadServiceClassFromContext(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return applicationContext.getBean(clazz); // Get the bean from the Spring context
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Service class not found: " + className, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load service class from Spring context: " + className, e);
        }
    }

    // Store the service configuration in Redis
    private void storeInRedis(String serviceName, Map<String, Object> configMap) {
        String redisKey = "service:" + serviceName;

        // Convert all values in configMap to String
        Map<String, String> stringConfigMap = configMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue()) // Convert value to String
                ));

        // Store the configuration as a hash in Redis
        redisTemplate.opsForHash().putAll(redisKey, stringConfigMap);

        System.out.println("Stored configuration for service: " + serviceName + " in Redis under key: " + redisKey);
    }
}
