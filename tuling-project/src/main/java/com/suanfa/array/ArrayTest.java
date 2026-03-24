package com.suanfa.array;

public class ArrayTest {

    private int size;
    private int[] data;
    private int index;

    public ArrayTest(int size) {        //数组的初始化过程
        this.size = size;
        data = new int[size];        //分配的内存空间{0,0,0,0,0}
        index = 0;
    }

    public void insert(int loc, int n) {
        if (index++ < size) {
            for (int i = size - 1; i > loc; i--) {
                data[i] = data[i - 1];  //从最后一个下标开始数据往后移动，抛弃到尾部最后一个
            }
            data[loc] = n;
        }
    }

    public void delete(int loc) {
        for (int i = loc; i < size; i++) {
            if (i < size) {
                data[i] = data[i + 1];//从删除的下标开始往前移动
            }
        }
        index--;
    }

    public static void main(String[] args) {
        ArrayTest arrayTest = new ArrayTest(5);
        arrayTest.insert(2, 3);
        arrayTest.delete(2);
        System.out.println(arrayTest.data[2]);
    }
}
