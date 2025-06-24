package com.curelingo.curelingo.translation;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class TranslationCache {
    
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    
    /**
     * 캐시에서 번역 결과를 조회합니다.
     */
    public String get(String koreanText) {
        return cache.get(koreanText);
    }
    
    /**
     * 번역 결과를 캐시에 저장합니다.
     */
    public void put(String koreanText, String englishText) {
        if (koreanText != null && englishText != null) {
            cache.put(koreanText, englishText);
        }
    }
    
    /**
     * 캐시에 해당 텍스트가 있는지 확인합니다.
     */
    public boolean contains(String koreanText) {
        return cache.containsKey(koreanText);
    }
    
    /**
     * 캐시를 초기화합니다.
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * 캐시 크기를 반환합니다.
     */
    public int size() {
        return cache.size();
    }
} 