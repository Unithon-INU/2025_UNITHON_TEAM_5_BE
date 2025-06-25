package com.curelingo.curelingo.gemini.chatbot.dto;

public record ChatMessage(
        String role,    // "user" or "assistant"
        String content
) {
}