package com.jaysan1292.groupproject.util;

import java.util.regex.Pattern;

/** @author Jason Recillo */
public class RegexUtils {
    private static final Pattern firstCap = Pattern.compile("(.)([A-Z][a-z]+)");
    private static final Pattern allCap = Pattern.compile("([a-z0-9])([A-Z])");

    private RegexUtils() {}

    public static String camelCaseToUnderscore(String str) {
        String s = firstCap.matcher(str).replaceAll("$1_$2");
        return allCap.matcher(s).replaceAll("$1_$2").toLowerCase();
    }
}
