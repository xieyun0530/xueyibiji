package com.suanfa.hash;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/3/16 20:26
 */
public class BitMap {

    int[] intData;
    int max;

    public BitMap(int max) {
        this.max = max;
        //max >> 往右移动3位 2*2*2=8，+1是因为取整的原因，需要把多余数部分的数据再开辟一个数组
        intData = new int[(max >> 5) + 1];
    }

    public void addInt(int number) {
        int array = number >> 5;
        int loc = number & 32-1;
        //接下来就是要把bit数组里面的 bisIndex这个下标的byte里面的 第loc 个bit位置为1
        //把1向左移动loc位，然后与data[loc]做或运算，就会把loc下标的0置为1
        intData[array] |= 1 << loc;
    }

    public boolean findInt(int number) {
        //除以8
        int array = number >> 5;
        int loc = number & 32-1;
        //如果原来的那个位置是0 那肯定就是0 只有那个位置是1 才行
        int flag = intData[array] & 1 << loc;
        if (flag == 0) return false;
        return true;
    }

    public void delInt(int number) {
        int array = number >> 5;
        int loc = number & 32-1;
        //删除，异或^或者使用~取反
        intData[array] = ~1 << loc;
    }

    public static void main(String[] args) {
        BitMap bitMap = new BitMap(200000001);    //10亿
        bitMap.addInt(2);
        bitMap.addInt(3);
        bitMap.addInt(65);
        bitMap.addInt(66);

        System.out.println(bitMap.findInt(3));
        System.out.println(bitMap.findInt(65));
        bitMap.delInt(65);
        System.out.println(bitMap.findInt(65));
    }
}
