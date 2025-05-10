package com.curelingo.curelingo.gemini.prompt;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.HospitalCandidate;

import java.util.List;

public class GeminiEmergencyAdvisorPromptBuilder {

    /**
     * 응급 병원 추천을 위한 프롬프트 문자열을 생성합니다.
     * 병원 후보 목록을 읽어, 모델이 이해하기 쉬운 포맷의 텍스트를 구성합니다.
     */
    public static String buildPrompt(EmergencyAdviceRequest request) {
        StringBuilder prompt = new StringBuilder("후보 병원 목록:\n");

        List<HospitalCandidate> hospitals = request.candidates();
        for (int i = 0; i < hospitals.size(); i++) {
            HospitalCandidate h = hospitals.get(i);
            prompt.append(String.format(
                    "%d. %s (거리: %dm, 가용 병상: %d/%d, 혼잡도: %s, 진료과: %s, 야간 운영: %s)\n",
                    i + 1,
                    h.name(),
                    h.distanceMeters(),
                    h.availableBeds(),
                    h.totalBeds(),
                    h.congestionLevel(),
                    h.department(),
                    h.isNightOpen() ? "가능" : "불가"
            ));
        }
        prompt.append("\nPlease choose the most appropriate hospital from the list above.");

        return prompt.toString();
    }
}
