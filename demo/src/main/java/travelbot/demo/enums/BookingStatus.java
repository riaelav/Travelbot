package travelbot.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED;

    @JsonCreator
    public static BookingStatus from(String v) {
        return v == null ? null : BookingStatus.valueOf(v.trim().toUpperCase());
    }
}
