package com.curelingo.curelingo.egen.nearby;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NearbyHospitalService {

    @Value("${egen.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<NearbyHospitalItem> getNearbyHospitals(double lat, double lon) {
        try {
            String url = String.format("http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytLcinfoInqire" +
                            "?serviceKey=%s&WGS84_LAT=%s&WGS84_LON=%s&pageNo=1&numOfRows=10&_type=json",
                    apiKey,
                    URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8),
                    URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8)
            );

            URI uri = new URI(url);
            String jsonString = restTemplate.getForObject(uri, String.class);

            log.info("Final Request URI: {}", uri);
            log.info("API Response: {}", jsonString);

            ObjectMapper objectMapper = new ObjectMapper();
            NearbyHospitalResponse nearbyHospitalResponse = objectMapper.readValue(jsonString, NearbyHospitalResponse.class);

            if (nearbyHospitalResponse.getResponse() == null ||
                nearbyHospitalResponse.getResponse().getBody() == null ||
                nearbyHospitalResponse.getResponse().getBody().getItems() == null) {
                log.error("NearbyHospitalResponse body is null or malformed");
                return List.of();
            }

            List<NearbyHospitalItem> items = nearbyHospitalResponse.getResponse().getBody().getItems().getItem();
            return items != null ? items : List.of();
        } catch (IOException | URISyntaxException e) {
            log.error("Error occurred while fetching nearby hospitals", e);
            return List.of();
        }
    }
}
