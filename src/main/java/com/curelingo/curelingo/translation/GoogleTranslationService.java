package com.curelingo.curelingo.translation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
@Slf4j
public class GoogleTranslationService {

    @Value("${google.translation.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final TranslationCache cache;
    private static final String GOOGLE_TRANSLATE_URL = "https://translation.googleapis.com/language/translate/v2";

    public GoogleTranslationService(RestTemplate restTemplate, TranslationCache cache) {
        this.restTemplate = restTemplate;
        this.cache = cache;
    }

    /**
     * 한국어 텍스트를 영어로 번역합니다. (캐시 지원)
     * 
     * @param koreanText 번역할 한국어 텍스트
     * @return 번역된 영어 텍스트
     */
    public String translateToEnglish(String koreanText) {
        if (koreanText == null || koreanText.trim().isEmpty()) {
            return null;
        }

        // 캐시에서 확인
        if (cache.contains(koreanText)) {
            String cachedResult = cache.get(koreanText);
            return cachedResult;
        }

        try {
            // Google Translate API 요청 URL 구성
            String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_TRANSLATE_URL)
                    .queryParam("key", apiKey)
                    .queryParam("q", koreanText)
                    .queryParam("source", "ko")  // 한국어
                    .queryParam("target", "en")  // 영어
                    .queryParam("format", "text")
                    .build()
                    .toUriString();

            // API 호출
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                
                if (data != null) {
                    List<Map<String, Object>> translations = (List<Map<String, Object>>) data.get("translations");
                    
                    if (translations != null && !translations.isEmpty()) {
                        String translatedText = (String) translations.get(0).get("translatedText");
                        
                        // 캐시에 저장
                        cache.put(koreanText, translatedText);
                        
                        return translatedText;
                    }
                }
            }

            return koreanText; // 번역 실패 시 원본 반환

        } catch (Exception e) {
            log.error("번역 중 오류 발생: {}", e.getMessage(), e);
            return koreanText; // 오류 시 원본 반환
        }
    }

    /**
     * 여러 텍스트를 한 번에 번역합니다. (API 호출 최소화)
     * 
     * @param koreanTexts 번역할 한국어 텍스트 리스트
     * @return 번역된 영어 텍스트 리스트
     */
    public List<String> translateMultipleToEnglish(List<String> koreanTexts) {
        if (koreanTexts == null || koreanTexts.isEmpty()) {
            return koreanTexts;
        }

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GOOGLE_TRANSLATE_URL)
                    .queryParam("key", apiKey)
                    .queryParam("source", "ko")
                    .queryParam("target", "en")
                    .queryParam("format", "text");

            // 여러 텍스트를 한 번에 전송
            for (String text : koreanTexts) {
                if (text != null && !text.trim().isEmpty()) {
                    builder.queryParam("q", text);
                }
            }

            String url = builder.build().toUriString();

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                
                if (data != null) {
                    List<Map<String, Object>> translations = (List<Map<String, Object>>) data.get("translations");
                    
                    if (translations != null && translations.size() == koreanTexts.size()) {
                        return translations.stream()
                                .map(translation -> (String) translation.get("translatedText"))
                                .toList();
                    }
                }
            }

            // 일괄 번역 실패, 개별 번역으로 대체
            return koreanTexts.stream()
                    .map(this::translateToEnglish)
                    .toList();

        } catch (Exception e) {
            log.error("일괄 번역 중 오류 발생: {}", e.getMessage(), e);
            return koreanTexts; // 오류 시 원본 반환
        }
    }

    /**
     * API 호출 제한을 위한 지연 처리
     */
    private void delayForRateLimit() {
        try {
            Thread.sleep(100); // 100ms 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 