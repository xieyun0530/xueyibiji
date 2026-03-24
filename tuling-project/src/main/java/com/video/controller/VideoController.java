package com.video.controller;

import com.alibaba.fastjson.JSON;
import com.video.PhotoDigest;
import com.video.VideoProcessor;
import com.video.test1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    /**
     * 原视频路径
     */
    private final static String primaryFileVideoPath = "\\video\\primaryFile\\video\\";
    /**
     * 原视频抽帧后的图片路径
     */
    private final static String primaryFileImagePath = "\\video\\primaryFile\\image\\";
    /**
     * 剪辑后视频路径
     */
    private final static String processFileVideoPath = "\\video\\processFile\\video\\";
    /**
     * 剪辑后视频抽帧后的图片路径
     */
    private final static String processFileImagePath = "\\video\\processFile\\image\\";

    private static final ThreadPoolExecutor POOLS = new ThreadPoolExecutor(16, 16, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024 * 1024));

    /**
     * 视频对比
     *
     * @param primaryMultipartFile 原视频
     * @param processMultipartFile 剪辑后的视频
     * @return
     * @throws Exception
     */
    @RequestMapping("/videoContrast")
    public Object videoContrast(@RequestParam("primaryMultipartFile") MultipartFile primaryMultipartFile, @RequestParam("processMultipartFile") MultipartFile processMultipartFile) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //原视频
        frameExtraction(primaryFileVideoPath, primaryFileImagePath, primaryMultipartFile);
        //剪辑后视频
        frameExtraction(processFileVideoPath, processFileImagePath, processMultipartFile);

        List<String> list = getPhotoDigest();
        stopWatch.stop();
        log.info("总耗时：" + stopWatch.getTotalTimeSeconds());
        return list;
    }

    private List<String> getPhotoDigest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<String> list = new ArrayList<>();
        List<Double> listNum = new ArrayList<>();
        //对比
        File processImageFiles = new File(processFileImagePath);
        File primaryFiles = new File(primaryFileImagePath);
        List<CompletableFuture<Double>> resultList = new ArrayList<>();
        for (int i = 0; i < processImageFiles.listFiles().length; i++) {
            File processFile = processImageFiles.listFiles()[i];
            CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(new Supplier<Double>() {
                @Override
                public Double get() {
                    Double result = 0d;
                    for (File primaryFile : primaryFiles.listFiles()) {
                        Double percent = PhotoDigest.compare(PhotoDigest.getData(processFile.getPath()),
                                PhotoDigest.getData(primaryFile.getPath()));
                        if (percent > result) {
                            result = percent;
                        }
                    }
                    return result;
                }
            }, POOLS);
            resultList.add(completableFuture);
        }

        for (int i = 0; i < resultList.size(); i++) {
            CompletableFuture<Double> completableFuture = resultList.get(i);
            try {
                Double result = completableFuture.get();
                list.add("第" + i + "帧：" + result);
                log.info("第" + i + "帧：" + result);
                listNum.add(result);
            } catch (Exception e) {
                log.error("获取结果异常：", e);
            }
        }

        Double avg = listNum.stream().collect(Collectors.averagingDouble(a -> a));
        Double max = listNum.stream().max(Comparator.comparing(Function.identity())).get();
        Double min = listNum.stream().min(Comparator.comparing(Function.identity())).get();
        String avgStr = String.format("%.2f",avg);
        String maxStr = String.format("%.2f",max);
        String minStr = String.format("%.2f",min);
        list.add("对比结果平均值：" + avgStr + "最大值：" + maxStr + "最小值：" + minStr);
        log.info("对比结果：{}", JSON.toJSON(list));
        log.info("对比结果平均值：{}，最大值：{}，最小值：{}", avgStr, maxStr, minStr);
        stopWatch.stop();
        log.info("图片对比耗时：{}", stopWatch.getTotalTimeSeconds());
        return list;
    }

    public void frameExtraction(String videoPath, String imagePath, MultipartFile multipartFile) {
        try {
            File fileFolder = new File(videoPath);
            FileUtils.deleteDirectory(fileFolder);
            fileFolder.mkdirs(); // mkdirs() 会创建所有不存在的父文件夹
            String filename = multipartFile.getOriginalFilename();
            File fileVideo = new File(videoPath + filename);
            Path path = fileVideo.toPath();
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            File imagePathFile = new File(imagePath);
            FileUtils.deleteDirectory(imagePathFile);
            imagePathFile.mkdirs(); // mkdirs() 会创建所有不存在的父文件夹

            File file = new File(videoPath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        continue;
                    }
                    String fpath = files[i].getParent() + "\\" + files[i].getName();
                    String fileName = files[i].getName();
                    try {
                        VideoProcessor.randomGrabberFFmpegImage(fpath, imagePath, fileName.substring(0, fileName.length() - 4));
                    } catch (FrameGrabber.Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            log.error("抽帧异常：", e);
            throw new RuntimeException("抽帧异常");
        }
    }
}
