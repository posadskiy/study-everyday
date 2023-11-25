package com.posadskiy.kata;

public class Budget {

    public static int find_caterer(int budget, int people) {
        if (people == 0) return -1;
        double discount = people > 60 ? 0.8 : 1;
        if (people * 30 * discount <= budget) return 3;
        if (people * 20 <= budget) return 2;
        if (people * 15 <= budget) return 1;
        return -1;
    }
}
