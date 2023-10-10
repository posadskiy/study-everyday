package dev.posadskiy.kata;

public class ProductOfArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        final int[] ints = new int[nums.length];
        
        ints[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            ints[i] = ints[i-1] * nums[i-1];
        }
        
        var right = 1;
        for (int j = nums.length-1; j >= 0; j--) {
            ints[j] *= right;
            right *= nums[j];
        }
        
        return ints;
    }
}
