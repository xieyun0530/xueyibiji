package com.suanfa.sort;

import java.io.*;

/**
 * 计数算法
 */
public class CountSort {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        String str = null;
        String fileName = "D:\\tuling\\【数据结构与算法-赵云】\\09\\贪心算法&动态规划\\贪心算法&动态规划\\200w.txt";
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        int[] data = new int[2100002];
        int i = 0;
        while ((str = br.readLine()) != null) {
            double a = Double.valueOf(str);
            a = a * 100;
            data[i++] = (int) a;
        }
        sort(data);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public static void sort(int[] data) throws Exception {
        int max = data.length;
        //这里需要加一，是因为分数最小的是1，所以下标时1需要比原来的数组大一个
        int[] counts = new int[max + 1];
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                counts[data[i]]++;
            }
        }
        File file = new File("D:\\java_project_github\\gitee\\java_mayiketang\\200w-csort.txt");
        Writer out = new FileWriter(file);

        for (int i = 0; i <= max; i++) {
            if (counts[i] > 0) {
                for (int j = 0; j < counts[i]; j++) {
                    out.write(((double) (i / 100.0)) + "\r\n");
                }
            }
        }
        out.close();
    }
}
