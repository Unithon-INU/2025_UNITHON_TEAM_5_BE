package com.curelingo.curelingo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

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

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().removeIf(c -> c instanceof StringHttpMessageConverter);
        restTemplate.getMessageConverters().add(0,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return restTemplate;
    }
}
