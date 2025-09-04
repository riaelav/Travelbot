package travelbot.demo.payloads;

import travelbot.demo.enums.MessageRole;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long conversationId,
        MessageRole role,
        String body,
        Instant createdAt
) {
}
