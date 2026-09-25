package com.wardconnect.ccms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * OOP Concept: Enum
 * Defines complaint categories available in the community.
 */
public enum Category {
    WATER("Water"),
    ROADS("Roads"),
    STREETLIGHTS("Streetlights"),
    GARBAGE("Garbage"),
    DRAINAGE("Drainage"),
    NOISE("Noise");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category fromValue(String value) {
        if (value == null) return null;
        for (Category cat : Category.values()) {
            if (cat.value.equalsIgnoreCase(value.trim()) || cat.name().equalsIgnoreCase(value.trim())) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
