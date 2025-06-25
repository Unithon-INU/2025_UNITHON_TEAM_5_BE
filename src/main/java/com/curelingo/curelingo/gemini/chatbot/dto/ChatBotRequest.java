package com.curelingo.curelingo.gemini.chatbot.dto;

import java.util.List;

public record ChatBotRequest(
        String message,
        List<ChatMessage> messages // 이전 대화 목록 (role, content)
) {
}
