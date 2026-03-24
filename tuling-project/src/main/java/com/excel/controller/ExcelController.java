package com.excel.controller;

import com.excel.dto.ExportDTO;
import com.excel.utils.ExcelUtils;
import com.excel.vo.ExportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * excel导出
 */
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

    /**
     * 动态列导出excel
     *
     * @param response
     * @param dto
     * @throws IOException
     */
    @PostMapping("/dynamicExport")
    public void exportExcelAsStream(HttpServletResponse response, @RequestBody ExportDTO dto) throws IOException {
        // 实现数据获取逻辑
        List<ExportVO> exportVOList = getDataList();
        ExcelUtils.dynamicSynExport(response, dto, exportVOList);
    }

    /**
     * 动态列导异步出excel
     *
     * @param dto
     * @throws IOException
     */
    @PostMapping("/dynamicAsyncExport")
    public String dynamicAsyncExport(@RequestBody ExportDTO dto) throws IOException {
        // 实现数据获取逻辑
        List<ExportVO> exportVOList = getDataList();
        return ExcelUtils.dynamicAsyncExport(dto, exportVOList);
    }

    /**
     * 动态列导异步出excel
     *
     * @param dto
     * @throws IOException
     */
    @PostMapping("/dynamicAsyncExportTest")
    public String dynamicAsyncExportTest(@RequestBody ExportDTO dto) {
        return ExcelUtils.dynamicAsyncExportTest(dto);
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<ExportVO> getDataList() {
        List<ExportVO> list = new ArrayList<>();
        ExportVO exportVO = new ExportVO();
        exportVO.setName("张一");
        exportVO.setAge(18);
        exportVO.setSex(1);
        list.add(exportVO);

        ExportVO exportVO1 = new ExportVO();
        exportVO1.setName("张二");
        exportVO1.setAge(18);
        exportVO1.setSex(1);
        list.add(exportVO1);

        ExportVO exportVO2 = new ExportVO();
        exportVO2.setName("张三");
        exportVO2.setAge(18);
        exportVO2.setSex(1);
        list.add(exportVO2);

        ExportVO exportVO3 = new ExportVO();
        exportVO3.setName("张四");
        exportVO3.setAge(18);
        exportVO3.setSex(1);
        list.add(exportVO3);

        ExportVO exportVO4 = new ExportVO();
        exportVO4.setName("张五");
        exportVO4.setAge(18);
        exportVO4.setSex(1);
        list.add(exportVO4);

        return list;
    }
}
