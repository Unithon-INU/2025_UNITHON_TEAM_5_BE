package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.model.EgenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class EgenService {

    @Value("${egen.api.base-url}")
    private String baseUrl;

    @Value("${egen.api.endpoints.useful-sckbd}")
    private String usefulSckbdPath;

    @Value("${egen.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EgenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EgenResponse fetchEgenData(String stage1, String stage2) {
        String encodedKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        String url = String.format("%s%s?serviceKey=%s&STAGE1=%s&STAGE2=%s&pageNo=1&numOfRows=10&_type=xml",
                baseUrl, usefulSckbdPath, encodedKey,
                URLEncoder.encode(stage1, StandardCharsets.UTF_8),
                URLEncoder.encode(stage2, StandardCharsets.UTF_8)
        );

        return restTemplate.getForObject(url, EgenResponse.class);
    }
}
