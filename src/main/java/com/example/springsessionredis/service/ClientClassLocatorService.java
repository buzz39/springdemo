package com.example.springsessionredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsessionredis.client.ClientClass;

@Service
public class ClientClassLocatorService {

    @Autowired
    private ClientClassConfigurationHandler clientClassConfigurationHandler;

    // Method to retrieve the correct client class (default or stub) dynamically
    public ClientClass getClientClass(String clientClassName, boolean useStub) {
        String implementationType = useStub ? "Stub" : "Default";  // Decide which implementation to use
        ClientClass clientClass = clientClassConfigurationHandler.getClientClass(clientClassName, implementationType);

        if (clientClass == null) {
            throw new IllegalArgumentException("Client class not found: " + clientClassName);
        }

        return clientClass;
    }
}
