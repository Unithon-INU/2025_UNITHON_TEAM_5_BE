package com.curelingo.curelingo.gemini.service;

import com.curelingo.curelingo.gemini.GeminiRestClient;
import com.curelingo.curelingo.gemini.dto.GeminiEmergencyAdvisorPromptResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiEmergencyAdvisorService {

    private final GeminiRestClient geminiRestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 응급 상황에 맞는 프롬프트를 기반으로 Gemini API에 요청을 보내고,
     * 응답에서 추천 병원 이름과 사유를 추출하여 반환합니다.
     */
    public GeminiEmergencyAdvisorPromptResponse getRecommendation(String prompt) {
        Map<String, Object> payload = buildEmergencyRecommendationPayload(prompt);
        // log.info("[GeminiEmergencyAdvisorService] Built payload for Gemini emergency recommendation: {}", payload);
        String rawResponse = geminiRestClient.callGeminiApi(payload);
        // log.info("[GeminiEmergencyAdvisorService] Raw Gemini API response: {}", rawResponse);
        try {
            // Extract the JSON string from candidates[0].content.parts[0].text
            JsonNode jsonNode = objectMapper.readTree(rawResponse);
            String innerJson = jsonNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
            GeminiEmergencyAdvisorPromptResponse response =
                    objectMapper.readValue(innerJson, GeminiEmergencyAdvisorPromptResponse.class);
            log.info("[GeminiEmergencyAdvisorService] 추천 병원: {}, 사유: {}", response.recommendedHospitalName(), response.recommendedReason());
            return response;
        } catch (Exception e) {
            log.error("[GeminiEmergencyAdvisorService] Failed to extract/parse recommended hospital", e);
            return null;
        }
    }

    /**
     * 응급실 추천을 위한 Gemini API 요청 Payload를 생성합니다.
     */
    private Map<String, Object> buildEmergencyRecommendationPayload(String prompt) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(
                new HashMap<String, Object>() {{
                    put("role", "user");
                    put("parts", List.of(new HashMap<String, String>() {{
                        put("text", prompt);
                    }}));
                }}
        ));
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, String> hospitalNameSchema = new HashMap<>();
        hospitalNameSchema.put("type", "string");
        Map<String, String> reasonSchema = new HashMap<>();
        reasonSchema.put("type", "string");
        properties.put("recommendedHospitalName", hospitalNameSchema);
        properties.put("recommendedReason", reasonSchema);
        responseSchema.put("properties", properties);
        responseSchema.put("required", List.of("recommendedHospitalName", "recommendedReason"));
        responseSchema.put("propertyOrdering", List.of("recommendedHospitalName", "recommendedReason"));
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        generationConfig.put("responseSchema", responseSchema);
        payload.put("generationConfig", generationConfig);
        return payload;
    }
}
