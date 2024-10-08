package com.example.springsessionredis.exittags;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ExitTagReturn {

	@XmlAttribute(name="name")
    private String name;
	
    @XmlAttribute(name="value")
    private String value;
    
    public String getName() {
		return name;
	}
    
	public void setName(String name) {
		this.name = name;
	}
    
    public String getValue() {
		return value;
	}
    
	public void setValue(String value) {
		this.value = value;
	}

}