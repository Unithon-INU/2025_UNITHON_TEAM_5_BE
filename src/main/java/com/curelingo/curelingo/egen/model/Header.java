package com.curelingo.curelingo.egen.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Header {

    @JacksonXmlProperty(localName = "resultCode")
    public String resultCode;

    @JacksonXmlProperty(localName = "resultMessage")
    public String resultMessage;
}
