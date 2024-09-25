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

public enum Status {
	
	//TODO these should be confirmed
		HANGUP(0), 
		SUCCESS(1), 
		FAILURE(2), 
		ERROR(3) ;

		private int id = 0;
		
		Status(int id){
			this.setId(id);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		
}

