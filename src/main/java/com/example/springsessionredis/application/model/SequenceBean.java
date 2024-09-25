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
 * Bean holding event instances. 
 *  
 * @author Nuance Communications
 */
public class SequenceBean {
	private String COMPLETE_NODE_SEQ_HL_VSS= "";
	private String ABORT_NODE_SEQ_HL_VSS= "";

	public String getSelfServicesCompleted() {
		return COMPLETE_NODE_SEQ_HL_VSS;
	}
	
	public String getSelfServicesAborted() {
		return ABORT_NODE_SEQ_HL_VSS;
	}

}
