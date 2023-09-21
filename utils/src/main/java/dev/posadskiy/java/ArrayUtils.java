package dev.posadskiy.java;

public class ArrayUtils {
    
    public static int min(int[] values) {
        if (values.length == 0) {
            return -1;
        }
        
        var min = values[0];

        for (int i = 1; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        
        return min;
    }
    
    public static int max(int[] values) {
        if (values.length == 0) {
            return -1;
        }
        
        var max = values[0];

        for (int i = 1; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        
        return max;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    
}
