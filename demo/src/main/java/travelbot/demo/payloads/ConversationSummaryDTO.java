package travelbot.demo.payloads;

import travelbot.demo.enums.LeadValue;

import java.time.Instant;

public record ConversationSummaryDTO(
        Long id,
        String customerPhone,
        Instant startedAt,
        Instant closedAt,
        long messagesCount,
        LeadValue leadValue
) {
}
