package com.suanfa.chart;

import java.util.Scanner;
import java.util.Stack;

/**
 * 深度优先
 */
public class DFS {

    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int n; // 地图的行
    int m; // 地图的列
    int dx; // 安琪的位置x
    int dy; // 安琪的位置y
    int data[][]; // 邻接矩阵 存储从1开始

    int minStep = Integer.MAX_VALUE; // 要走的最小步数,求最小的数 你最开始是不是要赋值一个最大的数

    boolean mark[][]; // 标记数据 走过的位置
    //栈存储当前走过的最小路径下
    Stack<Point> stackUrlIndex = new Stack();


    //四个方向消除if else { 0, 1 }前 { 1, 0 }右边 { 0, -1 }后面, { -1, 0 }左边
    int next[][] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public DFS(int n, int m, int dx, int dy, int[][] data, boolean[][] mark) {
        this.n = n;
        this.m = m;
        this.dx = dx;
        this.dy = dy;
        this.data = data;
        this.mark = mark;
    }

    public void dfs(int x, int y, int step, Stack<Point> stack) { // x,y表示我的位置，step,当前走过的路径长度
        if (x == dx && y == dy) {        //枚举了所有的路径
            if (step < minStep) {
                minStep = step;
                stackUrlIndex.clear();
                stackUrlIndex = (Stack) stack.clone();
            }
            System.out.println(step);
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nextx = x + next[i][0];
            int nexty = y + next[i][1];
            if (nextx < 1 || nextx > n || nexty < 1 || nexty > m)
                continue;
            if (data[nextx][nexty] == 0 && !mark[nextx][nexty]) { // 表示可以走 每个点都有4个方向，
                //如果当前路径已经大于最小路径了，就不需要继续往下走了
                if (step >= minStep) {
                    return;
                }
                // 这里有三行代码
                mark[nextx][nexty] = true;
                Point point = new Point(nextx, nexty);
                stack.add(point);
                dfs(nextx, nexty, step + 1, stack);
                // 回溯，让其递归的时候需要回溯，如果不加就会尾递归直接结束
                mark[nextx][nexty] = false;
                //往回走的需要出栈
                stack.pop();

            }
        }
        return;
    }

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int n = 5;
        int m = 4;

        int data[][] = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                data[i][j] = cin.nextInt();
            }
        }
        int dx = cin.nextInt();
        int dy = cin.nextInt();

        boolean mark[][] = new boolean[n + 1][m + 1];
        int x = cin.nextInt();
        int y = cin.nextInt();

        mark[x][y] = true;        //我的起始位置
        DFS dfs = new DFS(n, m, dx, dy, data, mark);
        Stack<Point> stack = new Stack<>();
        dfs.dfs(x, y, 0, stack);
        System.out.println(dfs.minStep);
        for (Point p : dfs.stackUrlIndex) {
            System.out.println("下标路径：" + p.x + "\t" + p.y);
        }
    }
}
/*
0 0 1 0
0 0 0 0
0 0 0 0
0 1 0 0
0 0 0 1
4 3
1 1
 * 
 */
