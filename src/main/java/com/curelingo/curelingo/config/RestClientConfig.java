package com.curelingo.curelingo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * Gemini API 전용 RestClient 인스턴스를 Spring Bean으로 등록합니다.
     * baseUrl은 application.properties 또는 .env에 정의된 gemini.api.url을 사용합니다.
     */
    @Bean
    public RestClient geminiRestClientInstance(@Value("${gemini.api.url}") String geminiApiUrl) {
        return RestClient.builder()
                .baseUrl(geminiApiUrl)
                .build();
    }
}
