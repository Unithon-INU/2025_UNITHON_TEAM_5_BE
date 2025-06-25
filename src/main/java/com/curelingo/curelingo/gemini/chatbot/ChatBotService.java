package com.curelingo.curelingo.gemini.chatbot;

import com.curelingo.curelingo.gemini.GeminiRestClient;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotRequest;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotResponse;
import com.curelingo.curelingo.gemini.prompt.GeminiChatBotPromptBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatBotService {
    private final GeminiRestClient geminiRestClient;

    public ChatBotResponse chat(ChatBotRequest request) {
        // 1. 프롬프트 빌드
        String prompt = GeminiChatBotPromptBuilder.buildPrompt(request.messages());

        // 2. Gemini messages 포맷 생성 (프롬프트 하나만 보냄, 메시지 누적 지원)
        List<Map<String, Object>> geminiMessages = List.of(
                Map.of("role", "user", "parts", List.of(Map.of("text", prompt)))
        );

        // 3. Gemini API 호출
        String rawResponse = geminiRestClient.callGeminiApi(geminiMessages);

        // 4. 응답에서 답변만 추출 + 후처리
        String answer = extractAnswerFromGeminiResponse(rawResponse);
        answer = postProcess(answer);

        return new ChatBotResponse(answer);
    }

    private String extractAnswerFromGeminiResponse(String rawResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawResponse);

            // Gemini: answer -> candidates[0] -> content -> parts[0] -> text
            JsonNode answerNode = root.get("answer");
            if (answerNode == null) answerNode = root;

            JsonNode candidates = answerNode.get("candidates");
            if (candidates != null && candidates.isArray() && !candidates.isEmpty()) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 마크다운, 개행, 특수문자 제거 등 후처리
    private String postProcess(String answer) {
        if (answer == null) return "";
        return answer
                .replaceAll("[*#`\\-]", "")      // 마크다운 기호 제거
                .replaceAll("\\\\n|\\n", " ")   // 개행/역슬래시 제거
                .replaceAll("\\s{2,}", " ")     // 여러 공백 하나로
                .trim();
    }
}