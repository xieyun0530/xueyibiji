package com.nio;

import java.nio.ByteBuffer;

/**
 * Created by vn0790t on 2020/5/26.
 */
public class BufferMakeAndRest {

    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("abcde".getBytes());
        byte[] bytes = new byte[buf.limit()];
        buf.flip();
        buf.get(bytes, 0, 2);
        buf.mark();
        String str = new String(bytes,0,2);
        System.out.println(str);
        System.out.println(buf.position());

        buf.get(bytes,2,2);
        String str1 = new String(bytes,2,2);
        System.out.println(str1);
        System.out.println(buf.position());
        buf.reset();
        System.out.println("重置恢复到make位置。。。");
        System.out.println(buf.position());
    }
}
