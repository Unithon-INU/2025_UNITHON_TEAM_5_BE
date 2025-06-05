package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
@Slf4j
public class EgenService {

    @Value("${egen.api.key}")
    private String apiKey;

    @Value("${egen.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public EgenResponse<NearbyHospitalItem> getNearbyHospitals(double lat, double lon) {
        String url = String.format("%s/ErmctInfoInqireService/getEgytLcinfoInqire?serviceKey=%s&WGS84_LAT=%s&WGS84_LON=%s&pageNo=1&numOfRows=10&_type=json",
                baseUrl, apiKey,
                URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8)
        );
        return callEgenApi(url, NearbyHospitalItem.class);
    }

    public EgenResponse<HospitalInfoItem> getHospitalInfo(String hpid) {
        String url = String.format("%s/ErmctInfoInqireService/getEgytBassInfoInqire?serviceKey=%s&HPID=%s&pageNo=1&numOfRows=10&_type=json",
                baseUrl, apiKey,
                URLEncoder.encode(hpid, StandardCharsets.UTF_8)
        );
        return callEgenApi(url, HospitalInfoItem.class);
    }

    public EgenResponse<AvailableBedsItem> getAvailableBeds(String stage1, String stage2) {
        String url = String.format("%s/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire?serviceKey=%s&STAGE1=%s&STAGE2=%s&pageNo=1&numOfRows=10&_type=json",
                baseUrl, apiKey,
                URLEncoder.encode(stage1, StandardCharsets.UTF_8),
                URLEncoder.encode(stage2, StandardCharsets.UTF_8)
        );
        return callEgenApi(url, AvailableBedsItem.class);
    }

    public EgenResponse<ClinicItem> getClinics(
            String Q0, String Q1, String QZ, String QD, String QT, String QN, String ORD, Integer pageNo, Integer numOfRows
    ) {
        StringBuilder url = new StringBuilder(baseUrl + "/HsptlAsembySearchService/getHsptlMdcncListInfoInqire?serviceKey=" + apiKey);

        if (Q0 != null) url.append("&Q0=").append(URLEncoder.encode(Q0, StandardCharsets.UTF_8));
        if (Q1 != null) url.append("&Q1=").append(URLEncoder.encode(Q1, StandardCharsets.UTF_8));
        if (QZ != null) url.append("&QZ=").append(QZ);
        if (QD != null) url.append("&QD=").append(QD);
        if (QT != null) url.append("&QT=").append(QT);
        if (QN != null) url.append("&QN=").append(URLEncoder.encode(QN, StandardCharsets.UTF_8));
        if (ORD != null) url.append("&ORD=").append(ORD);
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), ClinicItem.class);
    }

    public EgenResponse<NearbyHospitalItem> getNearbyClinics(double lat, double lon) {
        String url = String.format("%s/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?serviceKey=%s&WGS84_LAT=%s&WGS84_LON=%s&pageNo=1&numOfRows=10&_type=json",
                baseUrl, apiKey,
                URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8)
        );
        return callEgenApi(url, NearbyHospitalItem.class);
    }

    public EgenResponse<HospitalInfoItem> getClinicInfo(String hpid) {
        String url = String.format("%s/HsptlAsembySearchService/getHsptlBassInfoInqire?serviceKey=%s&HPID=%s&pageNo=1&numOfRows=10&_type=json",
                baseUrl, apiKey,
                URLEncoder.encode(hpid, StandardCharsets.UTF_8)
        );
        return callEgenApi(url, HospitalInfoItem.class);
    }

    private <T> EgenResponse<T> callEgenApi(String url, Class<T> itemClass) {
        try {
            URI uri = new URI(url);
            String responseStr = restTemplate.getForObject(uri, String.class);
            log.info("Request URI: {}", uri);
            log.info("Response: {}", responseStr);

            ObjectMapper mapper = new ObjectMapper();
            EgenApiWrapper<T> wrapper = mapper.readValue(
                    responseStr,
                    mapper.getTypeFactory().constructParametricType(EgenApiWrapper.class, itemClass)
            );
            return wrapper.getResponse();
        } catch (Exception e) {
            log.error("Error calling Egen API", e);
            return null;
        }
    }
}
