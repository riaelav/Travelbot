package travelbot.demo.payloads;

import travelbot.demo.enums.LeadValue;

import java.util.Map;

public record CustomerUpdateRequest(
        String name,
        LeadValue leadValue,
        Map<String, Object> preferences
) {
}
