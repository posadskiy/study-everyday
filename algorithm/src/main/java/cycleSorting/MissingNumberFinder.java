package cycleSorting;

import java.util.Arrays;

/**
 * Missing number in the array problem
 * <p>
 * <a href="https://leetcode.com/problems/missing-number/description/">Link</a>
 */
public class MissingNumberFinder {
    public int missingNumber(int[] nums) {
        final int[] extendedArray = Arrays.copyOf(nums, nums.length + 1);
        new CycleSorter().cycleSort(extendedArray);

        for (int i = 0; i < extendedArray.length - 1; i++) {
            if (extendedArray[i + 1] - extendedArray[i] != 1) {
                return extendedArray[i] + 1;
            }
        }

        return -1;
    }
}
