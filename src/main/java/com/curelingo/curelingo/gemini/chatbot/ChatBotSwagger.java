package com.curelingo.curelingo.gemini.chatbot;

import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotRequest;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ChatBotSwagger {

    @Operation(
            summary = "의료 챗봇과 대화",
            description = """
                    사용자의 대화 맥락을 기반으로 응급 여부 및 진료과/응급실 방문 필요 여부를 답변하는 챗봇 API.
                    의료 관련 외 질문은 답변하지 않습니다.
                    """,
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ChatBotRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "챗봇 응답",
                            content = @Content(schema = @Schema(implementation = ChatBotResponse.class))
                    )
            }
    )
    ChatBotResponse chat(ChatBotRequest request);
}
