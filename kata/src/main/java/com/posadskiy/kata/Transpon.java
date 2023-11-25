package com.posadskiy.kata;

class Transpon {

    static void transpon(int arr[][]) {
        for (int i = 0; i < arr.length; ++i) {
            for (int j = i+1; j < arr.length; ++j) {
                arr[i][j] = arr[i][j] ^ arr[j][i];
                arr[j][i] = arr[i][j] ^ arr[j][i];
                arr[i][j] = arr[i][j] ^ arr[j][i];
            }
        }
    }
}
