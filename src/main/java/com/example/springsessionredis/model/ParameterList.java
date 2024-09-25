package com.example.springsessionredis.model;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class ParameterList {
    private List<String> values;

    @XmlElement(name = "value")
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
