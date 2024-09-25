package com.example.springsessionredis.exittags;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ExitTag {
    
    @XmlAttribute(name="applicationTag")
    private String applicationTag;
    
    @XmlAttribute(name="brandColor")
    private String brandColor;
    
	@XmlElement(name="Return")
	private List<ExitTagReturn> exitTagReturns = new ArrayList<ExitTagReturn>();

	public String getApplicationTag() {
		return applicationTag;
	}
	
	public void setApplicationTag(String applicationTag) {
		this.applicationTag = applicationTag;
	}
	
	public String getBrandColor() {
		return brandColor;
	}
	
	public void setBrandColor(String brandColor) {
		this.brandColor = brandColor;
	}
	
	public List<ExitTagReturn> getExitTagReturns() {
		return exitTagReturns;
	}

}
