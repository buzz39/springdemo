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


/**
 * Bean holding <TODO> 
 *  
 * @author Nuance Communications
 */

public class KPIDetails {
	private String DATA_KEY;
	private String DATA_VALUE;
	private int DETAIL_SEQ_ID = 0;
	
	public String getDATA_KEY() {
		return DATA_KEY;
	}
	public void setDATA_KEY(String dATA_KEY) {
		DATA_KEY = dATA_KEY;
	}
	public String getDATA_VALUE() {
		return DATA_VALUE;
	}
	public void setDATA_VALUE(String dATA_VALUE) {
		DATA_VALUE = dATA_VALUE;
	}
	public int getDETAIL_SEQ_ID() {
		return DETAIL_SEQ_ID;
	}
	public void setDETAIL_SEQ_ID(int dETAIL_SEQ_ID) {
		DETAIL_SEQ_ID = dETAIL_SEQ_ID;
	}
}

