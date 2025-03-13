package org.example;

import java.util.Random;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        int upBound = 100;
        int downBound = -100;

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        Random random = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(upBound - downBound) - upBound;
        }

        int minAbsValue = arr[0];
        for (int num : arr) {
            if (Math.abs(num) < Math.abs(minAbsValue)) {
                minAbsValue = num;
            }
        }

        System.out.println(minAbsValue);
    }
}
