/*
 * ---------------------------------------------------------------------------
 *
 * COPYRIGHT (c) 2016 Nuance Communications Inc.
 *
 * All Rights Reserved. Nuance Confidential.
 *
 * The copyright to the computer program(s) herein is the property of
 * Nuance Communications Inc. The program(s) may be used and/or copied
 * only with the written permission from Nuance Communications Inc.
 * or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 *
 *
 * ---------------------------------------------------------------------------
 */
package com.example.springsessionredis.dataaccess;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.example.springsessionredis.application.model.AbstractClientDataAccessBusinessFunction;
import com.example.springsessionredis.application.model.GenericUtility;
import com.example.springsessionredis.application.model.ReportingManager;
import com.example.springsessionredis.application.model.SessionConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Sending the SMS to the backend.
 * 
 * @author Nuance Communications
 */
public class SendSMS extends AbstractClientDataAccessBusinessFunction {
	public SendSMS(StringRedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	private static final String SMS_POST = "SMS_POST";
	public static final int STATUS_ONE = 1;
	private static String className = SendSMS.class.getName();
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(className);
	
	public Map<String, String> execute(HttpServletRequest requestDataContext) {
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Entering method.");
		Map<String, String> hashMap = new HashMap<String, String>();
		String returnCode = "success";

		HttpSession session = requestDataContext.getSession();
		String smsText      = (String) session.getAttribute(SessionConstants.SMS_TEXT);
		String appSessionId = getString(requestDataContext, SessionConstants.UR_FIRST_CONNID);
		String urClir       = (String) session.getAttribute(SessionConstants.UR_CLIR);
		String ivrCli       = (String) session.getAttribute(SessionConstants.IVR_CLI);
		String ivrCliType   = (String) session.getAttribute(SessionConstants.IVR_CLI_TYPE);
		String ivrIdValue   = (String) session.getAttribute(SessionConstants.IVR_ID_VALUE);
		String ivrIdType    = (String) session.getAttribute(SessionConstants.IVR_ID_TYPE);
		
		// logger.info(className+".execute: UR_CLIR[" + urClir + "]");
		logger.info(className+".execute: IVR_CLI[" + ivrCli + "]");
		logger.info(className+".execute: IVR_CLI_TYPE[" + ivrCliType + "]");
		logger.info(className+".execute: IVR_ID_VALUE[" + ivrIdValue + "]");
		logger.info(className+".execute: IVR_ID_TYPE[" + ivrIdType + "]");
		
		// Define msisdn
		String msisdn = "";
		if ("MSISDN".equalsIgnoreCase(ivrCliType)) {
			msisdn = ivrCli;
		}
		else if ("MSISDN".equalsIgnoreCase(ivrIdType) && !"MSISDN".equalsIgnoreCase(ivrCliType)) {
			msisdn = ivrIdValue;
		}
		
		ReportingManager rm = (ReportingManager)session.getAttribute(SessionConstants.REPORTING_MANAGER);
        String applicationTag  = (String) session.getAttribute(SessionConstants.APPLICATIONTAG);
        String brandColor      = (String) session.getAttribute(SessionConstants.BRANDCOLOR);
        
		// check if number is msisdn
		if (!msisdn.startsWith("4915") && !msisdn.startsWith("4916") && !msisdn.startsWith("4917")) {
			logger.warn(className+".execute: number is not mobile (msisdn is: " + msisdn + "), cannot send SMS");
		} else {
			try {
				// preparing sending
				String baseURL = getDataAccessLayerURL(requestDataContext);
				String lookupURL = getLookupURL(requestDataContext);
				String finalURL = baseURL + lookupURL;
	
				if (logger.isDebugEnabled()) {
				    logger.debug(className+".execute: dataAccessLayerURL = [" + baseURL + "]");
				    logger.debug(className+".execute: lookupURL = [" + lookupURL + "]");
				    logger.debug(className+".execute: finalURL = [" + finalURL + "]");
				}
				
				logger.info(className+".execute: Sending SMS to msisdn = [" + msisdn + "] smsText = ["+smsText+"]");
				
				// sending the SMS
				if (smsText!=null && msisdn!=null) {
	    			Map<String, String> paramMap = new HashMap<String,String>();
	    			paramMap.put("phoneNumber", msisdn);
	    			paramMap.put("smsText", smsText);
	    			paramMap.put("appsessionID", appSessionId);
	    			GenericUtility.callService(finalURL, paramMap, "POST");
	    
	    			populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCode, true);
	    			
	    			session.removeAttribute(SessionConstants.SMS_TEXT);
				}
				else {
				    logger.warn(className+".execute: smsText or msisdn was empty, cannot send SMS");
				}
				
			} catch (Exception e) {
				logger.error(className+".execute: Error sending SMS", e);
				populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCode, false);
	
				// report the error
				String message = "CORBA:sendSMS:Error:unknown";
				
				incrementBackendError(requestDataContext);
				setLastBackendError(requestDataContext, message);
			}
		}
		
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Exiting method.");
		return hashMap;
	}

	protected String getLookupURL(HttpServletRequest httpRequest) {
		@SuppressWarnings("unchecked")
		Map<String,String> dbMap = (Map<String, String>) httpRequest.getAttribute("dataaccess.configuration");
		
		String url = dbMap.get(SEND_SMS);
		if(logger.isDebugEnabled()) logger.debug(className+".getLookupURL: url= [" + url + "]");

		return url;
	}
	
}
