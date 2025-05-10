package com.curelingo.curelingo.egen.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "response")
public class EgenResponse {

    @JacksonXmlProperty(localName = "header")
    public Header header;

    @JacksonXmlProperty(localName = "body")
    public Body body;
}
