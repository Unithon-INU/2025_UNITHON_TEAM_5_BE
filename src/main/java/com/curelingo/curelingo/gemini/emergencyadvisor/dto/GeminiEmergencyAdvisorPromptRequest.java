package com.curelingo.curelingo.gemini.emergencyadvisor.dto;

import java.util.List;

public record GeminiEmergencyAdvisorPromptRequest(List<Content> content) {
    public record Content(List<Part> parts, String role) {
        public record Part(String text) {
        }
    }
}
