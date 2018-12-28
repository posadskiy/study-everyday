package dev.posadskiy.kata;

class Persist {
    static int persistence(long n) {
        int count = 0;
        while (n > 9) {
            count++;
            n = String.valueOf(n).chars().reduce(1, (a, b) -> a * Character.getNumericValue(b)); // a * (b - '0')
        }

        return count;
    }
}
