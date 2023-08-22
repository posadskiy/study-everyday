package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an n x n array, return the array elements arranged from outermost elements to the middle element, traveling clockwise.
 * <p>
 * array = [[1,2,3],
 * [4,5,6],
 * [7,8,9]]
 * snail(array) #=> [1,2,3,6,9,8,7,4,5]
 * For better understanding, please follow the numbers of the next array consecutively:
 * <p>
 * array = [[1,2,3],
 * [8,9,4],
 * [7,6,5]]
 * snail(array) #=> [1,2,3,4,5,6,7,8,9]
 * This image will illustrate things more clearly:
 * <p>
 * <p>
 * NOTE: The idea is not sort the elements from the lowest value to the highest; the idea is to traverse the 2-d array in a clockwise snailshell pattern.
 * <p>
 * NOTE 2: The 0x0 (empty matrix) is represented as en empty array inside an array [[]].
 */
public class Snail {
    public static int[] snail(int[][] array) {
        if (array.length == 0 || array[0].length == 0) {
            return new int[]{};
        }

        int lastX = array.length;
        int lastY = array.length;
        int firstX = 0;
        int firstY = 0;

        List<Integer> result = new ArrayList<>();

        while (lastX > firstX && lastY > firstY) {
            for (int i = firstY; i < lastY; ++i) {
                result.add(array[firstX][i]);
            }
            firstX++;

            for (int i = firstX; i < lastX; ++i) {
                result.add(array[i][lastY - 1]);
            }
            lastY--;

            for (int i = lastY; i > firstY; --i) {
                result.add(array[lastX - 1][i - 1]);
            }
            lastX--;

            for (int i = lastX; i > firstX; --i) {
                result.add(array[i - 1][firstY]);
            }
            firstY++;
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
