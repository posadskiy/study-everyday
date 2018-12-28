package dev.posadskiy.kata;

class BubbleSort {

    static int[] sort(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 1; j < arr.length - i; ++j) {
                if (arr[j-1] > arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }
            }
        }

        return arr;
    }
}
