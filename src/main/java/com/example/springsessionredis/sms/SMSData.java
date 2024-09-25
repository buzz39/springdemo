package com.example.springsessionredis.sms;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sms-data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SMSData {

	@XmlElement(name="sms")
	private List<SMS> sms = new ArrayList<SMS>();
	
	public List<SMS> getSms() {
		return sms;
	}
	
}
