package com.suanfa;

import java.util.stream.IntStream;

public class AlgorithmTest {

    public static void main(String[] args) {
        AlgorithmTest algorithmTest = new AlgorithmTest();
        System.out.println(algorithmTest.findMedianSortedArrays(new int[]{1,3}, new int[]{2}));
    }


    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] resultNums = IntStream.concat(IntStream.of(nums1), IntStream.of(nums2)).toArray();
        for (int i = 0; i < resultNums.length; i++) {
            for (int j = 0; j < resultNums.length - 1 - i; j++) {
                if (resultNums[j]<resultNums[j+1]) {
                    int temp = resultNums[j];
                    resultNums[j] = resultNums[j+1];
                    resultNums[j+1] = temp;
                }
            }
        }
        int mergeLength = resultNums.length;
        //合并后的长度是否为偶数
        int resultIndex = mergeLength/2;
        if(mergeLength % 2 == 0){
            return (resultNums[resultIndex-1] + resultNums[resultIndex]) / 2.0;
        }else{
            return resultNums[resultIndex];
        }
    }
}
