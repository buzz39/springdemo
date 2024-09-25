package com.example.springsessionredis.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class Parameter {
    private String name;
    private String value;  // For single value entries
    private MapParameter map;  // For nested parameters
    private ParameterList list;  // For lists of values

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlElement(name = "map")
    public MapParameter getMap() {
        return map;
    }

    public void setMap(MapParameter map) {
        this.map = map;
    }

    @XmlElement(name = "list")
    public ParameterList getList() {
        return list;
    }

    public void setList(ParameterList list) {
        this.list = list;
    }
}
