package com.curelingo.curelingo.egen.basic;

import com.curelingo.curelingo.egen.model.EgenResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class HospitalInfoService {

    @Value("${egen.api.base-url}")
    private String baseUrl;

    @Value("${egen.api.endpoints.useful-sckbd}")
    private String usefulSckbdPath;

    @Value("${egen.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public HospitalInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EgenResponse<HospitalInfoItem> fetchHospitalInfo(String HPID) {
        try {
            String url = String.format("%s%s?serviceKey=%s&HPID=%s&pageNo=1&numOfRows=10&_type=xml",
                    baseUrl, usefulSckbdPath, apiKey,
                    URLEncoder.encode(HPID, StandardCharsets.UTF_8)
            );

            URI uri = new URI(url);
            String xmlString = restTemplate.getForObject(uri, String.class);

            System.out.println("Final Request URI: " + uri);  // 요청 URL 확인
            System.out.println("API Response: " + xmlString); // 응답 확인

            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xmlString, EgenResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
