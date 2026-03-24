package com.suanfa.sort;

import java.util.Arrays;

/**
 * 归并算法
 */
public class MergeSort {

    /**
     * 拆的时间复杂度logn * 并的时间复杂度n = nlogn
     *
     * @param arr
     * @param left
     * @param right
     */
    public static void sortMerge(int[] arr, int left, int right) {
        //相等了就表示只有一个数了 不用再拆了
        if (left < right) {
            //取中间值进行拆分
            int mid = (left + right) / 2;
            //左边拆分(从右往左拆分)，拆的过程的时间复杂度是logn（从右往左拆分）
            sortMerge(arr, left, mid);
            //右边拆分（从左往右拆分）
            sortMerge(arr, mid + 1, right);
            //这里合并值是从右边开始合并(因为这里是栈先执行后返回后执行的先返回)，等栈回溯到最后一次时就相当于拆成两个一起合并了
            merge(arr, left, mid, right);
            System.out.println(Arrays.toString(arr));
        }
    }

    /**
     * 并的时间复杂度为n
     *
     * @param arr
     * @param left
     * @param mid
     * @param right
     */
    public static void merge(int[] arr, int left, int mid, int right) {
        //临时数组，用来存放合并的有序数据
        int[] temp = new int[arr.length];
        //分割左边的开始位置
        int point1 = left;
        //分割右边开始位置
        int point2 = mid + 1;
        //这里为什么是left，因为这里排序是从左边到右边结束，所以开始值是left
        int tempIndex = left;
        //将左边和右边合并，使用插入排序，终止条件时左边的开始位置到左边边界，右边到右边边界
        while (point1 <= mid && point2 <= right) {
            if (arr[point1] < arr[point2]) {
                temp[tempIndex] = arr[point1];
                point1++;
            } else {
                temp[tempIndex] = arr[point2];
                point2++;
            }
            tempIndex++;
        }
        //如果左边的位置小于或者等于左边的边界值，说明左边的值还有数据没有放入到临时数组中
        // 这里一般会有一个数据还没有放进去，因为最后一个数据是没有数据可比较了
        while (point1 <= mid) {
            temp[tempIndex] = arr[point1];
            point1++;
            tempIndex++;
        }
        //这里是放入右边还没有放入的数据
        while (point2 <= right) {
            temp[tempIndex] = arr[point2];
            point2++;
            tempIndex++;
        }
        //总结：这里左边和右边只可能有一边需要放入，因为最后剩一个数据要么是左边的要么是右边的

        //这里为什么是left开始right结束，以为排序是从left开始right结束的
        for (int i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
    }

    public static void main(String[] args) {
        int arr[] = {9, 5, 6, 8, 0, 3, 7, 1};
        sortMerge(arr, 0, arr.length - 1);
    }
}
