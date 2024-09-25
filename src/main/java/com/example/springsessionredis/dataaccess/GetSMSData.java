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
import com.example.springsessionredis.application.model.SessionConstants;
import com.example.springsessionredis.sms.SMSDataManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Read the SMS data which depends on the applicationTag.
 * 
 * @author Nuance Communications
 */
public class GetSMSData extends AbstractClientDataAccessBusinessFunction {
	
	public GetSMSData(StringRedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	private static String className = GetSMSData.class.getName();
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(className);

	private final String returnCodeSuccess = "success";
	private final String returnCodeNoData = "no_data";

	public Map<String, String> execute(HttpServletRequest requestDataContext) {
		if (logger.isDebugEnabled()) logger.debug(className + ".execute: Entering method.");
		Map<String, String> hashMap = new HashMap<String, String>();
		System.out.println(requestDataContext.getSession().getId());
		HttpSession session = requestDataContext.getSession();
		System.out.println(session.getId());
		String applicationTag = (String) session.getAttribute(SessionConstants.APPLICATIONTAG);
		String brandColor = (String) session.getAttribute(SessionConstants.BRANDCOLOR);

		// get the SMS text
		String text = (String) session.getAttribute(SessionConstants.SMS_TEXT);

		// if not present fetch again
		if (text == null) {
			logger.debug(
					"Getting sms text for applicationTag=[" + applicationTag + "] and brandColor=[" + brandColor + "]");
			text = SMSDataManager.getInstance().getText(applicationTag, brandColor);
		}

		if (text != null && !text.isEmpty()) {
			logger.debug("Found sms text");
			session.setAttribute(SessionConstants.SMS_TEXT, text);

			populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCodeSuccess, true);
		} else {
			logger.debug("No sms text found!");
			session.removeAttribute(SessionConstants.SMS_TEXT);

			logger.warn("Tried sending SMS for applicationTag=[" + applicationTag + "] and brandColor=[" + brandColor
					+ "] but no SMS was found. Check the configurations!");
			populateStringMapValue(hashMap, OUTPUT_RETURN_CODE, returnCodeNoData, true);
		}

		if (logger.isDebugEnabled())
			logger.debug(className + ".execute: Exiting method.");
		return hashMap;
	}

	protected String getLookupURL(HttpServletRequest httpRequest) {
		@SuppressWarnings("unchecked")
		Map<String, String> dbMap = (Map<String, String>) httpRequest.getAttribute("dataaccess.configuration");

		String url = dbMap.get(REPORT_CALL);
		if (logger.isDebugEnabled())
			logger.debug(className + ".getLookupURL: url= [" + url + "]");

		return url;
	}

}
