package com.excel.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
@Slf4j
public class ExcelRunner implements ApplicationRunner {

    /**
     * 基础游戏 配置3
     */
    public final Map<Integer, List<Integer>> BASE_PLAY_THREE = new HashMap<>();
    /**
     * 基础游戏 配置4
     */
    public final Map<Integer, List<Integer>> BASE_PLAY_FOUR = new HashMap<>();
    /**
     * 基础游戏 配置5
     */
    public final Map<Integer, List<Integer>> BASE_PLAY_FIVE = new HashMap<>();
    /**
     * 基础游戏 配置6
     */
    public final Map<Integer, List<Integer>> BASE_PLAY_SIX = new HashMap<>();
    /**
     * 基础游戏 配置7
     */
    public final Map<Integer, List<Integer>> BASE_PLAY_SEVEN = new HashMap<>();


    /**
     * 免费游戏 配置3
     */
    public final Map<Integer, List<Integer>> FREE_PLAY_THREE = new HashMap<>();
    /**
     * 免费游戏 配置4
     */
    public final Map<Integer, List<Integer>> FREE_PLAY_FOUR = new HashMap<>();
    /**
     * 免费游戏 配置5
     */
    public final Map<Integer, List<Integer>> FREE_PLAY_FIVE = new HashMap<>();
    /**
     * 免费游戏 配置6
     */
    public final Map<Integer, List<Integer>> FREE_PLAY_SIX = new HashMap<>();
    /**
     * 免费游戏 配置7
     */
    public final Map<Integer, List<Integer>> FREE_PLAY_SEVEN = new HashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (InputStream inBase = ExcelRunner.class.getResourceAsStream("/BaseGame.xlsx");
             InputStream inPlay = ExcelRunner.class.getResourceAsStream("/BaseGame.xlsx")) {
            List<ExcelRunnerDTO> basePalyList = EasyExcel.read(inBase).sheet(0).head(ExcelRunnerDTO.class).doReadSync();
            int start = 1;
            int end = 7;
            if (CollectionUtils.isNotEmpty(basePalyList)) {
                //基础游戏 配置3
                setMap(basePalyList, BASE_PLAY_THREE, start, end);
                //基础游戏 配置4
                setMap(basePalyList, BASE_PLAY_FOUR, start += 7, end += 7);
                //基础游戏 配置5
                setMap(basePalyList, BASE_PLAY_FIVE, start += 7, end += 7);
                //基础游戏 配置6
                setMap(basePalyList, BASE_PLAY_SIX, start += 7, end += 7);
                //基础游戏 配置7
                setMap(basePalyList, BASE_PLAY_SEVEN, start + 7, end + 7);

            }
            List<ExcelRunnerDTO> freePalyList = EasyExcel.read(inPlay).sheet(1).head(ExcelRunnerDTO.class).doReadSync();
            if (CollectionUtils.isNotEmpty(freePalyList)) {
                start = 1;
                end = 7;
                //免费游戏 配置3
                setMap(freePalyList, FREE_PLAY_THREE, start, end);
                //免费游戏 配置4
                setMap(freePalyList, FREE_PLAY_FOUR, start += 7, end += 7);
                //免费游戏 配置5
                setMap(freePalyList, FREE_PLAY_FIVE, start += 7, end += 7);
                //免费游戏 配置6
                setMap(freePalyList, FREE_PLAY_SIX, start += 7, end += 7);
                //免费游戏 配置7
                setMap(freePalyList, FREE_PLAY_SEVEN, start + 7, end + 7);
            }
        } finally {
            log.info("加载excel完成");
        }
    }

    private void setMap(List<ExcelRunnerDTO> excelRunnerDTOList, Map<Integer, List<Integer>> map, int start, int end) {
        List<ExcelRunnerDTO> list = IntStream.range(start, end).mapToObj(excelRunnerDTOList::get)
                .collect(Collectors.toList());
        int key = 1; // 从1开始的键
        for (ExcelRunnerDTO excelRunnerDTO : list) {
            List<Integer> values = new ArrayList<>();
            values.add(excelRunnerDTO.getReal2());
            values.add(excelRunnerDTO.getReal3());
            values.add(excelRunnerDTO.getReal4());
            values.add(excelRunnerDTO.getReal5());
            values.add(excelRunnerDTO.getReal6());
            values.add(excelRunnerDTO.getReal7());
            values.add(excelRunnerDTO.getReal8());
            values.add(excelRunnerDTO.getReal9());
            values.add(excelRunnerDTO.getReal10());
            values.add(excelRunnerDTO.getReal12());
            values.add(excelRunnerDTO.getReal12());
            values.add(excelRunnerDTO.getReal13());
            values.add(excelRunnerDTO.getReal14());
            map.put(key++, values);
        }
    }
}
