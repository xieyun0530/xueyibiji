package com.chatgpt.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * EasyPOI 导出 Excel 并设置单元格中特定字符颜色的示例
 *
 * @author chatgpt-project
 * @since 2025-12-25
 */
public class ExcelColorDemo {

    public static void main(String[] args) {
        try {

            // 使用 Workbook 和 DataHandler 处理敏感词
            exportWithDataHandler();

            System.out.println("Excel 导出成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法4: 使用 Workbook 和敏感词处理，标红显示
     * 先用 EasyPOI 导出基础数据，然后后处理单元格标红敏感词
     */
    private static void exportWithDataHandler() throws Exception {
        // 定义敏感词集合
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet.add("异常");
        sensitiveWordSet.add("警告");
        sensitiveWordSet.add("超标");

        // 创建表头
        List<ExcelExportEntity> colList = new ArrayList<>();

        ExcelExportEntity colEntity1 = new ExcelExportEntity("姓名", "name");
        colEntity1.setWidth(20);
        colList.add(colEntity1);

        ExcelExportEntity colEntity2 = new ExcelExportEntity("状态", "status");
        colEntity2.setWidth(40);
        colList.add(colEntity2);

        ExcelExportEntity colEntity3 = new ExcelExportEntity("金额", "amount");
        colEntity3.setWidth(20);
        colList.add(colEntity3);

        // 创建数据
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "张三");
        map1.put("status", "正常运行中");
        map1.put("amount", 1000);
        list.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "李四");
        map2.put("status", "异常-需要处理");
        map2.put("amount", 2000);
        list.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "王五");
        map3.put("status", "警告-请注意");
        map3.put("amount", 3000);
        list.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", "赵六");
        map4.put("status", "重要提示: 此订单金额超标，请尽快处理");
        map4.put("amount", 5000);
        list.add(map4);

        // 创建导出参数
        ExportParams params = new ExportParams();
        params.setTitle("敏感词标红报表");
        params.setSheetName("数据列表");

        // 使用 EasyPOI 导出基础数据
        Workbook workbook = ExcelExportUtil.exportExcel(params, colList, list);

        // 如果有敏感词，则后处理单元格，将敏感词标红
        if (!CollectionUtils.isEmpty(sensitiveWordSet)) {
            processSensitiveWords(workbook, sensitiveWordSet);
        }

        // 保存文件
        String outputPath = System.getProperty("user.home") + File.separator + "easypoi_datahandler_export.xlsx";
        FileOutputStream fos = new FileOutputStream(new File(outputPath));
        workbook.write(fos);
        fos.close();
        workbook.close();

        System.out.println("敏感词标红导出完成: " + outputPath);
    }

    /**
     * 处理 Workbook 中的敏感词，将敏感词标红
     */
    private static void processSensitiveWords(Workbook workbook, Set<String> sensitiveWordSet) {
        XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;

        // 创建红色字体
        XSSFFont redFont = xssfWorkbook.createFont();
        redFont.setColor(IndexedColors.RED.getIndex());
        redFont.setBold(true);

        // 遍历所有 Sheet
        for (int sheetIndex = 0; sheetIndex < xssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetIndex);

            // 遍历所有行
            for (Row row : sheet) {
                // 遍历所有单元格
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();

                        // 检查是否包含敏感词
                        boolean hasSensitiveWord = false;
                        for (String sensitiveWord : sensitiveWordSet) {
                            if (cellValue.contains(sensitiveWord)) {
                                hasSensitiveWord = true;
                                break;
                            }
                        }

                        // 如果包含敏感词，创建富文本并标红
                        if (hasSensitiveWord) {
                            XSSFRichTextString richText = new XSSFRichTextString(cellValue);

                            // 标红所有敏感词
                            for (String sensitiveWord : sensitiveWordSet) {
                                int index = 0;
                                while ((index = cellValue.indexOf(sensitiveWord, index)) != -1) {
                                    richText.applyFont(index, index + sensitiveWord.length(), redFont);
                                    index += sensitiveWord.length();
                                }
                            }

                            ((XSSFCell) cell).setCellValue(richText);
                        }
                    }
                }
            }
        }
    }
}
