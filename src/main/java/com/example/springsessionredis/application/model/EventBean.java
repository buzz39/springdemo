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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Bean holding event instances. 
 *  
 * @author Nuance Communications
 */
public class EventBean {
	private List<KPIBean> KPI = new ArrayList<>();

	/**
	 * Returns all KPIs in a list.
	 * @return a list with KPIs
	 */
	public List<KPIBean> getKpis() {
		return KPI;
	}

	/**
	 * Adds a single KPI
	 * @param kpiBean The KPI to add
	 */
	public void addKpis(KPIBean kpiBean) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
		Calendar cal = Calendar.getInstance();
		kpiBean.setKPI_TIMESTAMP(dateFormat.format(cal.getTime()));
		
		KPI.add(kpiBean);
	}
	
	/**
	 * Merges other events into this event bean.
	 * @param bean The other event bean
	 */
	public void mergeWith(EventBean bean) {
		KPI.addAll(bean.KPI);
	}
	
}
