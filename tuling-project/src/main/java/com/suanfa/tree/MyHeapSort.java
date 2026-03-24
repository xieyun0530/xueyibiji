package com.suanfa.tree;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @Description: 堆树
 * @Author: xiewu
 * @Date: 2022/3/10 17:29
 */
public class MyHeapSort {

    public static void main(String[] args) {
        int data[] = {8, 4, 20, 7, 3, 1, 25, 14, 17};
        heapSort(data);
        System.out.println(Arrays.toString(data));
        topK();
    }

    public static void topK() {
        //给你1亿个不重复的数字（整数，1~2^32-1），求出top10。前10大的数字，还可动态添加新数字，但总个数不会超过1亿
        //解决方式使用小顶堆，每次删除堆顶，然后进行一次堆化，选出最小的放入到堆顶
        int k = 10;
        int data[] = new int[k];
        Random r = new Random();
        long time = System.currentTimeMillis();
        int size = 0;
        boolean minTreeFlag = true;
        for (int i = 0; i < 100000000; i++) {
            int num = r.nextInt(1000000000);
            if (size < k) {
                data[size] = num;
                size++;
            } else {
                if (minTreeFlag) {
                    minHeapMethold(data);
                    minTreeFlag = true;
                }
                //如果当前数字大于堆顶也，则把堆顶替换成当前数，然后进行一次堆化，这样就把最小的放到堆顶
                // 注意：不要进行堆树排序这样会使得时间复杂度变成，排序nlogn 如果不排序logn
                if (num > data[0]) {
                    data[0] = num;
                    minHeap(data, 0, data.length - 1);
                }
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - time) + "ms");
        System.out.println(Arrays.toString(data));
    }

    //将数组按照大顶推进行堆化
    public static void heapSort(int[] data) {
        //堆化，从最后一个非叶子节点堆化，公式 长度/2+1
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            maxHeap(data, i, data.length);
        }
        //堆排序
        for (int i = data.length - 1; i > 0; i--) {
            //堆顶最后一个未被堆化的数字进行交换
            int temp = data[0];
            data[0] = data[i];
            data[i] = temp;
            //每次从堆顶往下到最后一个未被堆化的数字结束进行堆化，已经被堆化的不能再堆化了
            maxHeap(data, 0, i);
        }
    }

    //堆化，大顶堆，堆顶一定最大，但是无序，时间复杂度logn
    public static void maxHeap(int data[], int start, int end) {
        int parent = start;
        int son = parent * 2 + 1;
        while (son < end) {
            int maxSon = son;
            if (son + 1 < end && data[son] < data[son + 1]) {
                maxSon = son + 1;
            }
            if (data[parent] > data[maxSon]) {
                return;
            }
            int temp = data[maxSon];
            data[maxSon] = data[parent];
            data[parent] = temp;
            //以最大的那个子节点为父节点继续往下堆化
            parent = maxSon;
            //左子树
            son = maxSon * 2 + 1;
        }
    }

    //得到一个小顶堆树
    public static void minHeapMethold(int data[]) {
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            minHeap(data, i, data.length);
        }
    }

    //堆化，小顶推，堆顶一定最小，但是无序，时间复杂度logn
    public static void minHeap(int data[], int start, int end) {
        int parent = start;
        int son = parent * 2 + 1;
        while (son < end) {
            int maxSon = son;
            if (son + 1 < end && data[son] > data[son + 1]) {
                maxSon = son + 1;
            }
            if (data[parent] < data[maxSon]) {
                return;
            }
            int temp = data[maxSon];
            data[maxSon] = data[parent];
            data[parent] = temp;
            //以最大的那个子节点为父节点继续往下堆化
            parent = maxSon;
            //左子树
            son = maxSon * 2 + 1;
        }
    }

}
