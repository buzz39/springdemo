package com.example.springsessionredis.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class ParametersWrapper {

    private List<Parameter> parameterList;

    @XmlElement(name = "parameter")
    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
