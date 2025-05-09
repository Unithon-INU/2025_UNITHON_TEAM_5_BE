package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import com.curelingo.curelingo.gemini.prompt.GeminiEmergencyAdvisorPromptBuilder;
import com.curelingo.curelingo.gemini.service.GeminiEmergencyAdvisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyAdvisorService {

    private final GeminiEmergencyAdvisorService advisorService;

    public EmergencyAdviceResponse getRecommendedHospital(EmergencyAdviceRequest request) {
        String prompt = GeminiEmergencyAdvisorPromptBuilder.buildPrompt(request);
        String hospitalName = advisorService.getRecommendedHospitalName(prompt);
        log.info("[Gemini] Gemini API 응답 결과: {}", hospitalName);
        if (hospitalName == null) {
            log.warn("[Gemini] Gemini API returned null or missing recommended hospital");
            return new EmergencyAdviceResponse("추천 결과 없음");
        }
        return new EmergencyAdviceResponse(hospitalName);
    }
}
