package com.example.springsessionredis.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import com.example.springsessionredis.application.model.SessionConstants;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/create")
    public String createSession(HttpSession session) {
        // Store a session attribute to activate the session
        session.setAttribute("user", "UserSessionData");

		/*
		 * session.setAttribute(SessionConstants.APPLICATIONTAG, "adresse_aendernDSL");
		 * session.setAttribute(SessionConstants.BRANDCOLOR, "rot");
		 * session.setAttribute(SessionConstants.SMS_TEXT,
		 * "Lieber o2 Kunde, anbei ihr gewünschter Link, um Ihre Adresse in unserem Onlineportal selbst zu ändern:  dsl.o2online.de/kontaktdaten/?partnerId=Hotline&amp;medium=IVR_DSLrot&amp;keywordtext=kontaktdatenDSL   Ihr o2 Team."
		 * );
		 */
        
        // Return the default session ID provided by HttpSession
        return "Session created successfully. Session ID: " + session.getId();
    }
}
