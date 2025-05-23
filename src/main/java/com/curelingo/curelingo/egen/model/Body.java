package com.curelingo.curelingo.egen.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Body<T> {

    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    public List<T> items;

    @JacksonXmlProperty(localName = "numOfRows")
    public int numOfRows;

    @JacksonXmlProperty(localName = "pageNo")
    public int pageNo;

    @JacksonXmlProperty(localName = "totalCount")
    public int totalCount;
}
