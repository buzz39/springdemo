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
import com.example.springsessionredis.application.model.SessionConstants;
import com.example.springsessionredis.sms.SMSDataManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
/**
 * Get information on mobile usage for the target MSISDN.
 * 
 * @author Nuance Communications
 */
public class SearchMobileUsageMCE extends AbstractClientDataAccessBusinessFunction {
	public SearchMobileUsageMCE(StringRedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	private static final String MOBILE_USAGE_INFO = "MOBILE_USAGE_INFO";
	public static final int STATUS_ONE = 1;
	/**
	 * Simple calss name for logging
	 */
	private static String className = SearchMobileUsageMCE.class.getSimpleName();
	/**
	 * Logger for this class
	 */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(className);
	/**
	 * Get information on mobile usage for the target MSISDN.
	 */
	@SuppressWarnings("rawtypes")
	public Map execute(HttpServletRequest requestDataContext) {
		
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Entering Method");
		
		HttpSession session = requestDataContext.getSession();
		Map<String, String> outputMap = new HashMap<String, String>();
		String returnCode = "success";
		String returnMessage = "";
		String finalURL = null;
		String ivrOriginalEntry = null;
		try {
			String dataAccessLayerURL = getDataAccessLayerURL(requestDataContext);
			if(logger.isDebugEnabled()) logger.debug(className+".execute: dataAccessLayerURL = [" + dataAccessLayerURL + "]");
			
			String searchMobileUsageMCE = String.valueOf(requestDataContext.getAttribute(SEARCH_MOBILE_USAGE_MCE));
			if(logger.isDebugEnabled()) logger.debug(className+".execute: searchMobileUsageMCE = [" + searchMobileUsageMCE + "]");
			
			finalURL = dataAccessLayerURL + searchMobileUsageMCE;
			if(logger.isDebugEnabled()) logger.debug(className+".execute: finalURL = [" + finalURL + "]");
			
			String msisdn = getString(requestDataContext, SessionConstants.IVR_MSISDN);
			String appSessionId = getString(requestDataContext, SessionConstants.UR_FIRST_CONNID);
	        ivrOriginalEntry = (String) session.getAttribute(SessionConstants.IVR_ORIGINAL_ENTRY);

			Map<String, String> mobileUsageInputParamMap = new HashMap<String, String>();
			mobileUsageInputParamMap.put("msisdn", msisdn);
			mobileUsageInputParamMap.put("appsessionID", appSessionId);
		
			logger.info(className+".execute: Retrieving UsageInfo for msisdn=["+msisdn+"] using RESTURL=["+finalURL+"]");
			
			String wsResponse = GenericUtility.callService(finalURL, mobileUsageInputParamMap, "POST");
			outputMap = parseMapOutput(wsResponse);
			
			logger.info(className+".execute: Received UsageInfo data for msisdn=["+msisdn+"], data=["+outputMap+"]");
			
			//backend call is successful
			if("0".equals(outputMap.get("returnCode"))) {
				Integer daysLeftInCurrentBillCycle = getIntegerValueFromStringMap(outputMap, "DaysLeftInCurrentBillCycle");
				Integer voideO2Net = getIntegerValueFromStringMap(outputMap, "VoiceO2Net");
				Integer voiceFixedNet = getIntegerValueFromStringMap(outputMap, "VoiceFixedNet");
				Integer voiceOtherDomesticeMobileNet = getIntegerValueFromStringMap(outputMap, "VoiceOtherDomesticMobileNet");
				Integer voideInternational = getIntegerValueFromStringMap(outputMap, "VoiceInternational");
				Integer voicePremium = getIntegerValueFromStringMap(outputMap, "VoicePremium");
				Integer voiceTotal = getIntegerValueFromStringMap(outputMap, "VoiceTotal");
				Integer smsTotal = getIntegerValueFromStringMap(outputMap, "SMSTotal");
				Integer mobileDataMinutes = getIntegerValueFromStringMap(outputMap, "MobileDataMinutes");
				Integer mobileDataMB = getIntegerValueFromStringMap(outputMap, "MobileDataMB");
				
				//TODO - commenting as nothing to map here
				/*
				 * setAttribute(session, SessionConstants.DAYS_LEFT_IN_CURRENT_BILL_CYCLE,
				 * daysLeftInCurrentBillCycle, true); setAttribute(session,
				 * SessionConstants.VOICE_O2_NET, voideO2Net, true); setAttribute(session,
				 * SessionConstants.VOICE_FIXED_NET, voiceFixedNet, true); setAttribute(session,
				 * SessionConstants.VOICE_OTHER_DOMESTIC_MOBILE_NET,
				 * voiceOtherDomesticeMobileNet, true); setAttribute(session,
				 * SessionConstants.VOICE_INTERNATIONAL, voideInternational, true);
				 * setAttribute(session, SessionConstants.VOICE_PREMIUM, voicePremium, true);
				 * setAttribute(session, SessionConstants.VOICE_TOTAL, voiceTotal, true);
				 * setAttribute(session, SessionConstants.SMS_TOTAL, smsTotal, true);
				 * setAttribute(session, SessionConstants.MOBILE_DATA_MINUTES,
				 * mobileDataMinutes, true); setAttribute(session,
				 * SessionConstants.MOBILE_DATA_MB, mobileDataMB, true);
				 */
				
				returnCode = "success";
				
				try {
					// find correct text (suffix 0 uses heute)
					String name = daysLeftInCurrentBillCycle==0 ? "usageInfoDynamicSMS0" : "usageInfoDynamicSMS";
					
					// replace all values (for heute :DAYS_LEFT_IN_CURRENT_BILL_CYCLE will not be there)
				    String text = 
				            SMSDataManager.getInstance().getText(name, "")
				            .replaceAll(":DAYS_LEFT_IN_CURRENT_BILL_CYCLE", Integer.toString(daysLeftInCurrentBillCycle))
				            .replaceAll(":VOICE_O2_NET", Integer.toString(voideO2Net))
				            .replaceAll(":VOICE_FIXED_NET", Integer.toString(voiceFixedNet))
				            .replaceAll(":VOICE_OTHER_DOMESTIC_MOBILE_NET", Integer.toString(voiceOtherDomesticeMobileNet))
				            .replaceAll(":VOICE_INTERNATIONAL", Integer.toString(voideInternational))
				            .replaceAll(":VOICE_PREMIUM", Integer.toString(voicePremium))
				            .replaceAll(":VOICE_TOTAL", Integer.toString(voiceTotal))
				            .replaceAll(":SMS_TOTAL", Integer.toString(smsTotal))
				            .replaceAll(":MOBILE_DATA_MINUTES", Integer.toString(mobileDataMinutes))
				            .replaceAll(":MOBILE_DATA_MB", Integer.toString(mobileDataMB));

    				session.setAttribute(SessionConstants.SMS_TEXT, text);
    				logger.info(className+".execute: Wrote usage info sms into session with text ["+text+"]");
				}
				catch(Exception e) {
				    logger.error(className+".execute: Unexpected error in sending the SMS (missing SMS text probably?)", e);
				}
			}
			//backend call is not successful
			else {
				returnCode = "error";
				if ("VSS".equals(ivrOriginalEntry)) {
					session.setAttribute(SessionConstants.HANGUP_REQUIRED, "true");
				}
				returnMessage = outputMap.get("returnMessage");
			}
		}
		catch(Exception exception) {
			logger.error(className+".execute: Exception occure while WS call", exception);
			returnCode = "error";
			if ("VSS".equals(ivrOriginalEntry)) {
				session.setAttribute(SessionConstants.HANGUP_REQUIRED, "true");
			}
		}
		outputMap.put(OUTPUT_RETURN_CODE, returnCode);
		outputMap.put(OUTPUT_RETURN_MESSAGE, returnMessage);
		if(logger.isDebugEnabled()) logger.debug(className+".execute: returnCode = ["+returnCode+"]");
		
		if(logger.isDebugEnabled()) logger.debug(className+".execute: Exiting Method");
		return outputMap;
	}
	
	/**
	 * This method parses the string integer
	 * @param map
	 * @param key
	 * @return
	 */
	private Integer getIntegerValueFromStringMap(Map<String, String> map, String key) {
		Integer integerValue = null;
		if (map != null) {
			String stringValue = map.get(key);
			try {
				integerValue = Integer.parseInt(stringValue);
			}
			catch(NumberFormatException nfe) {
				integerValue = new Integer(0);
			}
		}
		return integerValue;		
	}
}
