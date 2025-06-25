package com.curelingo.curelingo.gemini;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiRestClient {

    private final RestClient geminiRestClientInstance;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private String geminiModel = "gemini-2.0-flash";

    /**
     * 완성된 payload를 사용해 Gemini API를 호출하고, 응답 원문(String)을 반환합니다.
     * Payload는 Gemini API 명세에 맞춰 호출자가 직접 구성해야 합니다.
     */
    public String callGeminiApi(Map<String, Object> payload) {
        log.info("[Gemini] Gemini API 요청 Payload: {}", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String payloadAsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            log.debug("[Gemini] Full Payload JSON:\n{}", payloadAsJson);
        } catch (Exception e) {
            log.error("[Gemini] Failed to serialize payload", e);
        }
        String rawResponse = geminiRestClientInstance
                .post()
                .uri("/v1beta/models/" + geminiModel + ":generateContent?key=" + geminiApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .body(String.class);
        log.debug("[Gemini] Raw Response:\n{}", rawResponse);
        return rawResponse;
    }

    public String callGeminiApi(List<Map<String, Object>> messages) {
        Map<String, Object> payload = Map.of("contents", messages);

        log.info("[Gemini] Gemini API 요청 Payload: {}", payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String payloadAsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            log.debug("[Gemini] Full Payload JSON:\n{}", payloadAsJson);
        } catch (Exception e) {
            log.error("[Gemini] Failed to serialize payload", e);
        }
        String rawResponse = geminiRestClientInstance
                .post()
                .uri("/v1beta/models/" + geminiModel + ":generateContent?key=" + geminiApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .retrieve()
                .body(String.class);
        log.debug("[Gemini] Raw Response:\n{}", rawResponse);
        return rawResponse;
    }
}
