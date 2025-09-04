package travelbot.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageRole {
    USER,
    ASSISTANT;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MessageRole from(String v) {
        return v == null ? null : valueOf(v.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
