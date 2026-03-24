package com.suanfa.sort;

import java.util.Arrays;

public class InsertSort {

    public static void main(String[] args) {
//        int arr[] = {9, 5, 6, 8, 0, 3, 7, 1};
        int arr[] = {7, 8, 9, 0, 4, 3, 1, 2, 5, 10};
//        sortInsert(arr);
        shellSort(arr);
    }

    /**
     * 希尔排序
     *
     * @param arr
     */
    public static void shellSort(int arr[]) {
        int insertNumber = 0;
        int insertNumber1 = 0;
        int insertNumber2 = 0;
        int number = 0;

        //每次对k除以2进行分组，add /= 2除以2且把除以2的值赋给add
        for (int add = arr.length / 2; add >= 1; add /= 2) {
            insertNumber++;
            for (int i = add; i < arr.length; i++) {
                insertNumber1++;
                //要发的牌，这里是分组中要发的牌第一次等于数组的一半
                int data = arr[i];
                //这里注意从尾部开始比较，省去了数组移动开销，这里是已经插好牌的结束位置
                //这里注意是分组后手里牌的下标
                int j = i - add;
                //这里需要注意是减增量add，因为是以增量add来分组的
                for (; j >= 0; j -= add) {
                    //如果已经排序好的牌大于要发的牌，这把这个排序好的牌往后一动一个位置空出来来放要发的牌
                    if (arr[j] > data) {
                        insertNumber2++;
                        //这里注意是j+add，是与增量add分组的数据进行比较牌的大小
                        arr[j + add] = arr[j];
                    } else {
                        //这里如果发的牌小于或者等于已经排序好的牌，这直接跳过
                        number++;
                        break;
                    }
                }
                //这里需要注意是j+add表示和add增量的数据分组的牌进行兑换
                //这里如果是j+add=i表示走了break，data就放入到自身的位置，说明是data在数组中是有序的
                arr[j + add] = data;
                System.out.println(Arrays.toString(arr));
            }
        }
        System.out.println(insertNumber);
        System.out.println(insertNumber1);
        System.out.println(insertNumber2);
        System.out.println(number);
    }

    public static void sortInsert(int arr[]) {
        int number = 0;
        int insertNumber = 0;
        int insertNumber1 = 0;

        //这里i=1，表示第一张牌不用比较
        for (int i = 1; i < arr.length; i++) {
            insertNumber++;
            //要发的牌
            int data = arr[i];
            //这里注意从尾部开始比较，省去了数组移动开销，这里是已经插好牌的结束位置
            int j = i - 1;
            for (; j >= 0; j--) {
                insertNumber1++;
                //如果已经排序好的牌大于要发的牌，这把这个排序好的牌往后一动一个位置空出来来放要发的牌
                if (arr[j] > data) {
                    arr[j + 1] = arr[j];
                } else {
                    //这里如果发的牌小于或者等于已经排序好的牌，这直接跳过
                    number++;
                    break;
                }
            }
            //这里把要发的牌插入到有序的位置，这里最好的情况是j+1=i，也就是放入到排序好的牌的末尾
            //这里j+1是因为上面的j--了把位置往后移动了一位，所以这里需要加回来
            //这里如果是j+1=i表示走了break，data就放入到自身的位置，说明是data在数组中是有序的
            arr[j + 1] = data;
            System.out.println(Arrays.toString(arr));
        }
        System.out.println(number);
        System.out.println(insertNumber);
        System.out.println(insertNumber1);
    }
}
