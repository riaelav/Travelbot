// payload/customer/CustomerResponse.java
package travelbot.demo.payloads;

import java.time.Instant;
import java.util.Map;

public record CustomerResponse(
        Long id,
        String phone,
        String name,
        String leadValue,
        Map<String, Object> preferences,
        Instant lastContactedAt,
        Instant createdAt
) {
}
