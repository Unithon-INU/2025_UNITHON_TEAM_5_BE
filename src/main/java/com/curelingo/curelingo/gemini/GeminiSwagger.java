package com.curelingo.curelingo.gemini;

import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotRequest;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotResponse;
import com.curelingo.curelingo.gemini.emergencyadvisor.GeminiEmergencyRecommendationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Gemini API", description = "Gemini 기반 의료 챗봇 및 응급실 추천 서비스를 제공합니다.")
public interface GeminiSwagger {

    @Operation(
            summary = "Gemini 의료 챗봇 대화",
            description = "의료 상담용 챗봇과의 대화를 처리합니다."
    )
    ChatBotResponse chat(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 챗봇 요청 메시지",
                    required = true
            )
            @RequestBody ChatBotRequest request
    );

    @Operation(
            summary = "인근 응급실 Gemini 추천",
            description = "현위치 좌표와 반경(km)만 입력하면, Gemini가 최적의 응급실을 추천합니다. 응답은 추천 HPID와 이유입니다."
    )
    GeminiEmergencyRecommendationResponse recommendEmergency(
            @Parameter(description = "현재 위도", example = "37.5154") @RequestParam double lat,
            @Parameter(description = "현재 경도", example = "127.0346") @RequestParam double lng,
            @Parameter(description = "검색 반경(km)", example = "5.0") @RequestParam double radiusKm,
            @Parameter(description = "언어 (ko: 한국어, en: 영어)", example = "ko") @RequestParam(defaultValue = "ko") String language
    );
}
