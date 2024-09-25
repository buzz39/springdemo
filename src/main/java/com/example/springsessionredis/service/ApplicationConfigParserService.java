package com.example.springsessionredis.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.example.springsessionredis.model.ApplicationConfig;
import com.example.springsessionredis.model.Parameter;
import com.example.springsessionredis.model.ParametersWrapper;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@Service
public class ApplicationConfigParserService {

    @Value("${application-config.xml.file.base-path:classpath:/}")
    private String basePath;  // Base path for XML files

    private final HandlerRegistry handlerRegistry;
    private final ResourceLoader resourceLoader;
    private final Environment environment;  // Environment to get the active profile

    // Constructor injection for the registry, resource loader, and environment
    public ApplicationConfigParserService(HandlerRegistry handlerRegistry, ResourceLoader resourceLoader, Environment environment) {
        this.handlerRegistry = handlerRegistry;
        this.resourceLoader = resourceLoader;
        this.environment = environment;
    }

    // Ensure this method runs after the application starts
    @PostConstruct
    public void loadAndProcessXML() throws JAXBException, IOException {
        // Determine the active profile
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = (activeProfiles.length > 0) ? activeProfiles[0] : "default";

        // If the profile is "default", load application-config.xml, otherwise load profile-specific XML
        String profileSpecificPath;
        if ("default".equals(activeProfile)) {
            profileSpecificPath = "classpath:application-config.xml";  // Load the base configuration file for "default"
        } else {
            profileSpecificPath = "classpath:application-config-" + activeProfile + ".xml";
        }

        // Load and parse the XML file
        Resource xmlResource = getResourceFromPath(profileSpecificPath);

        if (xmlResource == null || !xmlResource.exists()) {
            throw new IOException("XML resource not found at: " + profileSpecificPath);
        }

        // Create JAXB context and unmarshaller
        JAXBContext context = JAXBContext.newInstance(ApplicationConfig.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ApplicationConfig config = (ApplicationConfig) unmarshaller.unmarshal(xmlResource.getInputStream());

        // Process the parameters from the XML
        processParameters(config.getParametersWrapper());
    }

    private void processParameters(ParametersWrapper parametersWrapper) {
    	List<Parameter> parameterList = parametersWrapper.getParameterList();
        for (Parameter parameter : parameterList) {
            // Delegate handling to appropriate handler
            if (parameter.getName() != null) {
                handlerRegistry.getHandler(parameter.getName()).handle(parameter.getName(), parameter.getMap());
            }
        }
    }

    // Load the resource from the given path
    private Resource getResourceFromPath(String path) {
        return resourceLoader.getResource(path);
    }
}
