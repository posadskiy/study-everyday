package subset;

import java.util.*;

public class SubsetCreator {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> results = new HashSet<>();
        results.add(new ArrayList<>());

        for (int i = 0; i < nums.length; ++i) {
            Set<List<Integer>> temp = new HashSet<>();
            for (var value : results) {
                var tempList = new ArrayList<>(value);
                tempList.add(nums[i]);
                temp.add(tempList);
            }
            results.addAll(temp);
        }

        return new ArrayList<>(results);
    }
}
