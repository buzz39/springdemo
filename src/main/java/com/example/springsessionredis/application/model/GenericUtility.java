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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXB;


/**
 * File to hold Utility functions
 *
 * @author Nuance Communications
 */

public class GenericUtility {
	private static final Logger logger = LoggerFactory.getLogger(GenericUtility.class.getName());
    private static String className = GenericUtility.class.getSimpleName();

    public static enum ENC_TYPE { ASCII, UTF8 };
    
	public static Object convertXMLtoJAVA(File file, Type typeof){
		return JAXB.unmarshal(file, typeof.getClass());
	}
	
	public static String convertJAVAtoXML(Object object){
		StringWriter writer = new StringWriter();
		JAXB.marshal(object, writer);
		return writer.toString();
	}
	
	public static String callService(String urlStr, Map<String, String> paramMap, String requestType) {
		return callService(urlStr, paramMap, requestType, StandardCharsets.US_ASCII);
	}
	
	public static String callService(String urlStr, Map<String, String> paramMap, String requestType, Charset charSet) {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestType);
		} catch (IOException e) {
	        logger.error(className+"... Error creating HTTP connection Object...", e);
		}
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		// Create the form content
		OutputStream out;
		StringBuilder sb = null;
		try {
			out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			
			for(Entry<String, String> paramMapEntry: paramMap.entrySet()){
				writer.write(paramMapEntry.getKey());
				writer.write("=");
				writer.write(URLEncoder.encode(paramMapEntry.getValue(), "UTF-8"));
				writer.write("&");
			}
			writer.close();
			out.close();
		
			// an error occurred
			if (conn.getResponseCode()>=300) {
			    logger.warn(className+"... unexpected response code "+conn.getResponseCode());
				throw new IOException("Could not read data from database: returncode="+conn.getResponseCode()+", message="+conn.getResponseMessage());
			}
			
			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));
			sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		} catch (IOException e) {
	        logger.error(className+"... Please check if the TEFDataaccess is running...", e);
		}
		conn.disconnect();
		
		// return result or null if no result exists
		return (sb != null) ? sb.toString() : null;
	}
	
	public static boolean isNullOrEmptyString(String value) {
		if (value == null || "".equals(value)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static XMLGregorianCalendar convertStringToXmlGregorian(String dateString)
			throws DatatypeConfigurationException {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateString);
			GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
			gc.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (ParseException e) {
			// Optimize exception handling
			System.out.print(e.getMessage());
			return null;
		}

	}
}
