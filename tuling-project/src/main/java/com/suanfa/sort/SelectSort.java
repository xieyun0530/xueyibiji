package com.suanfa.sort;

import java.util.Arrays;

public class SelectSort {

    /**
     * 选择排序的思想是，找出最小的数放到最前面，以此往下找到第二小的数放到第一小的数后面，这样循环找
     *
     * @param args
     */
    public static void main(String[] args) {
        int arr[] = {9, 5, 6, 8, 0, 3, 7, 1};
        //最小数的下标
        int indexMin = 0;
        for (int i = 0; i < arr.length; i++) {
            //这里的+1表示下一个数进行比较，找到最小的数字
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[indexMin] > arr[j]) {
                    indexMin = j;
                }
            }
            //这里把最小数与已经排序好的后面的那个数进行交换
            //这里可以优化
//            int temp = arr[indexMin]; 3a + 2b = 5a
//            arr[indexMin] = arr[i];   5a - 2b = 2b
//            arr[i] = temp;            5a - 2b = 3a
            arr[indexMin] = arr[indexMin] + arr[i];
            arr[i] = arr[indexMin] - arr[i];
            arr[indexMin] = arr[indexMin] - arr[i];

        }
        System.out.println(Arrays.toString(arr));
    }
}
