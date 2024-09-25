package com.example.springsessionredis.exittags;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ExitTags")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExitTags {

	@XmlElement(name="ExitTag")
	private List<ExitTag> exitTags = new ArrayList<ExitTag>();
	
	public List<ExitTag> getExitTags() {
		return exitTags;
	}
	
}
