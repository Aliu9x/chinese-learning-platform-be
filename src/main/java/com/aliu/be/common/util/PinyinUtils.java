package com.aliu.be.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Tiện ích cơ bản để chuẩn hóa và kiểm tra Pinyin.
 * Class này không chuyển trực tiếp chữ Hán sang Pinyin; chức năng đó nên dùng thư viện chuyên dụng.
 */
public final class PinyinUtils {

    private static final Pattern PINYIN_PATTERN = Pattern.compile(
            "^[a-zA-ZüÜvV:āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ\\s'’-]+[1-5]?$"
    );

    private static final Map<Character, String> TONE_MAP = Map.ofEntries(
            Map.entry('ā', "a1"), Map.entry('á', "a2"), Map.entry('ǎ', "a3"), Map.entry('à', "a4"),
            Map.entry('ē', "e1"), Map.entry('é', "e2"), Map.entry('ě', "e3"), Map.entry('è', "e4"),
            Map.entry('ī', "i1"), Map.entry('í', "i2"), Map.entry('ǐ', "i3"), Map.entry('ì', "i4"),
            Map.entry('ō', "o1"), Map.entry('ó', "o2"), Map.entry('ǒ', "o3"), Map.entry('ò', "o4"),
            Map.entry('ū', "u1"), Map.entry('ú', "u2"), Map.entry('ǔ', "u3"), Map.entry('ù', "u4"),
            Map.entry('ǖ', "v1"), Map.entry('ǘ', "v2"), Map.entry('ǚ', "v3"), Map.entry('ǜ', "v4")
    );

    private PinyinUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidPinyin(String value) {
        if (StringUtils.isBlank(value)) return false;
        return PINYIN_PATTERN.matcher(StringUtils.normalizeWhitespace(value)).matches();
    }

    /** Chuẩn hóa ü, u: thành v và chuyển về chữ thường. */
    public static String normalize(String value) {
        if (value == null) return null;
        return StringUtils.normalizeWhitespace(value)
                .toLowerCase(Locale.ROOT)
                .replace("u:", "v")
                .replace('ü', 'v')
                .replace('’', '\'');
    }

    /** Loại bỏ dấu thanh nhưng vẫn giữ chữ v đại diện cho ü. */
    public static String removeToneMarks(String value) {
        String normalized = normalize(value);
        if (normalized == null) return null;
        return Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
    }

    /**
     * Chuyển một âm tiết có dấu sang dạng số đơn giản, ví dụ hǎo -> hao3.
     * Nên gọi riêng cho từng âm tiết.
     */
    public static String toneMarkToNumber(String syllable) {
        String normalized = normalize(syllable);
        if (StringUtils.isBlank(normalized)) return normalized;

        int tone = 5;
        StringBuilder result = new StringBuilder();
        for (char character : normalized.toCharArray()) {
            String mapped = TONE_MAP.get(character);
            if (mapped == null) {
                result.append(character);
            } else {
                result.append(mapped.charAt(0));
                tone = Character.getNumericValue(mapped.charAt(1));
            }
        }
        return result + String.valueOf(tone);
    }
}
