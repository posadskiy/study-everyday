package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Your task in order to complete this Kata is to write a function which formats a duration, given as a number of seconds, in a human-friendly way.
 * <p>
 * The function must accept a non-negative integer. If it is zero, it just returns "now". Otherwise, the duration is expressed as a combination of years, days, hours, minutes and seconds.
 * <p>
 * It is much easier to understand with an example:
 * <p>
 * * For seconds = 62, your function should return
 * "1 minute and 2 seconds"
 * * For seconds = 3662, your function should return
 * "1 hour, 1 minute and 2 seconds"
 * For the purpose of this Kata, a year is 365 days and a day is 24 hours.
 * <p>
 * Note that spaces are important.
 * <p>
 * Detailed rules
 * <p>
 * The resulting expression is made of components like 4 seconds, 1 year, etc. In general, a positive integer and one of the valid units of time, separated by a space. The unit of time is used in plural if the integer is greater than 1.
 * <p>
 * The components are separated by a comma and a space (", "). Except the last component, which is separated by " and ", just like it would be written in English.
 * <p>
 * A more significant units of time will occur before than a least significant one. Therefore, 1 second and 1 year is not correct, but 1 year and 1 second is.
 * <p>
 * Different components have different unit of times. So there is not repeated units like in 5 seconds and 1 second.
 * <p>
 * A component will not appear at all if its value happens to be zero. Hence, 1 minute and 0 seconds is not valid, but it should be just 1 minute.
 * <p>
 * A unit of time must be used "as much as possible". It means that the function should not return 61 seconds, but 1 minute and 1 second instead. Formally, the duration specified by of a component must not be greater than any valid more significant unit of time.
 */
public class TimeFormatter {
    public static String formatDuration(int seconds) {
        if (seconds == 0) {
            return "now";
        }

        long internalSeconds = seconds;
        long years = internalSeconds / TimeUnit.DAYS.toSeconds(365);
        internalSeconds -= TimeUnit.DAYS.toSeconds(365 * years);

        long days = internalSeconds / TimeUnit.DAYS.toSeconds(1);
        internalSeconds -= TimeUnit.DAYS.toSeconds(days);

        long hours = internalSeconds / TimeUnit.HOURS.toSeconds(1);
        internalSeconds -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = internalSeconds / TimeUnit.MINUTES.toSeconds(1);
        internalSeconds -= TimeUnit.MINUTES.toSeconds(minutes);

        long sec = internalSeconds;

        List<String> result = new ArrayList<>();
        if (years != 0) {
            result.add(years + " year" + addPluralFormIfNeeded(years));
        }
        if (days != 0) {
            result.add(days + " day" + addPluralFormIfNeeded(days));
        }
        if (hours != 0) {
            result.add(hours + " hour" + addPluralFormIfNeeded(hours));
        }
        if (minutes != 0) {
            result.add(minutes + " minute" + addPluralFormIfNeeded(minutes));
        }
        if (sec != 0) {
            result.add(sec + " second" + addPluralFormIfNeeded(sec));
        }

        if (result.size() == 1) {
            return result.get(0);
        }

        return String.join(", ", result.subList(0, result.size() - 1)) + " and " + result.get(result.size() - 1);
    }

    private static String addPluralFormIfNeeded(long number) {
        return number > 1 ? "s" : "";
    }
}
