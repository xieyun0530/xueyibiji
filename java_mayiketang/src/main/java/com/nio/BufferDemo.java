package com.nio;

import java.nio.ByteBuffer;

/**
 * Created by vn0790t on 2020/5/26.
 */
public class BufferDemo {

    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("容量：" + buf.capacity());
        System.out.println("可用空间：" + buf.limit());
        System.out.println("位置：" + buf.position());

        //储存数据
        buf.put("abcde".getBytes());
        System.out.println("容量：" + buf.capacity());
        System.out.println("可用空间：" + buf.limit());
        System.out.println("位置：" + buf.position());

        //开启读取模式
        buf.flip();
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        String str = new String(bytes, 0, bytes.length);
        System.out.println("使用flip读取模式：" + str);

        //开启重复读取
        buf.rewind();
        System.out.println("容量：" + buf.capacity());
        System.out.println("可用空间：" + buf.limit());
        System.out.println("位置：" + buf.position());
        byte[] bytesRewind = new byte[buf.limit()];
        buf.get(bytesRewind);
        String strRewind = new String(bytesRewind,0,bytesRewind.length);
        System.out.println("使用rewind重复读取模式："+strRewind);

        System.out.println("-----------清空缓冲区-------------");
        buf.clear();
        System.out.println("容量：" + buf.capacity());
        System.out.println("可用空间：" + buf.limit());
        System.out.println("位置：" + buf.position());
        System.out.println((char)buf.get());
    }
}
