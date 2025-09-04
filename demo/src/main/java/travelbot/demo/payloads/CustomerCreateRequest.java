// payload/customer/CustomerCreateRequest.java
package travelbot.demo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travelbot.demo.enums.LeadValue;

import java.util.Map;

public record CustomerCreateRequest(
        @NotBlank @Size(max = 30) String phone,
        @Size(max = 255) String name,
        @NotNull LeadValue leadValue,
        Map<String, Object> preferences
) {
}
