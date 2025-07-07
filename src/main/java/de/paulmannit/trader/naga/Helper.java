package de.paulmannit.trader.naga;

import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class Helper {
    public static String getSafeString(JsonObject json, String key) {
        JsonValue value = json.get(key);
        if (value != null && value.getValueType() == JsonValue.ValueType.STRING) {
            return ((JsonString) value).getString();
        }
        return null;
    }

    public static Double getSafeDouble(JsonObject json, String key) {
        JsonValue value = json.get(key);
        if (value != null && value.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) value).doubleValue();
        }
        return null;
    }

    public static Long getSafeLong(JsonObject json, String key) {
        JsonValue value = json.get(key);
        if (value != null && value.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) value).longValue();
        }
        return null;
    }

    public static Instant parseInstant(JsonObject obj, String key) {
        try {
            String str = Helper.getSafeString(obj, key);
            return str != null ? Instant.parse(str) : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Boolean getSafeBoolean(JsonObject json, String key) {
        JsonValue value = json.get(key);
        if (value != null) {
            return switch (value.getValueType()) {
                case TRUE -> true;
                case FALSE -> false;
                default -> null;
            };
        }
        return null;
    }
}
