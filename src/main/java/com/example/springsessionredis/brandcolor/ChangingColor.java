package com.example.springsessionredis.brandcolor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChangingColor {
    
    @XmlAttribute(name="numberType")
    private String numberType;
    
    @XmlAttribute(name="o2prodl")
    private String o2prodl;
    
    @XmlAttribute(name="hnprodl")
    private String hnprodl;

    @XmlAttribute(name="theme")
    private String theme;
    
    @XmlAttribute(name="color")
    private String color;

    public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public void setO2prodl(String o2prodl) {
		this.o2prodl = o2prodl;
	}

	public void setHnprodl(String hnprodl) {
		this.hnprodl = hnprodl;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNumberType() {
        return numberType;
    }

    public String getO2prodl() {
        return o2prodl;
    }

    public String getHnprodl() {
        return hnprodl;
    }

    public String getTheme() {
        return theme;
    }

    public String getColor() {
        return color;
    }
    
}
