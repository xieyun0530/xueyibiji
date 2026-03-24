package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by vn0790t on 2020/5/26.
 */
public class Test {

    //使用直接缓冲区完成文件的复制（内存映射）
    public static void zhiJie() throws IOException {
        long startTime = System.currentTimeMillis();
        FileChannel inFileChannel = FileChannel.open(Paths.get("C:\\email\\My Outlook Data File(1).pst"), StandardOpenOption.READ);
        FileChannel outFileChannel = FileChannel.open(Paths.get("C:\\email\\My Outlook Data File(2).pst"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

        MappedByteBuffer inMappedByteBuffer = inFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inFileChannel.size());
        MappedByteBuffer outMappedByteBuffer = outFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, inFileChannel.size());

        byte[] dsf = new byte[inMappedByteBuffer.limit()];
        inMappedByteBuffer.get(dsf);
        outMappedByteBuffer.put(dsf);

        inFileChannel.close();
        outFileChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("直接缓冲区耗时："+(endTime-startTime));
    }

    //使用非直接缓冲区
    public static void feizhijie() throws Exception {
        long startTime = System.currentTimeMillis();
        FileInputStream inputStream = new FileInputStream("C:\\email\\My Outlook Data File(1).pst");
        FileOutputStream outputStream = new FileOutputStream("C:\\email\\My Outlook Data File(2).pst");

        FileChannel inFileChannel = inputStream.getChannel();
        FileChannel outFileChannel1 = outputStream.getChannel();

        ByteBuffer buf = ByteBuffer.allocate((int)inFileChannel.size());
        while (inFileChannel.read(buf) != -1) {
            buf.flip();
            outFileChannel1.write(buf);
            buf.clear();
        }

        inputStream.close();
        outputStream.close();
        inFileChannel.close();
        outFileChannel1.close();

        long endTime = System.currentTimeMillis();
        System.out.println("非直接缓冲区耗时："+(endTime-startTime));
    }

    //分散读取与聚集写入
    public static void test() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\walmartTonyXieProject\\EC对账系统问题总结.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();

        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);

        ByteBuffer[] bufs = {byteBuffer1, byteBuffer2};
        fileChannel.read(bufs);

        for (ByteBuffer buffer : bufs) {
            buffer.flip();
        }

        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("--------------------分割读取--------------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        RandomAccessFile outRandomAccessFile = new RandomAccessFile("C:\\walmartTonyXieProject\\EC对账系统问题总结copy.txt", "rw");
        FileChannel outFileChannel = outRandomAccessFile.getChannel();

        outFileChannel.write(bufs);
    }

    public static void main(String[] args) throws Exception {
//        zhiJie();
        feizhijie();
//        test();
    }

}
