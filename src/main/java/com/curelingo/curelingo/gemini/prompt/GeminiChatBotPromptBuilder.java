package com.curelingo.curelingo.gemini.prompt;

import com.curelingo.curelingo.gemini.chatbot.dto.ChatMessage;
import java.util.List;

public class GeminiChatBotPromptBuilder {
    /**
     * Gemini 프롬프트 빌더.
     * - 의료/건강 관련 대화만 답변
     * - 그 외 주제는 일관적으로 거절
     * - Markdown/개행문자/특수기호(별표, #, 백틱 등) 금지
     * - 모든 답변은 평문, 자연스러운 한국어 문장으로 출력
     * - 최근 대화 맥락을 messages로 전달
     */
    public static String buildPrompt(List<ChatMessage> messages) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(
                """
                You are a medical triage chatbot for Korean users.
                Your task:
                - ONLY answer health, symptom, medical emergency, hospital/clinic, or medical department questions.
                - If the user asks about anything NOT related to medical or health topics, politely respond in Korean: "죄송합니다. 저는 의료 정보(응급/진료/증상/병원 안내)만 답변할 수 있습니다."
                - Do NOT use Markdown symbols (*, **, #, backticks, code block) or newlines (\\n) in your answers.
                - ALWAYS write in plain, natural Korean sentences. 
                - NEVER provide advice, jokes, or information unrelated to healthcare.
                - For each user input, determine if it is an emergency. If so, recommend going to an ER and explain why. If not, suggest the most relevant medical department.
                - Be concise and clear.
                """
        );

        prompt.append("\n\n대화 기록(Conversation history):\n");
        for (ChatMessage msg : messages) {
            prompt.append(msg.role()).append(": ").append(msg.content()).append(" ");
        }
        prompt.append("\n\n이전 맥락을 참고해 위 지침을 엄수하며 답변하세요. (Please answer according to the above instructions.)");
        return prompt.toString();
    }
}
