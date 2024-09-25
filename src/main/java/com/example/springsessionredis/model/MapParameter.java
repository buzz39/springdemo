package com.example.springsessionredis.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class MapParameter {

    private List<Parameter> parameters;

    @XmlElement(name = "parameter")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
