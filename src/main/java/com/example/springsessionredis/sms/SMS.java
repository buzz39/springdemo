package com.example.springsessionredis.sms;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class SMS {
	
	@XmlAttribute(name="applicationTag")
	private String applicationTag;
	
	@XmlAttribute(name="brandColor")
	private String brandColor;
	
	@XmlValue
	private String text;
	
	public String getApplicationTag() {
		return applicationTag;
	}
	
	public String getBrandColor() {
		return brandColor;
	}
	
	public String getText() {
		return text;
	}

}
