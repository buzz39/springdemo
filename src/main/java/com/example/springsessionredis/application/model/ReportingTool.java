/* 
 * ---------------------------------------------------------------------------
 *
 * COPYRIGHT (c) 2017 Nuance Communications Inc.

 * All Rights Reserved. Nuance Confidential.
 *
 * The copyright to the computer program(s) herein is the property of
 * Nuance Communications Inc. The program(s) may be used and/or copied
 * only with the written permission from Nuance Communications Inc.
 * or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 *
 * ---------------------------------------------------------------------------
 */
package com.example.springsessionredis.application.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;


/**
 * Implements interaction with the host agent for call start/stop
 * 
 * @author Nuance Communications
 */
public class ReportingTool {
    private static final Logger logger = LoggerFactory.getLogger(ReportingTool.class);
	
	public static final String APPLICATION = "APPLICATION";
	public static final String MODULE 	   = "MODULE";
	
	/**
	 * Sends the call start request.
	 * 
	 * @param session The session object to read data from
	 */
	static public void callStartHL(Session session, String type) {
		String contextPath = (String) session.getAttribute("CONTEXTPATH");
		callStart(session, type, contextPath, "HL");
	}
	
    /**
     * Sends the call start request.
     * 
     * @param session The session object to read data from
     */
    static public void callStartVSS(Session session, String type) {
        String contextPath = (String) session.getAttribute("CONTEXTPATH");
        callStart(session, type, contextPath, "VSS");
    }

    /**
     * Sends the call start request.
     * Same as {@link #callStart(Session, String, String, String)} with <b>segment</b> set to <tt>default</tt>.
     * 
     * @param session   The session object to read data from
     * @param type      The type of this call
     * @param path      The path of the module
     */
    static public void callStart(Session session, String type, String path) {
        callStart(session, type, path, "default");
    }
    
	/**
	 * Sends the call start request.
	 * 
	 * @param session  The session object to read data from
	 * @param type     The type of this call
	 * @param path     The path of the module
	 * @param segment  The caller segment of the call  
	 */
	static public void callStart(Session session, String type, String path, String segment) {
		try {
			String hostAgent = (String) session.getAttribute("HOSTAGENT");
			String callid = (String) session.getAttribute(SessionConstants.UR_FIRST_CONNID);
			String hostName = (String) session.getAttribute("HOSTNAME");
			String port = (String) session.getAttribute("PORT");
			
			if (callid==null) 							callid = "";
			if (path!=null && path.startsWith("/")) 	path = path.substring(1);
			if (segment==null)                          segment = "default";
			
			String urlString = hostAgent + "/call/start?appPath="+path+"&id="+callid+"&type="+type+"&host="+hostName+"&port="+port+"&segment="+segment;
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			in.close();
		} catch(Exception e) {
			logger.warn("Could not contact HostAgent on start call: "+e.getMessage());
		}
	}
	
	/**
	 * Sends the call end request.
	 * 
	 * @param session The session object to read data from
	 * @param returncode The returncode to use
	 */
	static public void callEnd(Session session, String type, String returncode) {
		String contextPath = (String) session.getAttribute("CONTEXTPATH");
		callEnd(session, type, contextPath, returncode);
	}
	
	/**
	 * Sends the call end request.
	 * 
	 * @param session The session object to read data from
	 * @param returncode The returncode to use
	 */
	static public void callEnd(Session session, String type, String contextPath, String returncode) {
		try {
			String hostAgent = (String) session.getAttribute("HOSTAGENT");
			String callid = (String) session.getAttribute(SessionConstants.UR_FIRST_CONNID);
			
			if (callid==null) 										callid = "";
			if (contextPath!=null && contextPath.startsWith("/")) 	contextPath = contextPath.substring(1);

			String urlString = hostAgent + "/call/end?appPath="+contextPath+"&id="+callid+"&outcome="+returncode+"&type="+type;
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			in.close();
		} catch(Exception e) {
			logger.warn("Could not contact HostAgent on end call: "+e.getMessage());
		}
	}
	
	/**
	 * Gets the URL to use from the dispatcher. 
	 * 
	 * @param dispatcher The dispatcher to use
	 * @param id The id to request 
	 */
	static public String getDispatcherURL(String dispatcher, String id) {
		String retval = null;
		try {
			String urlString = dispatcher + "/?id="+id+"&format=plain";
			URL url = new URL(urlString);
			InputStream in = url.openStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			retval = br.readLine();
			
			br.close();
		} catch(Exception e) {
			logger.warn("Could not resolve ID through dispatcher: "+e.getMessage());
		}
		return retval;
	}
	
}
