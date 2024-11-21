package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * You are given an array strarr of strings and an integer k. Your task is to return the first longest string consisting of k consecutive strings taken in the array.
 * <p>
 * #Example: longest_consec(["zone", "abigail", "theta", "form", "libe", "zas", "theta", "abigail"], 2) --> "abigailtheta"
 * <p>
 * n being the length of the string array, if n = 0 or k > n or k <= 0 return "".
 */
@Log4j2
class LongestConsec {

    static String longestConsec(String[] arr, int k) {
        if (arr.length < k || k <= 0) return "";
        if (arr.length == k) return String.join("", arr);

        int maxLength = 0;
        int maxIndex = 0;
        for (int i = 0; i < arr.length - k + 1; ++i) {
            int length = 0;
            for (int j = 0; j < k; j++) {
                length += arr[i + j].length();
            }
            if (length > maxLength) {
                maxLength = length;
                maxIndex = i;
            }
        }

        return StringUtils.join(Arrays.copyOfRange(arr, maxIndex, maxIndex + k), "");

    }

    /**
     * This solution mark as the best on codeward.com
     *
     * @param strarr
     * @param k
     * @return
     */
    static String longestConsecCandidate(String[] strarr, int k) {
        if (strarr.length == 0 || k > strarr.length || k <= 0)
            return "";

        String longestStr = "";
        for (int index = 0; index < strarr.length - k + 1; index++) {
            StringBuilder sb = new StringBuilder();
            for (int i = index; i < index + k; i++) {
                sb.append(strarr[i]);
            }
            if (sb.toString().length() > longestStr.length()) {
                longestStr = sb.toString();
            }
        }
        return longestStr;
    }


    public static void main(String[] args) {
        long start;
        long finish;
        int size = 500;
        int i;
        String[] arr = new String[size];
        for (int j = 0; j < size; j++) {
            arr[j] = RandomStringUtils.random(j);
        }

        start = System.nanoTime();
        i = 0;
        while (i < size) {
            i++;
            longestConsec(arr, i);
        }
        finish = System.nanoTime();
        long my = finish - start;
        log.info("TIME MY: " + my);

        start = System.nanoTime();
        i = 0;
        while (i < size) {
            i++;
            longestConsecCandidate(arr, i);
        }
        finish = System.nanoTime();
        long best = finish - start;
        log.info("TIME BEST: " + best);

        log.info("DIFFERENT: " + (best / (my + 0.0)));

    }
}
