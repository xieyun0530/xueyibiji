package com.jvm;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Created by vn0790t on 2020/6/8.
 */
public class JvmDemo1 {

    public static void main(String[] args) throws InterruptedException {
//        byte[] b1 = new byte[1 * 1024 * 1024];
//        System.out.println("分配了1M");
//        jvmInfo();
//        Thread.sleep(3000);
//        byte[] b2 = new byte[4 * 1024 * 1024];
//        System.out.println("分配了4m");
//        jvmInfo();

        //-Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
//        List<Object> list = new ArrayList<>();
//        Thread.sleep(3000);
//        jvmInfo();
//        for (int i = 0; i < 10; i++) {
//            System.out.println("i:"+i);
//            Byte [] bytes=	new Byte[1*1024*1024];
//            list.add(bytes);
//            jvmInfo();
//        }
//        System.out.println("添加成功...");

        String shelvesIdsA = "{640:1.0}";

        Map<Integer, BigDecimal> map = JSON.parseObject(shelvesIdsA, Map.class);
        Pair<Integer, BigDecimal> pair = null;
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            pair = Pair.of(entry.getKey(), entry.getValue());
        }
        System.out.println(pair.getKey()+"pppp"+pair.getValue());
        final Pair<Integer, BigDecimal> finalPairA = pair;
        List<BigDecimal> list = Arrays.asList(new BigDecimal(0),new BigDecimal(20));
        list.stream().forEach(item -> {
            System.out.println(item.multiply(finalPairA.getValue()));
        });
    }

    public static void jvmInfo() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("当前最大内存：" + maxMemory + ",转换为M：" + toM(maxMemory));

        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("当前空闲内存：" + freeMemory + ",转换为M：" + toM(freeMemory));

        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("当前已使用内存：" + totalMemory + ",转换为M：" + toM(totalMemory));
    }

    public static String toM(long maxMemory) {
        float num = (float) maxMemory / (1024 * 1024);
        DecimalFormat f = new DecimalFormat("0.00");
        String s = f.format(num);
        return s;
    }

}
