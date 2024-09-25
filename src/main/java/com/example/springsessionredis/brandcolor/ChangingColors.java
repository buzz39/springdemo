package com.example.springsessionredis.brandcolor;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ChangingColors")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangingColors {

	@XmlElement(name="ChangeColor")
	private List<ChangingColor> changingColors = new ArrayList<ChangingColor>();
	
	public List<ChangingColor> getChangingColors() {
		return changingColors;
	}
	
}
