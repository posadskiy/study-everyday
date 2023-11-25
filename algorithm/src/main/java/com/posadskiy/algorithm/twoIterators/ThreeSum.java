package com.posadskiy.algorithm.twoIterators;

import java.util.*;

public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        final Set<List<Integer>> intermediateResult = new HashSet<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            var first = i + 1;
            var second = nums.length - 1;
            while (first < second) {
                final int sum = nums[i] + nums[first] + nums[second];
                if (sum == 0) {
                    intermediateResult.add(List.of(nums[i], nums[first], nums[second]));

                    first++;
                    second--;
                } else if (sum < 0) {
                    first++;
                } else {
                    second--;
                }
            }
        }

        return new ArrayList<>(intermediateResult);
    }
}
