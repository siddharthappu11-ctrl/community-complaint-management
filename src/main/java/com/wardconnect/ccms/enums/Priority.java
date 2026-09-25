package com.wardconnect.ccms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * OOP Concept: Enum
 * Defines complaint urgency/priority levels.
 */
public enum Priority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Priority fromValue(String value) {
        if (value == null) return null;
        for (Priority p : Priority.values()) {
            if (p.value.equalsIgnoreCase(value.trim()) || p.name().equalsIgnoreCase(value.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
