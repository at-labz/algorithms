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
        int[] input = random.ints(1000000, 0, 10).toArray();
        int[] inputClone = input.clone();
//        System.out.println(Arrays.toString(input));
        long start = System.currentTimeMillis();
        MergeSort.bucketMerge(input);
        long end = System.currentTimeMillis();
        System.out.println("MergeSort took: " + (end - start) + "ms");
//        System.out.println(Arrays.toString(input));
        start = System.currentTimeMillis();
        Arrays.sort(inputClone);
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

    @Test
    public void testDistributionMap(){
//        int[] array = {1,1,1,3,4,5,6};
        int[] array = new Random().ints(10,100,110).toArray();
        System.out.println(Arrays.toString(array));
        MergeSort.Stats stats = MergeSort.analyze(array);
        System.out.println(stats.map);
        System.out.println("Size: "+stats.map.size());

        System.out.println("Min: "+stats.min);
        System.out.println("Max: "+stats.max);
    }


    @Test
    public void testBucketing(){
//        int[] array = {1,1,1,3,4,5,6};
        int[] array = new Random().ints(10,100,110).toArray();
        System.out.println(Arrays.toString(array));
        MergeSort.Stats stats = MergeSort.analyze(array);
        System.out.println(stats.map);
        System.out.println("Size: "+stats.map.size());

        System.out.println("Min: "+stats.min);
        System.out.println("Max: "+stats.max);
        System.out.println(MergeSort.bucket(array,stats.min,2));
    }

    @Test
    public void testBucketing2(){
        int[] array = new Random().ints(100,0,10000).toArray();
        System.out.println(Arrays.toString(array));
        MergeSort.Stats stats = MergeSort.analyze(array);
        System.out.println(stats.map);
        System.out.println("Size: "+stats.map.size());

        System.out.println("Min: "+stats.min);
        System.out.println("Max: "+stats.max);
        System.out.println(MergeSort.bucket(array,stats.min,4));
        MergeSort.bucketMerge(array);
    }

}
