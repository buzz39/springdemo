package com.example.springsessionredis.exittags;

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
 * This service initializes the ExitTag Data Manager.
 */
@Component
public class ExitTagServiceManagerImpl {

    // SLF4J Logger (Spring Boot default logging)
    private static final Logger logger = LoggerFactory.getLogger(ExitTagServiceManagerImpl.class);

    public static final String EXITTAG_FILE_KEY = "DataFile";
    public static final String EXITTAG_FILE_EXTERNAL_KEY = "DataFileExternal";
    public static final String RESOURCE_LOCATION_KEY = "resourceLocation";
    private static final String CONTEXT_KEY = "classpath:";

    private static boolean isConfigured = false;

    private final ResourceLoader resourceLoader;

    @Autowired
    public ExitTagServiceManagerImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        logger.debug("ExitTagServiceManagerImpl: Initialized ExitTag Service");
    }

    /*
     * Check if the service is configured
     */
    public boolean isConfigured() {
        return isConfigured;
    }

    /**
     * Initializes the ExitTag service configuration
     */
    @SuppressWarnings("rawtypes")
    public void initConfiguration(Map serviceConfigMap) {
        logger.debug("Initializing ExitTag Service configuration");
        try {
            // Get resource location from configuration or use CONTEXT_KEY as default
            String resourceLocation = (String) serviceConfigMap.getOrDefault(RESOURCE_LOCATION_KEY, CONTEXT_KEY);

            // Read the main ExitTagData file from the configuration
            Object dataFile = serviceConfigMap.get(EXITTAG_FILE_KEY);
            if (dataFile != null) {
                String filename = (String) dataFile;
                loadExitTagData(resourceLocation, filename);
            }

            // Read an optional external ExitTagData file from the configuration
            Object externalDataFile = serviceConfigMap.get(EXITTAG_FILE_EXTERNAL_KEY);
            if (externalDataFile != null) {
                String filename = (String) externalDataFile;
                loadExitTagData(resourceLocation, filename);
            }

            isConfigured = true;
        } catch (Exception e) {
            logger.error("Error while loading ExitTag XML files.", e);
            throw new BeanInitializationException("Error loading ExitTag configuration.", e);
        }
    }

    /**
     * Loads ExitTag data from a file and initializes the ExitTagDataManager
     */
    private void loadExitTagData(String resourceLocation, String filename) {
        logger.debug("Loading ExitTag data from resource location: {} and filename: {}", resourceLocation, filename);

        try {
            // Construct the resource path and load the file using ResourceLoader
            String resourcePath = resourceLocation + "/" + filename;
            Resource resource = resourceLoader.getResource(resourcePath);

            if (resource.exists() && resource.isReadable()) {
                try (InputStream in = resource.getInputStream()) {
                    ExitTags exitTags = ExitTagsLoader.load(in);
                    ExitTagDataManager.getInstance().set(exitTags);
                    logger.debug("ExitTag data loaded from file: {}", filename);
                }
            } else {
                logger.warn("ExitTag data file not found: {}", filename);
            }
        } catch (Exception e) {
            logger.error("Error loading ExitTag data file: {}", filename, e);
        }
    }
}
