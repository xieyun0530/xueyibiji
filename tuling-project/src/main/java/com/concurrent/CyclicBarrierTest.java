package com.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args) {
        int count = 2;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);

        for (int i = 1; i <= count; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("线程"+ finalI +"执行开始");
                    Thread.sleep(1000);
                    cyclicBarrier.await();
                    System.out.println("线程"+ finalI +"执行结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
