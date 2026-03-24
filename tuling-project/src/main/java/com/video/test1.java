package com.video;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author yjl
 * @Date 2022/1/10 15:39
 * @Version 1.0
 */



public class test1 {
    public static String[][] getPX(String args) {
        int[] rgb = new int[3];

        File file = new File(args);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        String[][] list = new String[width][height];
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];

            }
        }
        return list;

    }

    public static int compareImage(String imgPath1, String imgPath2){
        String[] images = {imgPath1, imgPath2};
        if (images.length == 0) {
            System.out.println("Usage >java BMPLoader ImageFile.bmp");
            System.exit(0);
        }

        // 分析图片相似度 begin
        String[][] list1 = getPX(images[0]);
        String[][] list2 = getPX(images[1]);
        int xiangsi = 0;
        int busi = 0;
        int i = 0, j = 0;
        for (String[] strings : list1) {
            if ((i + 1) == list1.length) {
                continue;
            }
            for (int m=0; m<strings.length; m++) {
                try {
                    String[] value1 = list1[i][j].toString().split(",");
                    String[] value2 = list2[i][j].toString().split(",");
                    int k = 0;
                    for (int n=0; n<value2.length; n++) {
                        if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
                            xiangsi++;
                        } else {
                            busi++;
                        }
                    }
                } catch (RuntimeException e) {
                    continue;
                }
                j++;
            }
            i++;
        }

        list1 = getPX(images[1]);
        list2 = getPX(images[0]);
        i = 0;
        j = 0;
        for (String[] strings : list1) {
            if ((i + 1) == list1.length) {
                continue;
            }
            for (int m=0; m<strings.length; m++) {
                try {
                    String[] value1 = list1[i][j].toString().split(",");
                    String[] value2 = list2[i][j].toString().split(",");
                    int k = 0;
                    for (int n=0; n<value2.length; n++) {
                        if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
                            xiangsi++;
                        } else {
                            busi++;
                        }
                    }
                } catch (RuntimeException e) {
                    continue;
                }
                j++;
            }
            i++;
        }
        String baifen = "";
        try {
            baifen = ((Double.parseDouble(xiangsi + "") / Double.parseDouble((busi + xiangsi) + "")) + "");
            baifen = baifen.substring(baifen.indexOf(".") + 1, baifen.indexOf(".") + 3);
        } catch (Exception e) {
            baifen = "0";
        }
        if (baifen.length() <= 0) {
            baifen = "0";
        }
        if(busi == 0){
            baifen="100";
        }

        System.out.println("相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率：" + Integer.parseInt(baifen) + "%");
        return Integer.parseInt(baifen);
    }

    public static void main(String[] args) throws InterruptedException {
        File fileAs = new File("E:\\video\\B3\\pic");
        System.out.println("文件总个数："+fileAs.listFiles().length);
        for (File fileA : fileAs.listFiles()) {
            CompletableFuture.runAsync(() -> {
                int result = 0;
                File fileBs = new File("E:\\video\\A3\\pic");
                for (File fileB : fileBs.listFiles()) {
                    int i = test1.compareImage(fileA.getPath(), fileB.getPath());
                    if (i > result) {
                        result = i;
                    }
                }
                System.out.println("相似率：" + result);
            });
        }
        Thread.sleep(100000000);
//        test.compareImage("E:\\video\\A\\pic\\mmexport1701242467415.mp4_0.jpg", "E:\\video\\B\\pic\\11月29日 (6).mp4_0.jpg");
    }
}
