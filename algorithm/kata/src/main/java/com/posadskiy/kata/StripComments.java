package com.posadskiy.kata;

import org.apache.commons.lang3.StringUtils;

class StripComments {
    static String stripComments(String text, String[] commentSymbols) {
        return text.replaceAll("\\s[" + StringUtils.join(commentSymbols, "") + "]( |\\w)*", "");
    }
}
