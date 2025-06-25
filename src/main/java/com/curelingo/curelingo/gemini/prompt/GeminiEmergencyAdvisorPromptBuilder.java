package com.curelingo.curelingo.gemini.prompt;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class GeminiEmergencyAdvisorPromptBuilder {
    /**
     * 인근 병원 가용병상 정보로 Gemini에게 프롬프트를 구성.
     * beds, hpid, distanceKm만 포함 (tel, 이름 등은 제외)
     */
    public static String buildPrompt(List<EmergencyBedStatus> beds) {
        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bedsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    beds.stream().map(b -> Map.of(
                            "hpid", b.getHpid(),
                            "distanceKm", b.getDistanceKm(),
                            "beds", b.getBeds()
                    )).toList()
            );
            sb.append("다음은 현위치 기준 가까운 응급실의 HPID, 거리, 그리고 가용병상 정보입니다.\n\n");
            sb.append("각 병원은 아래와 같습니다:\n");
            sb.append(bedsJson);
            sb.append("\n\n이 중에서 가용병상(여유)과 거리 모두를 고려해 가장 추천하는 응급실 한 곳의 hpid와 추천 이유를 JSON으로 답변하세요.\n");
            sb.append("응답 예시: {\"hpid\": \"선택한병원HPID\", \"recommendedReason\": \"추천이유(한문장)\"}\n");
        } catch (Exception e) {
            sb.append("병원 정보 변환 실패");
        }
        return sb.toString();
    }
}
