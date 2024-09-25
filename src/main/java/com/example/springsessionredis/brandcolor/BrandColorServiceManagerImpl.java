package com.example.springsessionredis.brandcolor;

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
 * This service initializes the BrandColor Data Manager. 
 * 
 */
@Component
public class BrandColorServiceManagerImpl {

    // SLF4J Logger (Spring Boot default logging)
    private static final Logger logger = LoggerFactory.getLogger(BrandColorServiceManagerImpl.class);

    public static final String BRANDCOLOR_FILE_KEY = "DataFile";
    public static final String BRANDCOLOR_FILE_EXTERNAL_KEY = "DataFileExternal";
    public static final String BRANDCOLOR_UPDATE_FREQUENCY_KEY = "UpdateFrequency";
    public static final String RESOURCE_LOCATION_KEY = "resourceLocation";
    private static final String CONTEXT_KEY = "classpath:";

    private static boolean isConfigured = false;

    private final ResourceLoader resourceLoader;

    @Autowired
    public BrandColorServiceManagerImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        logger.debug("BrandColorServiceManagerImpl: Initialized BrandColor Service");
    }

    /*
     * Check if the service is configured
     */
    public boolean isConfigured() {
        return isConfigured;
    }

    /**
     * Initializes the BrandColor service configuration
     */
    @SuppressWarnings("rawtypes")
    public void initConfiguration(Map serviceConfigMap) {
        logger.debug("Initializing BrandColor Service configuration");
        try {
            // Get resource location from configuration or use CONTEXT_KEY as default
            String resourceLocation = (String) serviceConfigMap.getOrDefault(RESOURCE_LOCATION_KEY, CONTEXT_KEY);

            // Read the main BrandColorData file from the configuration
            Object dataFile = serviceConfigMap.get(BRANDCOLOR_FILE_KEY);
            if (dataFile != null) {
                String filename = (String) dataFile;
                loadBrandColorData(resourceLocation, filename);
            }

            // Read an optional external BrandColorData file from the configuration
            Object externalDataFile = serviceConfigMap.get(BRANDCOLOR_FILE_EXTERNAL_KEY);
            if (externalDataFile != null) {
                String filename = (String) externalDataFile;
                loadBrandColorData(resourceLocation, filename);
            }

            isConfigured = true;
        } catch (Exception e) {
            logger.error("Error while loading BrandColor XML files.", e);
            throw new BeanInitializationException("Error loading BrandColor configuration.", e);
        }
    }

    /**
     * Loads BrandColor data from a file and initializes the BrandColorDataManager
     */
    private void loadBrandColorData(String resourceLocation, String filename) {
        logger.debug("Loading BrandColor data from resource location: {} and filename: {}", resourceLocation, filename);

        try {
            // Construct the resource path and load the file using ResourceLoader
            String resourcePath = resourceLocation + "/" + filename;
            Resource resource = resourceLoader.getResource(resourcePath);

            if (resource.exists() && resource.isReadable()) {
                try (InputStream in = resource.getInputStream()) {
                    ChangingColors colors = ChangingColorsLoader.load(in);
                    BrandColorDataManager.getInstance().set(colors);
                    logger.debug("BrandColor data loaded from file: {}", filename);
                }
            } else {
                logger.warn("BrandColor data file not found: {}", filename);
            }
        } catch (Exception e) {
            logger.error("Error loading BrandColor data file: {}", filename, e);
        }
    }
}
