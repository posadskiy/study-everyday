package dev.posadskiy.kata;

import java.util.Arrays;

/**
 * You probably know the "like" system from Facebook and other pages. People can "like" blog posts, pictures or other items. We want to create the text that should be displayed next to such an item.
 * <p>
 * Implement the function which takes an array containing the names of people that like an item. It must return the display text as shown in the examples:
 * <p>
 * []                                -->  "no one likes this"
 * ["Peter"]                         -->  "Peter likes this"
 * ["Jacob", "Alex"]                 -->  "Jacob and Alex like this"
 * ["Max", "John", "Mark"]           -->  "Max, John and Mark like this"
 * ["Alex", "Jacob", "Mark", "Max"]  -->  "Alex, Jacob and 2 others like this"
 * Note: For 4 or more names, the number in "and 2 others" simply increases.
 * <p>
 * <a href="https://www.codewars.com/kata/5266876b8f4bf2da9b000362/java">Link</a>
 */
public class WhoLikesIt {
    public static String whoLikesIt(String... names) {
        if (names.length == 0) {
            return "no one likes this";
        }

        if (names.length == 1) {
            return names[0] + " likes this";
        }

        if (names.length == 2) {
            return String.join(" and ", names) + " like this";
        }

        if (names.length == 3) {
            return names[0] + ", " + names[1] + " and " + names[2] + " like this";
        }

        return String.join(", ", Arrays.copyOfRange(names, 0, 2)) + " and " + (names.length - 2) + " others like this";
    }

    public static String best(String... names) {
        switch (names.length) {
            case 0:
                return "no one likes this";
            case 1:
                return String.format("%s likes this", names[0]);
            case 2:
                return String.format("%s and %s like this", names[0], names[1]);
            case 3:
                return String.format("%s, %s and %s like this", names[0], names[1], names[2]);
            default:
                return String.format("%s, %s and %d others like this", names[0], names[1], names.length - 2);
        }
    }
}
