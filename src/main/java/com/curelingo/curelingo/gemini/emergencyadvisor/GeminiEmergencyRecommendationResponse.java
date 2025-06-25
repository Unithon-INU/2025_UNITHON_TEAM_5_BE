package com.curelingo.curelingo.gemini.emergencyadvisor;

public record GeminiEmergencyRecommendationResponse(
        String recommendedHospitalHpid,   // 추천 병원 hpid
        String recommendedReason          // 추천 사유
) {
}
