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
 * Bean holding <TODO> 
 *  
 * @author Nuance Communications
 */

public class KPIBean {
	private String DETAIL;
	private String NAME;
	private int STATUS;
	private String KPI_TIMESTAMP;
	
	private List<KPIDetails> KPI_DETAIL = null;
	
	public KPIBean() {}
	
	public KPIBean(String name, String value) {
		this.NAME = name;
		this.DETAIL = value;
	}
	
	public String getDETAIL() {
		return DETAIL;
	}
	public void setDETAIL(String dETAIL) {
		DETAIL = dETAIL;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public int getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(int sTATUS) {
		STATUS = sTATUS;
	}
	public List<KPIDetails> getKPI_DETAIL() {
		return KPI_DETAIL;
	}
	public void setKPI_DETAIL(KPIDetails kPI_DETAIL) {
		KPI_DETAIL = new ArrayList<>();
		int index = KPI_DETAIL.size();
		kPI_DETAIL.setDETAIL_SEQ_ID(index+1);
		KPI_DETAIL.add(kPI_DETAIL);
	}
	
	public String getKPI_TIMESTAMP() {
		return KPI_TIMESTAMP;
	}
	public void setKPI_TIMESTAMP(String kPI_TIMESTAMP) {
		KPI_TIMESTAMP = kPI_TIMESTAMP;
	}
}

