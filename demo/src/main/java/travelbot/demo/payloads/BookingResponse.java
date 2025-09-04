package travelbot.demo.payloads;

import travelbot.demo.enums.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        Long id,
        Long customerId,
        Long conversationId,
        String productSummary,
        Integer priceCents,
        BookingStatus status,
        Instant createdAt
) {
}
