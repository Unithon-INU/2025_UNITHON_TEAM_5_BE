package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.*;
import com.fasterxml.jackson.databind.JavaType;
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

    public EgenResponse<NearbyHospitalItem> getNearbyHospitals(double lat, double lon, Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/ErmctInfoInqireService/getEgytLcinfoInqire?serviceKey=" + apiKey);

        url.append("&WGS84_LAT=").append(URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8));
        url.append("&WGS84_LON=").append(URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8));
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), NearbyHospitalItem.class);
    }

    public EgenResponse<HospitalInfoItem> getHospitalInfo(String hpid, Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/ErmctInfoInqireService/getEgytBassInfoInqire?serviceKey=" + apiKey);

        if (hpid != null) url.append("&HPID=").append(URLEncoder.encode(hpid, StandardCharsets.UTF_8));
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), HospitalInfoItem.class);
    }

    public EgenResponse<AvailableBedsItem> getAvailableBeds(String stage1, String stage2, Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire?serviceKey=" + apiKey);

        if (stage1 != null) url.append("&STAGE1=").append(URLEncoder.encode(stage1, StandardCharsets.UTF_8));
        if (stage2 != null) url.append("&STAGE2=").append(URLEncoder.encode(stage2, StandardCharsets.UTF_8));
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), AvailableBedsItem.class);
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

    public EgenResponse<NearbyHospitalItem> getNearbyClinics(double lat, double lon, Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?serviceKey=" + apiKey);

        url.append("&WGS84_LAT=").append(lat);
        url.append("&WGS84_LON=").append(lon);
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), NearbyHospitalItem.class);
    }

    public EgenResponse<HospitalInfoItem> getClinicInfo(String hpid, Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/HsptlAsembySearchService/getHsptlBassInfoInqire?serviceKey=" + apiKey);

        if (hpid != null) url.append("&HPID=").append(hpid);
        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), HospitalInfoItem.class);
    }

    public EgenResponse<HospitalFullInfoItem> getFullData(Integer pageNo, Integer numOfRows) {
        StringBuilder url = new StringBuilder(baseUrl + "/HsptlAsembySearchService/getHsptlMdcncFullDown?serviceKey=" + apiKey);

        if (pageNo != null) url.append("&pageNo=").append(pageNo);
        if (numOfRows != null) url.append("&numOfRows=").append(numOfRows);
        url.append("&_type=json");

        return callEgenApi(url.toString(), HospitalFullInfoItem.class);
    }

    private <T> EgenResponse<T> callEgenApi(String url, Class<T> itemClass) {
        try {
            URI uri = new URI(url);
            String responseStr = restTemplate.getForObject(uri, String.class);
            log.debug("Request URI: {}", uri);
            // log.debug("Response: {}", responseStr); // 응답 데이터는 너무 크므로 주석 처리

            ObjectMapper mapper = new ObjectMapper();
            JavaType responseType = mapper.getTypeFactory()
                    .constructParametricType(EgenApiWrapper.class,
                            mapper.getTypeFactory().constructParametricType(EgenResponse.class, itemClass));
            EgenApiWrapper<T> wrapper = mapper.readValue(responseStr, responseType);
            return wrapper.getResponse();
        } catch (Exception e) {
            log.error("Error calling Egen API", e);
            return null;
        }
    }
}
