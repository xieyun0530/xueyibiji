package com.suanfa.sort;

import java.util.Arrays;

public class BubbleSort {

    /**
     * 冒泡排序的思想，两个相邻的数进行比较，如果前一个比后一个大则他们之间调换下位置，一轮下来最大的数会被放入到末尾
     * 第二次比较的时候只需要比较到末尾前面一个数为止，以此类推
     *
     * @param args
     */
    public static void main(String[] args) {
        int arr[] = {9, 5, 6, 8, 0, 3, 7, 1};
        for (int i = 0; i < arr.length; i++) {
            //这列的-1表示最后一个数没有可以比较的了，-i表示i后面的是已经排序好的数据不用再比较了
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j + 1] < arr[j]) {
//                    int temp = arr[j + 1];
//                    arr[j + 1] = arr[j];
//                    arr[j] = temp;
                    //这里是优化 a=a+b,b=a-b,a=a-b
                    arr[j + 1] = arr[j + 1] + arr[j];
                    arr[j] = arr[j + 1] - arr[j];
                    arr[j + 1] = arr[j + 1] - arr[j];
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }
}
