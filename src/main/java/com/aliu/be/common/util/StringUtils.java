package com.aliu.be.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/** Tiện ích xử lý chuỗi, không phụ thuộc Apache Commons. */
public final class StringUtils {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern NON_SLUG_CHARACTER = Pattern.compile("[^a-z0-9]+", Pattern.CASE_INSENSITIVE);

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static String trimToNull(String value) {
        if (isBlank(value)) return null;
        return value.trim();
    }

    public static String normalizeWhitespace(String value) {
        if (value == null) return null;
        return WHITESPACE.matcher(value.trim()).replaceAll(" ");
    }

    public static String capitalize(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) return null;
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    public static String slugify(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) return null;

        String withoutMarks = DIACRITICS.matcher(
                Normalizer.normalize(normalized, Normalizer.Form.NFD)
        ).replaceAll("");

        return NON_SLUG_CHARACTER.matcher(withoutMarks.toLowerCase(Locale.ROOT))
                .replaceAll("-")
                .replaceAll("^-|-$", "");
    }
}
