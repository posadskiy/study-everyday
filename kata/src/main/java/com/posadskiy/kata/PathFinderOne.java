package com.posadskiy.kata;

import java.util.Arrays;
import java.util.List;

/**
 * You are at position [0, 0] in maze NxN and you can only move in one of the four cardinal directions (i.e. North, East, South, West).
 * Return true if you can reach position [N-1, N-1] or false otherwise.
 * <p>
 * Empty positions are marked . Walls are marked W. Start and exit positions are empty in all test cases.
 * <p>
 * <a href="https://www.codewars.com/kata/5765870e190b1472ec0022a2/java">Link</a>
 */
public class PathFinderOne {

    static boolean pathFinder(String maze) {
        int[][] values = parseMaze(maze);
        int[][] results = calculatePaths(values);

        recalculatePaths(results, values);

        final int length = values.length;
        final int width = values[0].length;
        return results[length - 1][width - 1] == 0;
    }

    private static int[][] parseMaze(String maze) {
        final List<String[]> list = Arrays.stream(maze.split("\\n")).map(value -> value.split("")).toList();
        final int length = list.size();
        final int width = list.get(0).length;
        int[][] values = new int[length][width];
        final int[] i = {0};
        list.forEach(row -> {
            final int[] j = {0};
            Arrays.stream(row).forEach(value -> values[i[0]][j[0]++] = value.equals(".") ? 0 : 1);
            i[0]++;
        });

        return values;
    }

    private static int[][] calculatePaths(int[][] values) {
        final int length = values.length;
        final int width = values[0].length;
        int[][] results = new int[length][width];
        for (int k = 1; k < length; ++k) {
            results[k][0] = results[k - 1][0] + Math.abs(values[k - 1][0] - values[k][0]);
        }
        for (int k = 1; k < width; ++k) {
            results[0][k] = results[0][k - 1] + Math.abs(values[0][k - 1] - values[0][k]);
        }
        for (int k = 1; k < length; ++k) {
            for (int m = 1; m < width; ++m) {
                final int leftValue = results[k - 1][m] + Math.abs(values[k - 1][m] - values[k][m]);
                final int topValue = results[k][m - 1] + Math.abs(values[k][m - 1] - values[k][m]);
                results[k][m] = Math.min(leftValue, topValue);
            }
        }
        return results;
    }

    private static void recalculatePaths(int[][] results, int[][] values) {
        final int length = values.length;
        final int width = values[0].length;
        for (int l = 1; l < Math.max(length, width); ++l) {
            for (int k = 0; k < length; ++k) {
                for (int m = 0; m < width; ++m) {
                    final int topValue = k - 1 >= 0 ? results[k - 1][m] + Math.abs(values[k - 1][m] - values[k][m]) : Integer.MAX_VALUE;
                    final int downValue =
                        k + 1 < values.length ? results[k + 1][m] + Math.abs(values[k + 1][m] - values[k][m]) : Integer.MAX_VALUE;
                    final int leftValue = m - 1 >= 0 ? results[k][m - 1] + Math.abs(values[k][m - 1] - values[k][m]) : Integer.MAX_VALUE;
                    final int rightValue =
                        m + 1 < values[k].length ? results[k][m + 1] + Math.abs(values[k][m + 1] - values[k][m]) : Integer.MAX_VALUE;
                    results[k][m] = Math.min(Math.min(Math.min(Math.min(leftValue, topValue), rightValue), downValue), results[k][m]);
                }
            }
        }
    }

}
