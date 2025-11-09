package com.masters.PSDMWebApi.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) return null;

        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        return String.format("%02d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;

        try {
            String[] parts = dbData.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid time format: " + dbData);
            }

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse Duration from DB: " + dbData, e);
        }
    }
}

