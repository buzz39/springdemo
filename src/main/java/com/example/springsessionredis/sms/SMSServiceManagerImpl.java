package com.example.springsessionredis.sms;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * This service initializes the SMS Data Manager. 
 * 
 */
@Component
public class SMSServiceManagerImpl {

    // SLF4J Logger (Spring Boot default logging)
    private static final Logger logger = LoggerFactory.getLogger(SMSServiceManagerImpl.class);

    public static final String SMS_FILE_KEY = "DataFile";
    public static final String SMS_FILE_EXTERNAL_KEY = "DataFileExternal";
    public static final String SMS_UPDATE_FREQUENCY_KEY = "UpdateFrequency";
    public static final String RESOURCE_LOCATION_KEY = "resourceLocation";
    private static final String CONTEXT_KEY = "classpath:";

    private static boolean isConfigured = false;

    private final ResourceLoader resourceLoader;

    @Autowired
    public SMSServiceManagerImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        logger.debug("SMSServiceManagerImpl: Initialized SMS Service");
    }

    /*
     * Check if the service is configured
     */
    public boolean isConfigured() {
        return isConfigured;
    }

    /**
     * Initializes the SMS service configuration
     */
    @SuppressWarnings("rawtypes")
    public void initConfiguration(Map serviceConfigMap) {
        logger.debug("Initializing SMS Service configuration");
        try {
            // Get resource location from configuration or use CONTEXT_KEY as default
            String resourceLocation = (String) serviceConfigMap.getOrDefault(RESOURCE_LOCATION_KEY, CONTEXT_KEY);

            // Read the main SMSData file from the configuration
            Object dataFile = serviceConfigMap.get(SMS_FILE_KEY);
            if (dataFile != null) {
                String filename = (String) dataFile;
                loadSMSData(resourceLocation, filename);
            }

            // Read an optional external SMSData file from the configuration
            Object externalDataFile = serviceConfigMap.get(SMS_FILE_EXTERNAL_KEY);
            if (externalDataFile != null) {
                String filename = (String) externalDataFile;
                loadSMSData(resourceLocation, filename);
            }

            isConfigured = true;
        } catch (Exception e) {
            logger.error("Error while loading SMS XML files.", e);
            throw new BeanInitializationException("Error loading SMS configuration.", e);
        }
    }

    /**
     * Loads SMS data from a file and initializes the SMSDataManager
     */
    private void loadSMSData(String resourceLocation, String filename) {
        logger.debug("Loading SMS data from resource location: {} and filename: {}", resourceLocation, filename);

        try {
            // Construct the resource path and load the file using ResourceLoader
            String resourcePath = resourceLocation + "" + filename;
            Resource resource = resourceLoader.getResource(resourcePath);

            if (resource.exists() && resource.isReadable()) {
                try (InputStream in = resource.getInputStream()) {
                    SMSData smsData = SMSDataLoader.load(in);
                    SMSDataManager.getInstance().set(smsData);
                    logger.debug("SMS data loaded from file: {}", filename);
                }
            } else {
                logger.warn("SMS data file not found: {}", filename);
            }
        } catch (Exception e) {
            logger.error("Error loading SMS data file: {}", filename, e);
        }
    }
}
