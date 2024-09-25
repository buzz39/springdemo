package com.example.springsessionredis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsessionredis.application.model.SessionConstants;
import com.example.springsessionredis.client.ClientClass;
import com.example.springsessionredis.dataaccess.GetSMSData;
import com.example.springsessionredis.dataaccess.LookupInitCDP;
import com.example.springsessionredis.service.ClientClassLocatorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/client")
public class ClientClassController {

	@Autowired
	private ClientClassLocatorService clientClassLocatorService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@GetMapping("/execute")
	public ResponseEntity<String> executeClientClass(@RequestParam String clientClassName,
			@RequestParam boolean useStub) {
		ClientClass clientClass = clientClassLocatorService.getClientClass(clientClassName, useStub);

		if (clientClass != null) {
			clientClass.execute(); // Dynamically invoke the client class method
			return ResponseEntity.ok("Execution successful.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client class not found.");
		}
	}

	@GetMapping("/executeGetSMSData")
	public ResponseEntity<String> executeGetSMSData(@RequestParam boolean useStub, HttpServletRequest request) {

		// Instantiate and execute GetSMSData
		GetSMSData getSMSData = new GetSMSData(redisTemplate);
		
		Map<String, String> smsData = getSMSData.execute(request);

		System.out.println(smsData.toString());

		return ResponseEntity.ok("Execution successful.");
	}
	
	@GetMapping("/executeLookupInitCDP")
	public ResponseEntity<String> executeLookupInitCDP(@RequestParam boolean useStub, HttpServletRequest request) {

		LookupInitCDP lookupInitCDP = new LookupInitCDP(redisTemplate);
		Map<String, String> initCDP = lookupInitCDP.execute(request);
		
		System.out.println(initCDP.toString());

		return ResponseEntity.ok("Execution successful.");
	}
	
	@GetMapping("/executeSetSessionValues")
	public ResponseEntity<String> executeSetSessionValues(@RequestParam boolean useStub, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(SessionConstants.APPLICATIONTAG, "adresse_aendernDSL");
        session.setAttribute(SessionConstants.BRANDCOLOR, "rot");
        session.setAttribute(SessionConstants.SMS_TEXT, "Lieber o2 Kunde, anbei ihr gewünschter Link, um Ihre Adresse in unserem Onlineportal selbst zu ändern:  dsl.o2online.de/kontaktdaten/?partnerId=Hotline&amp;medium=IVR_DSLrot&amp;keywordtext=kontaktdatenDSL   Ihr o2 Team.");

		return ResponseEntity.ok("Execution successful.");
	}
}
