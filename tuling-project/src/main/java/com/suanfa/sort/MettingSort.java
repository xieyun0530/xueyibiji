package com.suanfa.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 贪心算法，算法核心是，求某个问题总是做出眼前最大利益，这里求是最多排多少次会议
 * 但是如果再要求时长和次数最多，那么贪心算法就无法达到
 */
public class MettingSort {
    static class Metting {
        int number;
        int start;
        int end;

        public Metting(int number, int start, int end) {
            this.number = number;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Metting{" +
                    "number=" + number +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        List<Metting> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            Metting metting = new Metting(i, start, end);
            list.add(metting);
        }
        list.sort(new Comparator<Metting>() {
            @Override
            public int compare(Metting o1, Metting o2) {
                if (o1.end > o2.end) {
                    return 1;
                }
                return -1;
            }
        });
        //起始值，开始的位置
        int current = 0;
        for (Metting metting : list) {
            //这里就是判断开始值如果大于或者等于上一个的结束值，说明改会议满足要求，
            if (metting.start >= current) {
                System.out.println(metting.toString());
                current = metting.end;
            }
        }
    }


}
