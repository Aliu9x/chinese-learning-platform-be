package com.aliu.be.common.util;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.aliu.be.common.constant.AppConstants;

/** Tiện ích định dạng và chuyển đổi ngày giờ. */
public final class DateTimeUtils {

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT);
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_TIME_FORMAT);

    public static String format(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Ngày không đúng định dạng yyyy-MM-dd: " + value, ex);
        }
    }

    public static LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value.trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Ngày giờ không đúng định dạng yyyy-MM-dd HH:mm:ss: " + value, ex);
        }
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        ZoneId zoneId = ZoneId.of(AppConstants.DEFAULT_TIME_ZONE);
        return dateTime.atZone(zoneId).toInstant();
    }

    public static LocalDateTime fromInstant(Instant instant) {
        if (instant == null) return null;
        ZoneId zoneId = ZoneId.of(AppConstants.DEFAULT_TIME_ZONE);
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
