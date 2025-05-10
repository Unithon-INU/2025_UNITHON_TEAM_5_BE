package com.curelingo.curelingo.egen;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "response")
public class EmergencyHospitalResponse {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Data
    public static class Header {
        private String resultCode;
        private String resultMessage;
    }

    @Data
    public static class Body {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<HospitalItem> itemList;
    }

    @Data
    public static class HospitalItem {
        private String dutyName;
        private String dutyTel3;
        private String hpid;
        private String hv10;
        private String hv11;
        private String hv28;
        private String hv29;
        private String hv30;
        private String hv34;
        private String hv41;
        private String hv42;
        private String hv5;
        private String hv6;
        private String hv7;
        private String hvamyn;
        private String hvangioayn;
        private String hvcrrtayn;
        private String hvctayn;
        private String hvec;
        private String hvecmoayn;
        private String hvgc;
        private String hvhypoayn;
        private String hvicc;
        private String hvidate;
        private String hvincuayn;
        private String hvmriayn;
        private String hvncc;
        private String hvoc;
        private String hvoxyayn;
        private String hvventiayn;
        private String hvventisoayn;
        private String phpid;
        private String rnum;
    }
}
