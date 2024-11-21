package com.posadskiy.kata;

import java.util.Arrays;
import java.util.HashSet;

public class FindUnique {
    public static int stray(int[] numbers) {
        Arrays.sort(numbers);
        
        if (numbers[0] == numbers[1]) {
            return numbers[numbers.length - 1];
        }
        
        return numbers[0];
    }
    
    public static int strayAnotherWay(int[] input) {
        if (input[0] != input[1]) {
            if (input[0] == input[2]) {
                return input[1];
            }
            
            return input[0];
        }

        final HashSet<Integer> numbers = new HashSet<>();

        for (int i : input) {
            numbers.add(i);
            
            if (numbers.size() == 2) {
                return i;
            }
        }
        
        throw new RuntimeException();
    }
    
    public static int bestOne(int[] input) {
        if (input[0] != input[1] && input[0] != input[2]) {
            return input[0];
        }

        for (int i : input) {
            if (i != input[0]) {
                return i;
            }
        }
        
        throw new RuntimeException();
    }
}
