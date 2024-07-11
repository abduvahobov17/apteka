package com.asgardiateam.aptekaproject.utils;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.ibm.icu.text.Transliterator;

public final class Utils {

    public static final String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
    public static final String LATIN_TO_CYRILLIC = "Latin-Cyrillic";

    public static String toLatin(String cyrillic) {
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return toLatinTrans.transliterate(cyrillic);
    }

    public static String toCyrillic(String latin) {
        Transliterator toLatinTrans = Transliterator.getInstance(LATIN_TO_CYRILLIC);
        return toLatinTrans.transliterate(latin);
    }

    public static String buildLike(String name) {
        StringBuilder result = new StringBuilder("");
        if (name.length() < 1)
            return result.toString();
        for (int i = 0; i < name.length() - 1; i++) {
            result.append(name.charAt(i)).append("%");
        }
        return result.append(name.charAt(name.length() - 1)).toString();
    }

    private Utils() {
        throw new AptekaException("Utility class");
    }
}
