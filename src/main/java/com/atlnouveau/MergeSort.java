package com.atlnouveau;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MergeSort {

    public static void applyIteratively(int[] input) {
        if (input.length <= 1) {
            return;
        }
        int sublistSize = 1;
        while (sublistSize <= input.length) {
            int from = 0;
            int to = 0;
            while (to < input.length) {
                int startIndex = from;
                to = Math.min(input.length, from + sublistSize);
                int[] sliceA = Arrays.copyOfRange(input, from, to);                //this works since integer are copied. check doc of copyOfRange
                from = to;
                to = Math.min(input.length, from + sublistSize);
                int[] sliceB = Arrays.copyOfRange(input, from, to);
                from = to;
                sort(sliceA, sliceB, input, startIndex);
            }
            sublistSize = sublistSize * 2;
        }

    }


    public static void applyIteratively(int[] input, int threadCount) {
        if (input.length <= 1) {
            return;
        }
        int sublistSize = 1;
        while (sublistSize <= input.length) {
            int from = 0;
            int to = 0;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            while (to < input.length) {
                int startIndex = from;
                to = Math.min(input.length, from + sublistSize);
                int[] sliceA = Arrays.copyOfRange(input, from, to);                //this works since integer are copied. check doc of copyOfRange
                from = to;
                to = Math.min(input.length, from + sublistSize);
                int[] sliceB = Arrays.copyOfRange(input, from, to);
                from = to;
                executorService.submit(() -> {
                    sort(sliceA, sliceB, input, startIndex);
                });
            }
            try {
                executorService.shutdown();
                executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sublistSize = sublistSize * 2;
        }
    }


    /**
     * @param A          sorted
     * @param B          sorted
     * @param C          output
     * @param startIndex where to start overwriting in C
     */
    public static void sort(int[] A, int[] B, int[] C, int startIndex) {
        if (A.length != 0 && B.length != 0) {
            int cIndex = startIndex;
            int bIndex = 0;
            int aIndex = 0;
            for (; aIndex < A.length && bIndex < B.length; ) {
                if (A[aIndex] <= B[bIndex]) {
                    C[cIndex] = A[aIndex];
                    aIndex++;
                } else {
                    C[cIndex] = B[bIndex];
                    bIndex++;
                }
                cIndex++;
            }
            for (; aIndex < A.length; aIndex++) {
                C[cIndex] = A[aIndex];
                cIndex++;
            }
            for (; bIndex < B.length; bIndex++) {
                C[cIndex] = B[bIndex];
                cIndex++;
            }

        }


    }
}


//a b c d  e f g h

//1 1  0 1 2 3 4 5 6 7 8 9 10
//2 2  01 23 45 67 89 10
//4 4  0123 4567 8910
//8  01234567 8910
//16 012345678910
