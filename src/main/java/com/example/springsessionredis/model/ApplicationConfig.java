package com.example.springsessionredis.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "application-config")
public class ApplicationConfig {

    private List<Parameter> parameters;
    private ParametersWrapper parametersWrapper;  // Optional, for XMLs with <parameters>

    // Handle directly nested parameters in the root
    @XmlElement(name = "parameter")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    // Handle parameters inside <parameters> wrapper
    @XmlElement(name = "parameters")
    public ParametersWrapper getParametersWrapper() {
        return parametersWrapper;
    }

    public void setParametersWrapper(ParametersWrapper parametersWrapper) {
        this.parametersWrapper = parametersWrapper;
    }
}
