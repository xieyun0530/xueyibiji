package com.video;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 视频抽帧
 *
 */
@Slf4j
public class VideoProcessor {

    private static final ThreadPoolExecutor POOLS = new ThreadPoolExecutor(16, 16, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024 * 1024));

    public static void main(String[] args) throws Exception {
        String filePath = "E:\\video\\B3\\";	//批量处理，视频所在文件夹
        String targetPath = "E:\\video\\B3\\pic\\";	//输出图片文件夹
        File file = new File(filePath);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (int i=0; i<files.length; i++) {
                if (files[i].isDirectory()) {
                    continue;
                }
                String fpath = files[i].getParent()+"\\"+files[i].getName();
                System.out.println(fpath);
                String fileName = files[i].getName();
                try {
                    randomGrabberFFmpegImage(fpath, targetPath, fileName.substring(0, fileName.length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void randomGrabberFFmpegImage(String filePath, String targerFilePath, String targetFileName)
            throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        int ffLength = ff.getLengthInFrames();
        System.out.println(ffLength);
//        double fr = ff.getFrameRate();  //帧率
        System.out.println("帧率: " + ff.getFrameRate());

        System.out.println("视频长度: " + ff.getLengthInTime());

        int t = 10;		//抽帧间隔
        List<CompletableFuture<Integer>> list = new ArrayList<>();
        for (int k=0; k<ffLength; k++){
            Frame f = ff.grabImage();
            if (k%t==0){
                int finalK = k;
                Frame frame = new Frame();
                BeanUtils.copyProperties(f, frame);
                frame.keyFrame = f.keyFrame;
                frame.imageWidth = f.imageWidth;
                frame.imageHeight = f.imageHeight;
                frame.imageDepth = f.imageDepth;
                frame.imageChannels = f.imageChannels;
                frame.imageStride = f.imageStride;
                Buffer[] b = deepCopyBufferArray(f.image);
                frame.image = b;
                frame.sampleRate = f.sampleRate;
                frame.audioChannels = f.audioChannels;
                frame.samples = f.samples;
                frame.opaque = f.opaque;
                CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                    doExecuteFrame(frame, targerFilePath, targetFileName, finalK);
                    return 1;
                }, POOLS);
                list.add(completableFuture);
//                doExecuteFrame(f, targerFilePath, targetFileName, k);
            }
        }
        for (CompletableFuture<Integer> completableFuture : list) {
            try {
                completableFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        ff.stop();
        stopWatch.stop();
        log.info("视频耗时：{}, {}",filePath, stopWatch.getTotalTimeSeconds());
    }

    public static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName, int index) {
        try {
            if (null == f || null == f.image) {
                return;
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();

            String imageMat = "jpg";
            String FileName = targerFilePath + File.separator + targetFileName + "_" + index + "." + imageMat;
            BufferedImage bi = converter.getBufferedImage(f);
            File output = new File(FileName);
            try {
                ImageIO.write(bi, imageMat, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public static Buffer[] deepCopyBufferArray(Buffer[] original) {
        if (original == null) {
            return null;
        }

        Buffer[] copy = new Buffer[original.length];

        for (int i = 0; i < original.length; i++) {
            if (original[i] instanceof ByteBuffer) {
                // 如果是 ByteBuffer，进行深度复制
                ByteBuffer originalBuffer = (ByteBuffer) original[i];
                ByteBuffer copyBuffer = ByteBuffer.allocate(originalBuffer.capacity());
                originalBuffer.rewind(); // 重置位置
                copyBuffer.put(originalBuffer);
                copyBuffer.flip(); // 重置位置
                copy[i] = copyBuffer;
            } else {
                // 如果是其他类型的 Buffer，根据实际情况进行处理
                // ...
            }
        }

        return copy;
    }

}
