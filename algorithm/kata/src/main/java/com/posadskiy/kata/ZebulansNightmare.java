package com.posadskiy.kata;

public class ZebulansNightmare {
    public static String zebulansNightmare(final String functionName) {
        if (functionName.isEmpty()) {
            return "";
        }
        if (functionName.length() == 1) {
            return functionName;
        }

        var result = new StringBuilder(String.valueOf(functionName.charAt(0)));
        for (int i = 1; i < functionName.length() + 1; ++i) {
            if (i == functionName.length()) {
                continue;
            }

            if (functionName.charAt(i) == '_') {
                continue;
            }

            if ((functionName.charAt(i - 1) == '_')) {
                result.append(String.valueOf(functionName.charAt(i)).toUpperCase());
                continue;
            }

            result.append(functionName.charAt(i));
        }

        return result.toString();
    }
}
