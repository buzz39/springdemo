/* 
 * ---------------------------------------------------------------------------
 *
 * COPYRIGHT (c) 2016 Nuance Communications Inc.
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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Bean implementing CareSharedHL KPI reporting
 * 
 * @author Nuance Communications
 */
public class ReportingManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private ReportBean report = new ReportBean();
	
	private int numBackendErrors = 0;
	private int numUserErrors = 0;
	private String selfServicesCompleted = "";
	private String selfServicesAborted = "";

	public void incrementNumBackendErrors() {
		this.numBackendErrors++;
	}

	public void incrementNumUserErrors() {
		this.numUserErrors++;
	}

	public int getNumBackendErrors() {
		return numBackendErrors;
	}
	
	public int getNumUserErrors() {
		return numUserErrors;
	}

	public void setLastBackendError(String message) {
		report.setLAST_BACKEND_ERR(message);
	}
	
	public void addState(String stateID) {
		if (stateID!=null) {
			stateID = stateID.replaceAll(",,", ",");
		}
		String tmp = report.getNODE_SEQ_HL_VSS();
		if (tmp==null || tmp.isEmpty()) {
			tmp = stateID;
		} 
		else {
			tmp += "," + stateID;
		}
		report.setNODE_SEQ_HL_VSS(tmp);
	}
	
	public String getLastState() {
		String tmp = report.getNODE_SEQ_HL_VSS();
		if (tmp!=null) {
			String[] states= tmp.split(",");
			return states[states.length-1];
		}
		return null;
	}
	
	public String getStateBefore(String state) {
		String tmp = report.getNODE_SEQ_HL_VSS();
		if (tmp!=null) {
			String[] states= tmp.split(",");
			String eachState = null;
			for(int index = 0; index < states.length; index ++) {
				eachState = states[index];
				if(eachState.equalsIgnoreCase(state) == true) {
					return states[index - 1];
				}
			}
		}
		return null;
	}

	public String getSecondLastState() {
		String tmp = report.getNODE_SEQ_HL_VSS();
		if (tmp!=null) {
			String[] states= tmp.split(",");
			if (states.length>=2) {
				return states[states.length-2];
			}
		}
		return null;
	}

	public void setLastState(String lastState) {
		report.setDLG_LAST_NODE(lastState);
	}

	public void setSecondLastState(String secondLastState) {
		report.setDLG_NEXTLAST_NODE(secondLastState);
	}
	
	/**
	 * Successful end
	 */
	public void success() {
//		addKPI("Self_Result", "success");
		setHangupStatus(0);
	}
	
	/**
	 * Failure in user inputs
	 */
	public void failure() {
//		addKPI("Self_Result", "failure");
		setHangupStatus(0);
	}
	
	/**
	 * Error in application
	 */
	public void error() {
//		addKPI("Self_Result", "error");
		setHangupStatus(0);
	}
	
	/**
	 * System hung up
	 */
	public void hangup() {
//		addKPI("Self_Result", "hangup");
		setHangupStatus(2);
	}
	
	/**
	 * User hung up
	 */
	public void hungup() {
//		addKPI("Self_Result", "hangup");
		setHangupStatus(1);
	}
	
	public void backendErrors(String errors) {
//		addKPI("Self_NumBackendErrors", errors);
	}

	public void userErrors(String errors) {
//		addKPI("Self_NumUserErrors", errors);
	}

	public void setStartTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
		Calendar cal = Calendar.getInstance();
		report.setSTART_TIMESTAMP(dateFormat.format(cal.getTime()));
	}

	public void setEndTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
		Calendar cal = Calendar.getInstance();
		report.setEND_TIMESTAMP(dateFormat.format(cal.getTime()));
	}

	public void setServer(String server) {
		report.setVSS_HOST(server);
	}

	public void setPort(String port) {
		report.setVSS_PORT(port);
	}
	
	public void setCLI(String cli) {
		report.setDLG_CLI(cli);
	}

	public void setLanguage(String language) {
		if (language!=null) {
			int n = language.indexOf('-');
			language = language.substring(0,n);
		}
		report.setLANG(language);
	}

	public void setUniqueID(String appsessionid) {
		report.setGEN_DLG_ID(appsessionid);
	}

	public void setTransferTo(String transferTo) {
		report.setAGENT_EXIT(transferTo);
	}
	
	public void setCustomerSegment(String ivrCustomerSegment) {
		report.setCUST_SEG(ivrCustomerSegment);
	}
	
	public void setCustomerId(String customerId) {
		report.setCUST_ID(customerId);
	}
	
	public void setApplicationTag(String applicationTag) {
		report.setDLG_APP_TAG(applicationTag);
	}
	
	public void setService(String urService) {
		report.setUR_SERV(urService);
	}

	public void setDialogName(String dialogName) {
		report.setIVR_SERV_DLG_HL_VSS_MOD(dialogName);
	}

	public void setDialogVersion(String version) {
		report.setIVR_SERV_VERS_DLG_HL_VSS_MOD(version);
	}

	public void setBranding(String branding) {
		report.setBRANDING(branding);
	}

	public void setSelfServicesStarted(int started) {
		report.setVSS_START(started);
	}
	
	public String getSelfServicesCompleted() {
		return selfServicesCompleted;
	}
	
	public void setSelfServicesCompleted(List<String> selfServices) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<selfServices.size(); ++i) {
			if (i>0) sb.append(",");
			sb.append(selfServices.get(i));
		}
		report.setCOMPLETE_NODE_SEQ_HL_VSS(sb.toString());
	}
	
	public void setSelfServicesCompleted(int completed) {
		report.setVSS_COMPLETE(completed);
	}
	
	public String getSelfServicesAborted() {
		return selfServicesAborted;
	}

	public void setSelfServicesAborted(List<String> selfServices) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<selfServices.size(); ++i) {
			if (i>0) sb.append(",");
			sb.append(selfServices.get(i));
		}
		report.setABORT_NODE_SEQ_HL_VSS(sb.toString());
	}
	
	public void setAuthenticated(int authentication) {
		report.setCUST_AUTH(authentication);
	}
	
	public void setCLIR(int clir) {
		report.setCLIR(clir);
	}
	
	public void setHangupStatus(int status) {
		report.setIVR_HGUP_STATUS(status);
	}

	public void addKPI(String detailName, String detailValue, String kpiName, int status) {
		addKPI(detailName, detailValue, kpiName, "", status);
	}
	
	public void addKPI(String detailName, String detailValue, String kpiName, String detail, int status) {
		KPIBean kpi = null;
	
		kpi = report.getKPI().stream().filter(e -> e.getNAME().equals(kpiName)).findFirst().orElse(null);
	
		if (kpi == null) {
			kpi = new KPIBean();
			kpi.setNAME(kpiName);
			kpi.setSTATUS(status);
			kpi.setDETAIL(detail);
			report.getKPI().add(kpi);
		}

		KPIDetails kpiDetail = new KPIDetails();
		kpiDetail.setDATA_KEY(detailName);
		kpiDetail.setDATA_VALUE(detailValue);
		
		if (kpi.getKPI_DETAIL()==null) {
			kpi.setKPI_DETAIL(kpiDetail);
		}
		else {
			kpi.getKPI_DETAIL().add(kpiDetail);
		}
	}
	
	public void addKPI(String name, String value) {
		KPIBean kpi = new KPIBean(name,value);
		report.getKPI().add(kpi);
	}
	
	public void addEvents(EventBean bean) {
		report.getKPI().addAll(bean.getKpis());
	}

	public void addSelfService(String service, String returncode, String numBackendErrors, String numUserErrors) {
//		KPIBean kpi = new KPIBean("Self_"+service+"_Result", returncode);
//		report.getKPI().add(kpi);
//
//		kpi = new KPIBean("Self_"+service+"_NumBackendErrors", numBackendErrors);
//		report.getKPI().add(kpi);
//		
//		kpi = new KPIBean("Self_"+service+"_NumUserErrors", numUserErrors);
//		report.getKPI().add(kpi);
	}
	
	/**
	 * Merging the JSON'ized contents of an {@link EventBean} into this instance
	 * appending the events to the ones already stored.
	 * 
	 * @param json A JSON representation of the {@link EventBean} class.
	 */
	public void addEvents(String json) {
		Gson gson = new Gson();
		EventBean events = gson.fromJson(json, EventBean.class);
		report.getKPI().addAll(events.getKpis());
	}
	
	public void addSelfServices(String json) {
		Gson gson = new Gson();
		SequenceBean events = gson.fromJson(json, SequenceBean.class);
		selfServicesAborted = events.getSelfServicesAborted();
		selfServicesCompleted = events.getSelfServicesCompleted();
	}

	public String toJson(String appsessionID) {
		for (KPIBean bean : report.getKPI()) {
			int idx = 1000;
			if (bean.getKPI_DETAIL()!=null) {
				for (KPIDetails detail : bean.getKPI_DETAIL()) {
					detail.setDETAIL_SEQ_ID(idx);
					idx = idx+1;
				}
			}
		}
		
		report.setAppsessionID(appsessionID);
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String json = gson.toJson(report);
		return json;
	}

}
