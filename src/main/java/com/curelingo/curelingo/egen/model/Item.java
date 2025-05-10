package com.curelingo.curelingo.egen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JacksonXmlProperty(localName = "dutyName")
    private String dutyName;

    @JacksonXmlProperty(localName = "dutyTel3")
    private String dutyTel3;

    // ... 필요한 필드를 계속 추가

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyTel3() {
        return dutyTel3;
    }

    public void setDutyTel3(String dutyTel3) {
        this.dutyTel3 = dutyTel3;
    }
}
