// payload/customer/CustomerResponse.java
package travelbot.demo.payloads;

import java.time.Instant;

public record CustomerResponse(
        Long id,
        String phone,
        String name,
        String leadValue,
        String preferencesJson,
        Instant lastContactedAt,
        Instant createdAt
) {
}
