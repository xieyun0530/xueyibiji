package com.suanfa.rec;

public class Fibonacci {

    /**
     * 递归方式
     *
     * @param n
     * @return
     */
    public static int fac(int n) {
        if (n <= 2) {
            return 1;
        }
        return fac(n - 1) + fac(n - 2);
    }

    /**
     * 尾递归
     *
     * @param pre 上一个结果 等于当前结果
     * @param res 当前结果 上一个结果加上本次结果
     * @param n
     * @return
     */
    public static int tailFac(int pre, int res, int n) {
        if (n <= 2) {
            return res;
        }
        return tailFac(res, pre + res, n - 1);
    }

    public static void main(String[] args) {
        //数据过大会出现栈溢出，因为需要回溯，这样栈空间不会释放
        System.out.println(fac(5));
        //尾递归，栈空间直接覆盖了，不会出现栈溢出
        System.out.println(tailFac(1, 1, 5));

    }
}
