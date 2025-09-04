package travelbot.demo.payloads;

import java.time.Instant;

public record ConversationResponse(
        Long id,
        Long customerId,
        Instant startedAt,
        Instant closedAt
) {
}
