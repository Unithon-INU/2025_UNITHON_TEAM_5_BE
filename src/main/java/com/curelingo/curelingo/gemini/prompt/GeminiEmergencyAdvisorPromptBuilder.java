package com.curelingo.curelingo.gemini.prompt;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.HospitalCandidate;

import java.util.List;

/**
 * Gemini에게 응급 병원 추천 요청을 위한 프롬프트를 생성하는 유틸리티 클래스입니다.
 * 각 병원의 다양한 정보를 나열한 줄글 형태의 설명을 모델에게 전달합니다.
 */
public class GeminiEmergencyAdvisorPromptBuilder {

    public static String buildPrompt(EmergencyAdviceRequest request) {
        StringBuilder prompt = new StringBuilder("응급실 후보 병원 목록:\n");

        List<HospitalCandidate> hospitals = request.candidates();
        for (int i = 0; i < hospitals.size(); i++) {
            HospitalCandidate h = hospitals.get(i);
            prompt.append(String.format(
                    "%d. %s (전화번호: %s, 입원실 수: %s, 일반 중환자실: %s, 내과계 중환자실: %s, " +
                            "외과계 중환자실: %s, 신경계 병동 여부: %s, CT 가용 여부: %s, MRI 가용 여부: %s, " +
                            "인공호흡기 가용 여유: %s, 구급차 가용 여부: %s)\n",
                    i + 1,
                    h.dutyName(),
                    h.dutyTel3(),
                    h.inpatientRoom(),
                    h.generalICU(),
                    h.internalMedicineICU(),
                    h.surgicalICU(),
                    h.neurologyWard(),
                    h.ctAvailable(),
                    h.mriAvailable(),
                    h.ventilatorAvailable(),
                    h.ambulanceAvailable()
            ));
        }
        prompt.append("\nChoose the best hospital and briefly explain why in Korean(one short sentence).");

        return prompt.toString();
    }
}