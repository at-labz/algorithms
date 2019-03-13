package com.atnouveau;

import com.atlnouveau.MergeSort;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class Tests {


    @Test
    public void test1() {
        Random random = new Random(System.currentTimeMillis());
        int[] input = random.ints(100_000_000, 0, 100000).toArray();
        int[] inputClone = input.clone();
//        System.out.println(Arrays.toString(input));
        long start = System.currentTimeMillis();
        MergeSort.applyIteratively(input);
        long end = System.currentTimeMillis();
        System.out.println("MergeSort took: " + (end - start) + "ms");
//        System.out.println(Arrays.toString(input));
        start = System.currentTimeMillis();
        Arrays.parallelSort(inputClone);
        end = System.currentTimeMillis();
        System.out.println("Java sort took: " + (end - start) + "ms");
//        System.out.println(Arrays.toString(inputClone));
        Assert.assertTrue(Arrays.equals(input, inputClone));
    }


    @Test
    public void test2() {
        Random random = new Random(System.currentTimeMillis());
        int[] input = random.ints(10_000_000, 0, 100000).toArray();
        int[] inputClone = input.clone();
//        System.out.println(Arrays.toString(input));
        long start = System.currentTimeMillis();
        Arrays.sort(inputClone);

        long end = System.currentTimeMillis();
        System.out.println("Java sort took: " + (end - start) + "ms");

//        System.out.println(Arrays.toString(myMergeSort));
        start = System.currentTimeMillis();
        MergeSort.applyIteratively(input);
        end = System.currentTimeMillis();
        System.out.println("MergeSort took: " + (end - start) + "ms");
//        System.out.println(Arrays.toString(input));
        Assert.assertTrue(Arrays.equals(input, inputClone));
    }
}
