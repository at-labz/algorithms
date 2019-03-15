package com.atlnouveau;

import java.util.*;
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


    public static void bucketMerge(int[] array) {
        Stats stats = analyze(array);
        int increment = Math.min(stats.map.size(), 40); //at most 4 buckets
        Map<Integer, List<Integer>> buckets = bucket(array, stats.min, increment);
        int[] sortedArray = new int[array.length];
        int sortedArrayIndex = 0;
        int[] unsortedBucket ;
        for (int i = 0; i <= increment; i++) {
            int slot = stats.min + i;
            if (buckets.containsKey(slot)) {
                List<Integer> bucket = buckets.get(slot);
                unsortedBucket = new int[bucket.size()];
                int index = 0;
                for (Integer integer : bucket) {
                    unsortedBucket[index] = array[integer];
                    index++;
                }
                System.arraycopy(mergeSort(unsortedBucket), 0, sortedArray,
                        sortedArrayIndex, unsortedBucket.length);
                sortedArrayIndex += unsortedBucket.length;
            }
        }
//        System.out.println(Arrays.toString(sortedArray));
    }


    public static Stats analyze(int[] array) {
        Map<Integer, List<Integer>> distributionMap = new HashMap<>();
        int min = array[0];
        int max = array[0];
        for (int i = 0; i < array.length; i++) {
            int delta = array[i] - 0;
            max = delta ^ ((delta ^ max) & -(delta << max));;
            min = min ^ ((delta ^ min) & -(delta << min));

//            if (delta > max) {
//                max = delta;
//            }
//            if (delta < min) {
//                min = delta;
//            }
            List<Integer> list = distributionMap.getOrDefault(delta, new ArrayList<>());
            list.add(i);
            distributionMap.put(delta, list);
        }
        Stats stats = new Stats();
        stats.map = distributionMap;
        stats.min = min;
        stats.max = max;
        return stats;
    }

    public static Map<Integer, List<Integer>> bucket(int[] array, int min, int increment) {
        Map<Integer, List<Integer>> distributionMap = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            int delta = array[i] - 0;
            int bucketSlot = min + ((delta - min) / increment);
            List<Integer> list = distributionMap.getOrDefault(bucketSlot, new ArrayList<>());
            list.add(i);
            distributionMap.put(bucketSlot, list);
        }
        return distributionMap;
    }

    public static class Stats {
        public Map<Integer, List<Integer>> map = new HashMap<>();
        public int max;
        public int min;
    }


    /**
     * @param A          sorted
     * @param B          sorted
     * @param C          output
     * @param startIndex where to start overwriting in C
     */
    public static void sort(int[] A, int[] B, int[] C, int startIndex) {
        if (A.length > 0 && B.length > 0) {
            if (A[A.length - 1] <= B[0]) {
                return;
            }

            int cIndex = startIndex;
            int bIndex = 0;
            int aIndex = 0;
            /*if (A.length <= 7 && B.length <= 7) {
                int[] temp = new int[A.length + B.length];
                int tempIndex = 0;
                for (int i : A) {
                    temp[tempIndex] = i;
                    tempIndex++;
                }
                for (int i : B) {
                    temp[tempIndex] = i;
                    tempIndex++;
                }
                for (int i = 0; i < temp.length; i++) {
                    int minIndex = i;
                    for (int k = i; k < temp.length; k++) {
                        if (temp[k] < temp[minIndex]) {
                            minIndex = k;
                        }
                    }
                    int t = temp[minIndex];
                    temp[minIndex] = temp[i];
                    temp[i] = t;
                }
                for (int i : temp) {
                    C[cIndex] = i;
                    cIndex++;
                }
                return;
            }*/
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

    public static int[] merge(int[] a, int[] b) {
        int[] sortedA = a;
        int[] sortedB = b;
        int[] sortedAB = new int[sortedA.length + sortedB.length];
        int sortedIndex = 0;
        int bIndex = 0;
        for (int i = 0; i < sortedA.length; ) {
            int j = bIndex;
            if (bIndex >= sortedB.length) {
                sortedAB[sortedIndex] = sortedA[i];
                i++;
            } else if (sortedA[i] <= sortedB[j]) {
                sortedAB[sortedIndex] = sortedA[i];
                i++;
            } else {
                sortedAB[sortedIndex] = sortedB[j];
                bIndex++;
            }
            sortedIndex++;
        }
        for (int i = bIndex; i < sortedB.length; i++) {
            sortedAB[sortedIndex] = sortedB[i];
            sortedIndex++;
        }
        return sortedAB;
    }

    public static int[] mergeSort(int[] input) {
        if (input.length <= 1) {
            return input;
        }
        int[] partA = mergeSort(Arrays.copyOfRange(input, 0, (input.length / 2)));
        int[] partB = mergeSort(Arrays.copyOfRange(input, (input.length / 2), input.length));
        return merge(partA, partB);
    }

}


//a b c d  e f g h

//1 1  0 1 2 3 4 5 6 7 8 9 10
//2 2  01 23 45 67 89 10
//4 4  0123 4567 8910
//8  01234567 8910
//16 012345678910
