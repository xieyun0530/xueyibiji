package com.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vn0790t on 2020/6/8.
 */
public class JvmDemo2 {

    public static void main(String[] args) {
        //-Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
        byte[] b = null;
        for (int i = 0; i < 10; i++) {
            b = new byte[1 * 1024 * 1024];
        }
        Map<Integer, Integer> map = new HashMap<>(16);
        map.put(16, 16);
        map.put(32, 32);
        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }
    }
}
