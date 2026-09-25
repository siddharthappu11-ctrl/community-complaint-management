package com.wardconnect.ccms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * OOP Concept: Enum
 * Represents the current status of a complaint.
 */
public enum ComplaintStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String value;

    ComplaintStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ComplaintStatus fromValue(String value) {
        if (value == null) return null;
        for (ComplaintStatus status : ComplaintStatus.values()) {
            if (status.value.equalsIgnoreCase(value.trim()) || status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
