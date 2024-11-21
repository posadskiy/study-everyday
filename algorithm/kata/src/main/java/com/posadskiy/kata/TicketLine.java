package com.posadskiy.kata;


public class TicketLine {

    public static String Tickets(int[] peopleInLine) {

        int bill25 = 0;
        int bill50 = 0;

        for (int bill : peopleInLine) {
            switch (bill) {
                case 25: bill25++; break;
                case 50: {
                    if (bill25 < 1) return "NO";
                    bill25--;
                    bill50++;
                    break;
                }
                case 100: {
                    if (bill50 > 0 && bill25 > 0) {
                        bill50--;
                        bill25--;
                        break;
                    } else if (bill25 >= 3) {
                        bill25 -= 3;
                        break;
                    }
                    return "NO";
                }
            }
        }

        return "YES";
    }
}
