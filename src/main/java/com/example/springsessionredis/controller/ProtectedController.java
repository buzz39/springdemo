package com.example.springsessionredis.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import com.example.springsessionredis.sms.SMSDataManager;

@RestController
@RequestMapping("/protected")
public class ProtectedController {

    @GetMapping("/resource")
    public String accessProtectedResource(HttpServletRequest request) {
        // Get the session only if it exists, do not create a new session
        HttpSession session = request.getSession(false);
        
        
        String text = SMSDataManager.getInstance().getText("adresse_aendernDSL", "rot");
        System.out.println("SMS Text: "+text);

        if (session == null) {
            System.out.println("No session found in ProtectedController.");
            return "Invalid session.";
        }

        System.out.println("Session ID in ProtectedController: " + session.getId());
        return "Access granted to protected resource!";
    }
}
