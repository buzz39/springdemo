package com.example.springsessionredis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.springsessionredis.client.ClientClass;
import com.example.springsessionredis.model.MapParameter;
import com.example.springsessionredis.model.Parameter;

@Component
public class ClientClassConfigurationHandler implements ParameterHandler {

    private final Map<String, ClientClass> clientClassRegistry = new HashMap<>();  // Registry to store client class instances
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object handle(String paramName, Object paramValue) {
        MapParameter mapParameter = (MapParameter) paramValue;
        List<String> defaultImplementations = null;
        List<String> stubImplementations = null;

        // Extract 'default' and 'stub' class names from the parameter
        for (Parameter parameter : mapParameter.getParameters()) {
            if ("default".equalsIgnoreCase(parameter.getName())) {
                defaultImplementations = parameter.getList().getValues();
            } else if ("stub".equalsIgnoreCase(parameter.getName())) {
                stubImplementations = parameter.getList().getValues();
            }
        }

        // Dynamically load and register all default and stub classes
        processClientClasses(defaultImplementations, stubImplementations);

        return null;
    }

    // General method to dynamically load, instantiate, and register classes
    private void processClientClasses(List<String> defaultImpls, List<String> stubImpls) {
        if (defaultImpls != null) {
            loadAndRegisterClasses(defaultImpls, "Default");
        }
        if (stubImpls != null) {
            loadAndRegisterClasses(stubImpls, "Stub");
        }
    }

    // Dynamically load, instantiate, and register classes in the registry
    private void loadAndRegisterClasses(List<String> classNames, String implementationType) {
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                ClientClass instance = (ClientClass) clazz.getDeclaredConstructor().newInstance();
                clientClassRegistry.put(clazz.getSimpleName() + implementationType, instance);  // Register with "Default" or "Stub" suffix
                System.out.println("Loaded and registered " + clazz.getSimpleName() + " as " + implementationType);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to load class: " + className);
            }
        }
    }

    // Method to get the client class from the registry
    public ClientClass getClientClass(String clientClassName, String implementationType) {
        return clientClassRegistry.get(clientClassName + implementationType);
    }
}
