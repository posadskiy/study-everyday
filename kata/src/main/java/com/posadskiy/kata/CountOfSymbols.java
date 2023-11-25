package com.posadskiy.kata;

class CountOfSymbols {
    static int count(String a, String b) {
        return b.split("[" + a + "]*").length - 1;
    }
}
