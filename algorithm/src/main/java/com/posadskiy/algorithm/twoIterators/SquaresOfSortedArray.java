package com.posadskiy.algorithm.twoIterators;

/**
 * Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.
 * <p>
 * Example 1:
 * <p>
 * Input: [-4,-1,0,3,10]
 * <p>
 * Output: [0,1,9,16,100]
 * <p>
 * Explanation: After squaring, the array becomes [16,1,0,9,100].
 * After sorting, it becomes [0,1,9,16,100].
 * <p>
 * Example 2:
 * <p>
 * Input: [-7,-3,2,3,11]
 * Output: [4,9,9,49,121]
 */
public class SquaresOfSortedArray {
    public int[] sortedSquares(int[] nums) {
        var result = new int[nums.length];
        var first = 0;
        var second = nums.length - 1;

        var arrayIndex = second;

        while (second >= first) {
            if (Math.abs(nums[first]) > Math.abs(nums[second])) {
                result[arrayIndex--] = nums[first] * nums[first];
                first++;
            } else {
                result[arrayIndex--] = nums[second] * nums[second];
                second--;
            }
        }

        return result;
    }
}
