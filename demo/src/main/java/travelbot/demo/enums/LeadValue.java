package travelbot.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LeadValue {
    LOW,
    MID,
    HIGH;

    @JsonCreator
    public static LeadValue from(String value) {
        return value == null ? null : LeadValue.valueOf(value.trim().toUpperCase());
    }
}
