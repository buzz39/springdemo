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

import java.util.ArrayList;
import java.util.List;

/**
 * Bean holding the report data 
 *  
 * @author Nuance Communications
 */
public class ReportBean {
	private String appsessionID;
	private String START_TIMESTAMP; 			 
	private String END_TIMESTAMP;
	private String VSS_PORT;
	private String VSS_HOST;
	private String COMPLETE_NODE_SEQ_HL_VSS;
	private String ABORT_NODE_SEQ_HL_VSS;
	private String GEN_DLG_ID;
	private String DLG_CLI;
	private String CUST_ID;
	private int CUST_AUTH;
	private String CUST_SEG;
	private String UR_SERV;
	private String IVR_SERV_DLG_HL_VSS_MOD;
	private String IVR_SERV_VERS_DLG_HL_VSS_MOD; 
	private int IVR_HGUP_STATUS;
	private String NODE_SEQ_HL_VSS; 
	private String DLG_NEXTLAST_NODE;
	private String DLG_LAST_NODE;
	private String AGENT_EXIT;
	private String DLG_APP_TAG;
	private String LAST_BACKEND_ERR;
	private int VSS_COMPLETE;
	private int VSS_START;
	private String BRANDING;
	private String LANG;
	private int CLIR;
	private String NOINPUT_COUNT_DLG_HL_VSS_MOD;
	private String NOMATCH_COUNT_DLG_HL_VSS_MOD;
	private String INPUT_STATES_COUNT_DLG_HL_MOD;
	private List<KPIBean> KPI = new ArrayList<>();
	
	public List<KPIBean> getKPI() {
		return KPI;
	}
	
	public void setKPI(List<KPIBean> KPI) {
		this.KPI = KPI;
	}

	public void setAppsessionID(String appsessionID) {
		this.appsessionID = appsessionID;
	}
	
	public String getAppsessionID() {
		return appsessionID;
	}
	
	public String getSTART_TIMESTAMP() {
		return START_TIMESTAMP;
	}
	
	public void setSTART_TIMESTAMP(String sTART_TIMESTAMP) {
		START_TIMESTAMP = sTART_TIMESTAMP;
	}
	
	public String getEND_TIMESTAMP() {
		return END_TIMESTAMP;
	}
	
	public void setEND_TIMESTAMP(String eND_TIMESTAMP) {
		END_TIMESTAMP = eND_TIMESTAMP;
	}
	
	public String getVSS_PORT() {
		return VSS_PORT;
	}
	
	public void setVSS_PORT(String vSS_PORT) {
		VSS_PORT = vSS_PORT;
	}
	
	public String getVSS_HOST() {
		return VSS_HOST;
	}
	
	public void setVSS_HOST(String vSS_HOST) {
		VSS_HOST = vSS_HOST;
	}
	
	public String getCOMPLETE_NODE_SEQ_HL_VSS() {
		return COMPLETE_NODE_SEQ_HL_VSS;
	}
	
	public void setCOMPLETE_NODE_SEQ_HL_VSS(String cOMPLETE_NODE_SEQ_HL_VSS) {
		COMPLETE_NODE_SEQ_HL_VSS = cOMPLETE_NODE_SEQ_HL_VSS;
	}
	
	public String getABORT_NODE_SEQ_HL_VSS() {
		return ABORT_NODE_SEQ_HL_VSS;
	}
	
	public void setABORT_NODE_SEQ_HL_VSS(String aBORT_NODE_SEQ_HL_VSS) {
		ABORT_NODE_SEQ_HL_VSS = aBORT_NODE_SEQ_HL_VSS;
	}
	
	public String getGEN_DLG_ID() {
		return GEN_DLG_ID;
	}
	
	public void setGEN_DLG_ID(String gEN_DLG_ID) {
		GEN_DLG_ID = gEN_DLG_ID;
	}
	
	public String getDLG_CLI() {
		return DLG_CLI;
	}
	
	public void setDLG_CLI(String dLG_CLI) {
		DLG_CLI = dLG_CLI;
	}
	
	public String getCUST_ID() {
		return CUST_ID;
	}
	
	public void setCUST_ID(String cUST_ID) {
		CUST_ID = cUST_ID;
	}
	
	public int getCUST_AUTH() {
		return CUST_AUTH;
	}
	
	public void setCUST_AUTH(int cUST_AUTH) {
		CUST_AUTH = cUST_AUTH;
	}
	
	public String getCUST_SEG() {
		return CUST_SEG;
	}
	
	public void setCUST_SEG(String cUST_SEG) {
		CUST_SEG = cUST_SEG;
	}
	
	public String getUR_SERV() {
		return UR_SERV;
	}
	
	public void setUR_SERV(String uR_SERV) {
		UR_SERV = uR_SERV;
	}
	
	public String getIVR_SERV_DLG_HL_VSS_MOD() {
		return IVR_SERV_DLG_HL_VSS_MOD;
	}
	
	public void setIVR_SERV_DLG_HL_VSS_MOD(String iVR_SERV_DLG_HL_VSS_MOD) {
		IVR_SERV_DLG_HL_VSS_MOD = iVR_SERV_DLG_HL_VSS_MOD;
	}
	
	public String getIVR_SERV_VERS_DLG_HL_VSS_MOD() {
		return IVR_SERV_VERS_DLG_HL_VSS_MOD;
	}
	
	public void setIVR_SERV_VERS_DLG_HL_VSS_MOD(String iVR_SERV_VERS_DLG_HL_VSS_MOD) {
		IVR_SERV_VERS_DLG_HL_VSS_MOD = iVR_SERV_VERS_DLG_HL_VSS_MOD;
	}
	
	public int getIVR_HGUP_STATUS() {
		return IVR_HGUP_STATUS;
	}
	
	public void setIVR_HGUP_STATUS(int iVR_HGUP_STATUS) {
		IVR_HGUP_STATUS = iVR_HGUP_STATUS;
	}
	
	public String getNODE_SEQ_HL_VSS() {
		return NODE_SEQ_HL_VSS;
	}
	
	public void setNODE_SEQ_HL_VSS(String nODE_SEQ_HL_VSS) {
		NODE_SEQ_HL_VSS = nODE_SEQ_HL_VSS;
	}
	
	public String getDLG_NEXTLAST_NODE() {
		return DLG_NEXTLAST_NODE;
	}
	
	public void setDLG_NEXTLAST_NODE(String dLG_NEXTLAST_NODE) {
		DLG_NEXTLAST_NODE = dLG_NEXTLAST_NODE;
	}
	
	public String getDLG_LAST_NODE() {
		return DLG_LAST_NODE;
	}
	
	public void setDLG_LAST_NODE(String dLG_LAST_NODE) {
		DLG_LAST_NODE = dLG_LAST_NODE;
	}
	
	public String getAGENT_EXIT() {
		return AGENT_EXIT;
	}
	
	public void setAGENT_EXIT(String aGENT_EXIT) {
		AGENT_EXIT = aGENT_EXIT;
	}
	
	public String getDLG_APP_TAG() {
		return DLG_APP_TAG;
	}
	
	public void setDLG_APP_TAG(String dLG_APP_TAG) {
		DLG_APP_TAG = dLG_APP_TAG;
	}
	
	public String getLAST_BACKEND_ERR() {
		return LAST_BACKEND_ERR;
	}
	
	public void setLAST_BACKEND_ERR(String lAST_BACKEND_ERR) {
		LAST_BACKEND_ERR = lAST_BACKEND_ERR;
	}
	
	public int getVSS_COMPLETE() {
		return VSS_COMPLETE;
	}
	
	public void setVSS_COMPLETE(int vSS_COMPLETE) {
		VSS_COMPLETE = vSS_COMPLETE;
	}
	
	public int getVSS_START() {
		return VSS_START;
	}
	
	public void setVSS_START(int vSS_START) {
		VSS_START = vSS_START;
	}
	
	public String getBRANDING() {
		return BRANDING;
	}
	
	public void setBRANDING(String bRANDING) {
		BRANDING = bRANDING;
	}
	
	public String getLANG() {
		return LANG;
	}
	
	public void setLANG(String lANG) {
		LANG = lANG;
	}
	
	public int getCLIR() {
		return CLIR;
	}
	
	public void setCLIR(int cLIR) {
		CLIR = cLIR;
	}
	
	public String getNOINPUT_COUNT_DLG_HL_VSS_MOD() {
		return NOINPUT_COUNT_DLG_HL_VSS_MOD;
	}
	
	public void setNOINPUT_COUNT_DLG_HL_VSS_MOD(String nOINPUT_COUNT_DLG_HL_VSS_MOD) {
		NOINPUT_COUNT_DLG_HL_VSS_MOD = nOINPUT_COUNT_DLG_HL_VSS_MOD;
	}
	
	public String getNOMATCH_COUNT_DLG_HL_VSS_MOD() {
		return NOMATCH_COUNT_DLG_HL_VSS_MOD;
	}
	
	public void setNOMATCH_COUNT_DLG_HL_VSS_MOD(String nOMATCH_COUNT_DLG_HL_VSS_MOD) {
		NOMATCH_COUNT_DLG_HL_VSS_MOD = nOMATCH_COUNT_DLG_HL_VSS_MOD;
	}
	
	public String getINPUT_STATES_COUNT_DLG_HL_MOD() {
		return INPUT_STATES_COUNT_DLG_HL_MOD;
	}
	
	public void setINPUT_STATES_COUNT_DLG_HL_MOD(
			String iNPUT_STATES_COUNT_DLG_HL_MOD) {
		INPUT_STATES_COUNT_DLG_HL_MOD = iNPUT_STATES_COUNT_DLG_HL_MOD;
	}
	
}
