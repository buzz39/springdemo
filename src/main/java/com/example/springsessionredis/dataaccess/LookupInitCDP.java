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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.example.springsessionredis.application.model.AbstractClientDataAccessBusinessFunction;
import com.example.springsessionredis.application.model.BackendConstants;
import com.example.springsessionredis.application.model.GenericUtility;
import com.example.springsessionredis.application.model.ReportingManager;
import com.example.springsessionredis.application.model.SessionConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Fetching user data from the CDP Database.
 * 
 * @author Nuance Communications
 */
public class LookupInitCDP extends AbstractClientDataAccessBusinessFunction {
	public LookupInitCDP(StringRedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	private static String className = LookupInitCDP.class.getName();
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(className);
	
	private static final String CARE = "CARE";
	private static final String IVR = "IVR";
	
	public Map<String, String> execute(HttpServletRequest requestDataContext) {
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Entering method.");
		Map<String, String> hashMap = new HashMap<String, String>();
		String returnCode = "success";
		
		HttpSession session = requestDataContext.getSession();
		String brandId = (String) session.getAttribute(SessionConstants.UR_BRAND);
		if(logger.isDebugEnabled()) logger.debug(className+".execute: brandId = [" + brandId + "]");
		
		String msisdn = (String) session.getAttribute(SessionConstants.IVR_MSISDN);
		if (msisdn == null || "undefined".equalsIgnoreCase(msisdn)) {
			msisdn = "";
		}
		if(logger.isDebugEnabled()) logger.debug(className+".execute: msisdn = [" + msisdn + "]");
		
		String appsessionID = getString(requestDataContext, SessionConstants.UR_FIRST_CONNID);
		
		// Take the simnumber from SIM or the IVR_ID_VALUE when entering for the first time 
		String simcardId = (String) session.getAttribute(SessionConstants.SIM_NUMBER);
		if (simcardId == null || "undefined".equalsIgnoreCase(simcardId)) {
			simcardId = "";
			
			String ivrIdType = (String) session.getAttribute(SessionConstants.IVR_ID_TYPE);
			String ivrIdValue = (String) session.getAttribute(SessionConstants.IVR_ID_VALUE);
			if ("SIM".equals(ivrIdType) && ivrIdValue!=null && !"undefined".equalsIgnoreCase(ivrIdValue)) {
				simcardId = ivrIdValue;
			}
		}
		if(logger.isDebugEnabled()) logger.debug(className+".execute: simcardId = [" + simcardId + "]");
		
		String urFirstConnId = (String) session.getAttribute(SessionConstants.UR_FIRST_CONNID);
		if(logger.isDebugEnabled()) logger.debug(className+".execute: urFirstConnId = [" + urFirstConnId + "]");
		
		String ivrIdType = (String)session.getAttribute(SessionConstants.IVR_ID_TYPE);
		if(logger.isDebugEnabled()) logger.debug(className+".execute: ivrIdType = [" + ivrIdType + "]");
		
		String calltype = (String)session.getAttribute(SessionConstants.IVR_ORIGINAL_ENTRY);
		
		// check if data exists
		if (msisdn.isEmpty() && simcardId.isEmpty()) {
			logger.info(className+".execute: both msisdn and simcardId are empty, skipping data access");
			populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCode, false);
		}
		
		// get the data
		else {
			try {
				String baseURL = getDataAccessLayerURL(requestDataContext);
				if(logger.isDebugEnabled()) logger.debug(className+".execute: dataAccessLayerURL = [" + baseURL + "]");
				
				String cdpLookupURL = getCDPLookupURL(requestDataContext);
				if(logger.isDebugEnabled()) logger.debug(className+".execute: cdpLookupURL = [" + cdpLookupURL + "]");
	
				String finalURL = baseURL + cdpLookupURL;
				if(logger.isDebugEnabled()) logger.debug(className+".execute: finalURL = [" + finalURL + "]");
				
				/**
				 * Look up will be either on SIM number of MSISDN 
				 */
				Map<String, String> paramMap = new HashMap<String,String>();
				paramMap.put("calltype", calltype);
				paramMap.put("iSPMode", CARE);
				paramMap.put("iLOName", IVR);
				paramMap.put("iConnId", IVR+":"+urFirstConnId);
				paramMap.put("brandid", brandId);
				paramMap.put("msisdn", msisdn);
				paramMap.put("simcardId", simcardId);
				paramMap.put("ivrIdType", ivrIdType);
				paramMap.put("appsessionID", appsessionID);
				
				logger.info(className+".execute: Request to "+finalURL+" with "+paramMap);
				String wsResponse = GenericUtility.callService(finalURL, paramMap, "POST", StandardCharsets.UTF_8);
				hashMap = retrieveValuesFromResponse(wsResponse);
				String error = hashMap.get("errorType");
				if (error != null) {
					ReportingManager rm = (ReportingManager) session.getAttribute(SessionConstants.REPORTING_MANAGER);
					String cdpRequest = hashMap.get("request");
					String cdpResponse = hashMap.get("response").replaceAll("EQUALS", "=");
					String cdpBackend = hashMap.get("backendName");
			    	String cdpService = hashMap.get("serviceName");
			    	logger.error(cdpBackend + "_" + cdpService + ": Backend error executing request: "+cdpRequest+", response: " + cdpResponse);
					rm.addKPI(BackendConstants.REQUEST, cdpRequest, "BACKEND_EXCEPTION_" + cdpBackend + "_" + cdpService, "ERROR:" + error, 99);
					rm.addKPI(BackendConstants.RESPONSE, cdpResponse, "BACKEND_EXCEPTION_" + cdpBackend + "_" + cdpService, "ERROR:" + error, 99);
				}
				logger.info(className+".execute: Received response: "+hashMap);
				
				String returnCodeWS = hashMap.get("returnCode");
				String returnMessage = hashMap.get("returnMessage");
				if("1".equalsIgnoreCase(returnCodeWS)) {
					requestDataContext.getSession().setAttribute("lastBackendErrors", returnMessage);
				} else {
					if(logger.isDebugEnabled()) logger.debug(className+".execute: finished");
				}


			} catch (Exception e) {
				logger.error("Error found while accessing backend", e);
				returnCode = "failure";
				
				incrementBackendError(requestDataContext);
			}
		
			// if there is an error, then report it
			if ("1".equals(hashMap.get("returnCode"))) {
				String message = hashMap.get("returnMessage");
				logger.error("Backend returned error: "+message);
				
				ReportingManager rm = (ReportingManager) session.getAttribute(SessionConstants.REPORTING_MANAGER);
				rm.incrementNumBackendErrors();
				rm.setLastBackendError(message);
			}
			
			if (hashMap.get("msisdnOfSimNo") != null) {
				String temp = (String) hashMap.get("msisdnOfSimNo");
				if(logger.isDebugEnabled()) logger.debug(className+".execute: msisdnOfSimNo = [" + temp + "]");
				session.setAttribute(SessionConstants.IVR_MSISDN, temp);
			}
			
			// set into HTTP session
			String pkk = (String) hashMap.get("pkk");
			if (pkk!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: pkk = [" + pkk + "]");
				session.setAttribute(SessionConstants.PKK, pkk);
			}
	
			String vvl = (String) hashMap.get("vvl");
			if (vvl!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: vvl = [" + vvl + "]");
				session.setAttribute(SessionConstants.VVL, vvl);
			}
	
			String vvlLast = (String) hashMap.get("vvlLast");
			if (vvlLast!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: vvlLast = [" + vvlLast + "]");
				session.setAttribute(SessionConstants.VVL_LAST, vvlLast);
			}
			
			String winback = (String) hashMap.get("winback");
			if (winback!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: winback = [" + winback + "]");
				session.setAttribute(SessionConstants.WINBACK, winback);
			}
			
		    String hnprodl = (String) hashMap.get("HNPRODL");
            if (hnprodl==null) {
                hnprodl = "NULL";
            }
            if(logger.isDebugEnabled()) logger.debug(className+".execute: hnprodl = [" + hnprodl + "]");
            session.setAttribute(SessionConstants.HNPRODL, hnprodl);

            String o2Prodl = (String) hashMap.get("O2PRODL");
            if (o2Prodl==null) {
                o2Prodl = "NULL";
            }
            if(logger.isDebugEnabled()) logger.debug(className+".execute: o2Prodl = [" + o2Prodl + "]");
            session.setAttribute(SessionConstants.O2PRODL, o2Prodl);
			
			// calculate the brand color
			String custId = (String) hashMap.get("custId");
			String dslStack = (String) hashMap.get("dslStack");
			
			String brandColor = null;
			
			// handling dsl stack
			if (dslStack!=null && !"".equals(dslStack)) {
			    if(logger.isDebugEnabled()) logger.debug(className+".execute: dslStack = [" + dslStack + "]");
			    
			    if ("RED".equalsIgnoreCase(dslStack)) {
			        brandColor = "rot";
			    }
			    else if ("BLUE".equalsIgnoreCase(dslStack)) {
                    brandColor = "blau";
                }
                else if ("PURPLE".equalsIgnoreCase(dslStack)) {
                    brandColor = "purple";
                }
			    
			    if(logger.isDebugEnabled()) logger.debug(className+".execute: brandColor = [" + brandColor + "] from dslStack = [" + dslStack + "]");
			}
			
			// handling missing main color
            if (brandColor==null) {
                logger.warn(className+".execute: brandColor unknown since the dslStack was missing or empty");
			
        		if (custId!=null && !custId.isEmpty()) {
        			if(logger.isDebugEnabled()) logger.debug(className+".execute: custId = [" + custId + "]");
        			
        			brandColor = custId.startsWith("DE") ? "rot" : "blau";        			
        			
        			if(logger.isDebugEnabled()) logger.debug(className+".execute: brandColor = [" + brandColor + "] from custId = [" + custId + "]");
        		}
        		else {
        			logger.error(className+".execute: brandColor unknown since the custId was missing or empty");
        		}
            }
			
            // finally report
			if (brandColor!=null) {
                session.setAttribute(SessionConstants.BRANDCOLOR, brandColor);
                session.setAttribute(SessionConstants.CUSTOMERID, custId);
			}
			else {
			    logger.warn(className+".execute: brandColor unknown since the custId was missing or empty");
			}
			logger.info(className+".execute: brandColor = [" + brandColor + "] and custId = [" + custId + "] and dslStack = [" + dslStack + "]");
			
			// find activation message
			String activationMessage = (String) hashMap.get("activationMessage");
			if (activationMessage!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: activationMessage = [" + activationMessage + "]");
				session.setAttribute(SessionConstants.ACTIVATION_MESSAGE, activationMessage);
			}
	
			String activationStatus = (String) hashMap.get("activationStatus");
			if (activationStatus!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: activationStatus = [" + activationStatus + "]");
				session.setAttribute(SessionConstants.ACTIVATION_STATUS, activationStatus);
			}

			String activationOrderId = (String) hashMap.get("activationOrderId");
			if (activationOrderId!=null) {
				if(logger.isDebugEnabled()) logger.debug(className+".execute: activationOrderId = [" + activationOrderId + "]");
				session.setAttribute(SessionConstants.ACTIVATION_ORDER_ID, activationOrderId);
			}
			
			// TODO - will only be available once coming from the DB, assuming false for now
//			String vasa_forces_hang_up = (String) hashMap.get("vasa_forces_hang_up");
//			if (vasa_forces_hang_up==null) {
//				vasa_forces_hang_up = "0";
//			}
//			if(logger.isDebugEnabled()) logger.debug(className+".execute: vasa_forces_hang_up = [" + vasa_forces_hang_up + "]");
//			session.setAttribute(SessionConstants.VASA_FORCES_HANGUP, vasa_forces_hang_up);
	
			
			//PUK Information 
			String subscriptionId = (String) hashMap.get("subscriptionid");
			if (subscriptionId != null) {
				session.setAttribute(SessionConstants.SUBSCRIPTION_ID, subscriptionId);
			}

			// Customer firstname
	        String firstname = (String) hashMap.get("firstname");
	        if (firstname!=null) {
	            if(logger.isDebugEnabled()) logger.debug(className+".execute: firstname = [" + firstname + "]");
	            session.setAttribute(SessionConstants.FIRST_NAME, firstname);
	        }

	        // Customer lastname
            String lastname = (String) hashMap.get("lastname");
            if (lastname!=null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: lastname = [" + lastname + "]");
                session.setAttribute(SessionConstants.LAST_NAME, lastname);
            }
            
            // addressid
            String addressid = (String) hashMap.get("addressid");
            if (addressid!=null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: addressid = [" + addressid + "]");
                session.setAttribute(SessionConstants.ADDRESS_ID, addressid);
            }
            
            // addressName
            String addressName = (String) hashMap.get("addressName");
            if (addressName!=null) {
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: addressName = [" + addressName + "]");
            	session.setAttribute(SessionConstants.ADDRESS_NAME, addressName);
            }
            
            // simcardIdIndex
            String simcardIdIndex = (String) hashMap.get("simcardIdIndex");
            if (simcardIdIndex!=null) {
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: simcardIdIndex = [" + simcardIdIndex + "]");
            	session.setAttribute(SessionConstants.SIM_CARD_ID_INDEX, simcardIdIndex);
            }
            
            String simcardIdReturned = (String) hashMap.get("simcardIdReturned");
            if (simcardIdReturned!=null) {
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: simcardIdReturned = [" + simcardIdReturned + "]");
            	session.setAttribute(SessionConstants.SIM_CARD_ID_RETURNED, simcardIdReturned);
            }
            
            String currentSIMCardId = (String) hashMap.get("currentSIMCardId");
            if (currentSIMCardId!=null) {
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: currentSIMCardId = [" + currentSIMCardId + "]");
            	session.setAttribute(SessionConstants.CURRENT_SIMCARD_ID, currentSIMCardId);
            }
            
            // numberOfActiveSIMs
            String numberOfActiveSIMs = (String) hashMap.get("numberOfActiveSIMs");
            if (numberOfActiveSIMs!=null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: numberOfActiveSIMs = [" + numberOfActiveSIMs + "]");
                session.setAttribute(SessionConstants.NUMBER_OF_ACTIVE_SIMS, numberOfActiveSIMs);
            }
            
            //  fixnet_number for DSL module 
            String fixnetNumber = (String) hashMap.get("fixnet_number");
            if (fixnetNumber != null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: fixnetNumber = [" + fixnetNumber + "]");
                session.setAttribute(SessionConstants.FIXNET_NUMBER, fixnetNumber);
            }
            
            //  isValid for SIM module 
            String isValid = (String) hashMap.get("isValid");
            if (isValid != null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: isValid = [" + isValid + "]");
                session.setAttribute(SessionConstants.IS_VALID, isValid);
            }else{
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: isValid = [" + false + "]");
                session.setAttribute(SessionConstants.IS_VALID, "false");
            }
            
            String hasActiveSubcriptions = (String) hashMap.get("hasActiveSubcriptions");
            if (hasActiveSubcriptions != null) {
                if(logger.isDebugEnabled()) logger.debug(className+".execute: hasActiveSubcriptions = [" + hasActiveSubcriptions + "]");
                session.setAttribute(SessionConstants.HAS_ACTIVE_SUBCRIPTIONS, Boolean.valueOf(hasActiveSubcriptions));
            }else{
            	if(logger.isDebugEnabled()) logger.debug(className+".execute: hasActiveSubcriptions = [" + false + "]");
                session.setAttribute(SessionConstants.HAS_ACTIVE_SUBCRIPTIONS, false);
            }
			// return
			populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCode, true);
		}
		
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Exiting method.");
		return hashMap;
	}

	private Map<String, String> retrieveValuesFromResponse(String wsResponse) {
		Map<String, String> responseMap = new HashMap<String, String>();
		if (wsResponse != null && wsResponse.isEmpty() == false) {
			String entryKey = null;
			String entryValue = null;
			String[] dataEntries = wsResponse.replace("{", "").replace("}", "").split(", ");
			for (String dataEntry: dataEntries) {
				String[] data = dataEntry.split("=");
				if (data != null) {
					for(int index = 0; index < data.length; index ++) {
						if(index == 0){
							entryKey = data[index];
							entryValue = null;
						}
						if(index == 1){
							entryValue = data[index];
						}
					}
					if(entryKey != null && entryValue != null && !"null".equals(entryValue)) {
						entryKey = entryKey.trim();
						entryValue = entryValue.trim();
						responseMap.put(entryKey, entryValue);
					}
				}
			}
		}
		return responseMap;
	}

}
