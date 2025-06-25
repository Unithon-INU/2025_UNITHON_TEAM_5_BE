package com.curelingo.curelingo.gemini.emergencyadvisor;

import com.curelingo.curelingo.gemini.GeminiRestClient;
import com.curelingo.curelingo.gemini.prompt.GeminiEmergencyAdvisorPromptBuilder;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.curelingo.curelingo.mongodb.repository.EmergencyBedStatusRepository;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import com.curelingo.curelingo.mongodb.MongoHospital;
import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiEmergencyAdvisorService {

    private final GeminiRestClient geminiRestClient;
    private final EmergencyBedStatusRepository bedStatusRepository;
    private final HospitalRepository hospitalRepository;
    private final H3Service h3Service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<EmergencyBedStatus> findNearbyEmergencyBeds(double lat, double lng, double radiusKm) {
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        List<MongoHospital> mongoHospitals = hospitalRepository.findByDutyEryn("1");
        List<Hospital> hospitals = mongoHospitals.stream()
                .map(m -> Hospital.builder()
                        .hpid(m.getHpid())
                        .name(m.getDutyName())
                        .tel(m.getDutyTel1())
                        .addr(m.getDutyAddr())
                        .lat(m.getWgs84Lat())
                        .lng(m.getWgs84Lon())
                        .build())
                .toList();
        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        List<Hospital> candidates = hospitals.stream()
                .filter(h -> {
                    String cell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
                    double dist = h3Service.calcDistanceKm(lat, lng, h.getLat(), h.getLng());
                    return neighborCells.contains(cell) && dist <= radiusKm;
                })
                .toList();
        List<String> hpidList = candidates.stream().map(Hospital::getHpid).toList();
        Map<String, Double> hpidToDistance = candidates.stream()
                .collect(Collectors.toMap(Hospital::getHpid,
                        h -> h3Service.calcDistanceKm(lat, lng, h.getLat(), h.getLng())));
        List<EmergencyBedStatus> beds = bedStatusRepository.findByHpidIn(hpidList);
        beds.forEach(b -> b.setDistanceKm(hpidToDistance.getOrDefault(b.getHpid(), null)));
        return beds.stream()
                .sorted(Comparator.comparingDouble(b -> hpidToDistance.getOrDefault(b.getHpid(), Double.MAX_VALUE)))
                .toList();
    }

    public GeminiEmergencyRecommendationResponse recommendNearbyEmergency(
            double lat, double lng, double radiusKm
    ) {
        List<EmergencyBedStatus> candidates = findNearbyEmergencyBeds(lat, lng, radiusKm);

        // Gemini 프롬프트 생성 (beds, hpid, distanceKm만)
        String prompt = GeminiEmergencyAdvisorPromptBuilder.buildPrompt(candidates);

        // Gemini API 호출
        Map<String, Object> payload = buildEmergencyRecommendationPayload(prompt);
        String rawResponse = geminiRestClient.callGeminiApi(payload);

        // Gemini 응답 파싱
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String innerJson = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            JsonNode inner = objectMapper.readTree(innerJson);

            String hpid = inner.path("hpid").asText();
            String reason = inner.path("recommendedReason").asText();

            return new GeminiEmergencyRecommendationResponse(hpid, reason);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Gemini Payload
    private Map<String, Object> buildEmergencyRecommendationPayload(String prompt) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(
                Map.of("role", "user", "parts", List.of(Map.of("text", prompt)))
        ));
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        properties.put("hpid", Map.of("type", "string"));
        properties.put("recommendedReason", Map.of("type", "string"));
        responseSchema.put("properties", properties);
        responseSchema.put("required", List.of("hpid", "recommendedReason"));
        payload.put("generationConfig", Map.of(
                "responseMimeType", "application/json",
                "responseSchema", responseSchema
        ));
        return payload;
    }
}
