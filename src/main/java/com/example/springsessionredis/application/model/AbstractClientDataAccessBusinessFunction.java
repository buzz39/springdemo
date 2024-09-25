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
package com.example.springsessionredis.application.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * <TODO>
 *
 * @author Nuance Communications
 */
public abstract class AbstractClientDataAccessBusinessFunction{
	private static String className = AbstractClientDataAccessBusinessFunction.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(AbstractClientDataAccessBusinessFunction.class);
	
	protected static final String OUTPUT_RETURN_CODE = "returnCode";
	protected static final String OUTPUT_RETURN_MESSAGE = "returnMessage";
	protected static final String OUTPUT_ACTIVATION_STATUS = "activationStatus";
	protected static final String OUTPUT_PUK = "activationStatus";
	
	protected static final String DATAACCESS_BASE_URL = "dataaccessbase.url";
	protected static final String DATAACCESSAPP_NAME_FETCH_URL = "dataaccessapp.name.fetch.url";
	protected static final String DATAACCESS_APP_NAME = "dataaccessapp.name";
	protected static final String CDP_LOOKUP_URL = "cdplookup.url";
	protected static final String MCE_LOOKUP_URL = "mcelookup.url";
	protected static final String REPORT_CALL = "ReportCall";
	protected static final String SEND_SMS = "SendSMS";
	protected static final String SEARCH_MOBILE_USAGE_MCE = "SearchMobileUsageMCE";
	private final StringRedisTemplate redisTemplate;

	
	@Autowired
    public AbstractClientDataAccessBusinessFunction(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	
	public boolean getBoolean(HttpServletRequest request, String property){
        HttpSession session = request.getSession();
        Object object = session.getAttribute(property);
        
        boolean retval = false;
        if (object instanceof String){
        	String value = (String)object;
    		if (value.equals("true")) {
    			retval = true;
    		} else if (value.equals("false")) {
    			retval = false;
    		} else {
    			if (logger.isWarnEnabled()) logger.warn(className+".execute.getBoolean property["+property+"] is boolean. value=["+value+"]");
    		}
        } else {
        	if (logger.isWarnEnabled()) logger.warn(className+".execute.getBoolean property["+property+"] is not String instance value=["+object+"]");
        }
        return retval;
	}
	
	public String getString(HttpServletRequest request, String property){
		HttpSession session = request.getSession();
        Object object = session.getAttribute(property);
        
        String retval = "";
        if (object instanceof String){
        	retval = (String)object;
        } else {
        	if (logger.isWarnEnabled()) logger.warn(className+".execute.getString property["+property+"] is not String instance value=["+object+"]");
        }
        return retval ;
	}
	
	public int getInt(HttpServletRequest request, String property){
		HttpSession session = request.getSession();
		Object object = session.getAttribute(property);
        int retval = 0;
        if (object instanceof String){
        	String value = (String)object;
    		try {
    			retval = Integer.parseInt(value);
    		} catch (Exception e){
    			if (logger.isWarnEnabled()) logger.warn(className+".execute.getInt property["+property+"] is int. value=["+value+"]");
    		}
        } else {
        	if (logger.isWarnEnabled()) logger.warn(className+".execute.getInt property["+property+"] is not String instance value=["+object+"]");
        }
        return retval;
	}

	
	/**
	 * Returns the URLs to access the data access
	 * 
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected String getDataAccessLayerURL(HttpServletRequest request){
		StringBuilder url = new StringBuilder();
		
		// Building one of the following URLs
		// 1) BASE_URL + fetched_from(NAME_FETCH_URL)
		// 2) BASE_URL + DEFAULT_APP_NAME
		
		// get the object
		
        Map<Object, Object> dbMap = redisTemplate.opsForHash().entries("service:dataaccess.configuration");

		// get the base URL
		Object dataAccessBaseURL = dbMap.get(DATAACCESS_BASE_URL);
		if(dataAccessBaseURL == null || dataAccessBaseURL.toString().isEmpty() == true) {
			if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: dataAccessBaseURL is null/empty, using the current server root as base URL");
			dataAccessBaseURL = getServerRoot(request);
		}
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: dataAccessBaseURL= [" + dataAccessBaseURL + "]");
		
		url.append(dataAccessBaseURL.toString());
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: url= [" + url + "]");
		
		Object dataaccessAppName = null;
		if(dbMap.get(DATAACCESSAPP_NAME_FETCH_URL) == null || dbMap.get(DATAACCESSAPP_NAME_FETCH_URL).toString().isEmpty()) {
			logger.error(className+".getDataAccessLayerURL: dispatcher base URL is null/empty, so using default data access app name");
			dataaccessAppName = dbMap.get(DATAACCESS_APP_NAME);
		}
		else {
			String dataAccessAppNameFetchURL = (String) dbMap.get(DATAACCESSAPP_NAME_FETCH_URL);
			if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: dataAccessAppNameFetchURL= [" + dataAccessAppNameFetchURL + "]");
			if(dataAccessAppNameFetchURL.toUpperCase().startsWith("HTTP") == false) {
				dataAccessAppNameFetchURL = getServerRoot(request) + dataAccessAppNameFetchURL;
				if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: dataAccessAppNameFetchURL= [" + dataAccessAppNameFetchURL + "]");
			}
			try {
				dataaccessAppName = getDataAccessAppName(dataAccessAppNameFetchURL);
				if(dataaccessAppName == null || dataaccessAppName.toString().isEmpty() == true) {
					logger.error(className+".getDataAccessLayerURL: data access app name fetched is null or empty, using default one");
					dataaccessAppName = dbMap.get(DATAACCESS_APP_NAME);
				}
			} 
			catch (IOException e) {
				logger.error(className+".getDataAccessLayerURL: failed to get the data access app name, using default one", e);
				dataaccessAppName = dbMap.get(DATAACCESS_APP_NAME);
			}
		}
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: dataaccessAppName= [" + dataaccessAppName + "]");
		
		url.append(dataaccessAppName.toString());
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessLayerURL: url= [" + url + "]");
	
		return url.toString();
	}
	
	protected String getCDPLookupURL(HttpServletRequest httpRequest) {
		@SuppressWarnings("unchecked")
        Map<Object, Object> dbMap = redisTemplate.opsForHash().entries("service:dataaccess.configuration");
		
		String url = (String) dbMap.get(CDP_LOOKUP_URL);
		if(logger.isDebugEnabled()) logger.debug(className+".getCDPLookupURL: url= [" + url + "]");

		return url;
	}
	
	/**
	 * This method returns the data access application name, fetched from dispatcher 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	private String getDataAccessAppName(String urlString) throws IOException {
		StringBuilder dataAccessAppName = new StringBuilder();
		URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line = null;
	    while ((line = rd.readLine()) != null) {
	    	dataAccessAppName.append(line);
	    }
	    rd.close();
	    if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessAppName: fetched dataAccessAppName = [" + dataAccessAppName + "]");
	    return dataAccessAppName.toString();
	}
	
	/**
	 * This method returns the server root like, <protocol>://<server_name>:<port>/ (e.g. http://ac-ps-nss4:8080/)
	 * @param request
	 * @return
	 */
	private String getServerRoot(HttpServletRequest request) {
		String protocalPrefix = null;
		if(request.getProtocol().toUpperCase().contains("HTTPS") == true) {
			protocalPrefix = "https://";
		}
		else {
			protocalPrefix = "http://";
		}
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessAppName: protocalPrefix = [" + protocalPrefix + "]");
		
		StringBuilder serverRoot = new StringBuilder()
				.append(protocalPrefix)   					  //protocol
				.append(request.getLocalAddr()).append(":")   //server IP
				.append(request.getServerPort()).append("/"); //port
		
		if(logger.isDebugEnabled()) logger.debug(className+".getDataAccessAppName: serverRoot = [" + serverRoot + "]");
		return serverRoot.toString();
	}
	
	/**
	 * Increment the error count.
 	 * @param requestDataContext The request data context
	 */
	protected void incrementBackendError(HttpServletRequest request) {
		try {
			ReportingManager rm = (ReportingManager) request.getSession().getAttribute(SessionConstants.REPORTING_MANAGER);
			rm.incrementNumBackendErrors();
		} catch (Exception e) {
			logger.error("Error updating reporting", e);
		}
	}

	/**
	 * Increment the error count.
 	 * @param requestDataContext The request data context
	 */
	protected void setLastBackendError(HttpServletRequest request, String message) {
		try {
			ReportingManager rm = (ReportingManager) request.getSession().getAttribute(SessionConstants.REPORTING_MANAGER);
			rm.setLastBackendError(message);
		} catch (Exception e) {
			logger.error("Error updating reporting", e);
		}
	}

	/**
	 * This method parses the WS response map String to java.util.Map
	 * @param input a JSON string representing the data returned 
	 * @return a map with response data, potentially empty
	 */
	protected static Map<String, String> parseMapOutput(String input) {
		Map<String, String> ouputMap = new HashMap<String, String>();
		if (input!=null) {
    		String[] wsDataEntries = input.replace("{", "").replace("}", "").split(",");
    		String entrykey = "";
    		String entryValue = "";
    		for (String wsDataEntry : wsDataEntries) {
    			String[] wsData = wsDataEntry.split("=");
    			if (wsData != null) {
    				
    				for (int i = 0; i < wsData.length; i++) {
    					if (i == 0) {
    						entrykey = wsData[i];
    						if(entrykey != null) {
    							entrykey = entrykey.trim();
    						}
    					}
    					if (i == 1) {
    						entryValue = wsData[i];
    						if(entryValue != null) {
    							entryValue = entryValue.trim();
    						}
    					}
    				}
    				if (entrykey.length() > 0)
    					ouputMap.put(entrykey, entryValue);
    			}
    		}
		}
		return ouputMap;
	}
	
	//Telefonica placeholders
	
	protected void populateStringMapValue(Map<String, String> hashMap, String outputReturnCode, String returnCodeSuccess2, boolean b) {
		System.out.println(outputReturnCode);
		System.out.println(returnCodeSuccess2);
		System.out.println(b);
	}
}