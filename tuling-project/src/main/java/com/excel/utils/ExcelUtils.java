package com.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.excel.dto.ExportDTO;
import com.excel.vo.ExportAttributeDTO;
import com.excel.vo.ExportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelUtils {

    /**
     * 数据排序转换
     *
     * @param dto
     * @return
     */
    private static <T> List<List<Object>> getSortDataCovert(ExportDTO dto, List<T> exportVOList) {
        List<List<Object>> list = new ArrayList<>();
        try {
            // 使用迭代器遍历并删除满足条件的元素
            Iterator<T> iterator = exportVOList.iterator();
            while (iterator.hasNext()) {
                Object t = iterator.next();
                List<Object> subList = new ArrayList<>();
                Class tClass = t.getClass();
                for (ExportAttributeDTO exportAttributeDTO : dto.getParamsList()) {
                    Method method = tClass.getMethod("get" + capitalizeFirstLetter(exportAttributeDTO.getFieldName()));
                    Object invoke = method.invoke(t);
                    subList.add(invoke);
                }
                list.add(subList);
                iterator.remove(); // 安全地移除当前迭代的元素，数据置空，等待gc回收避免占用两份内存
            }
        } catch (Exception e) {
            log.error("数据转换异常：", e);
        }
        exportVOList.clear();
        return list;
    }


    /**
     * 自定义方法来实现首字母大写
     *
     * @param input
     * @return
     */
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    /**
     * 获取导出的动态头
     *
     * @param dto
     * @return
     */
    private static List<List<String>> getHeadList(ExportDTO dto) {
        List<List<String>> list = new ArrayList<>();
        // 实现数据获取逻辑
        try {
            for (ExportAttributeDTO exportAttributeDTO : dto.getParamsList()) {
                List<String> subList = new ArrayList<>();
                subList.add(exportAttributeDTO.getName());
                list.add(subList);
            }
        } catch (Exception e) {
            System.out.println("数据转换异常：" + e);
        }
        return list;
    }

    /**
     * 同步excel动态列导出
     *
     * @param response
     * @param dto
     * @param exportVOList
     */
    public static <T> void dynamicSynExport(HttpServletResponse response, ExportDTO dto, List<T> exportVOList) throws IOException {
        List<List<Object>> data = getSortDataCovert(dto, exportVOList);
        List<List<String>> headList = getHeadList(dto);

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
        headWriteFont.setFontHeightInPoints((short) 15);
        // 设置水平居中
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(horizontalCellStyleStrategy)
                .head(headList).sheet("模板").doWrite(data);
    }

    /**
     * 异步excel动态列导出
     *
     * @param dto
     * @param exportVOList
     */
    public static <T> String dynamicAsyncExport(ExportDTO dto, List<T> exportVOList) {
        ExcelWriter excelWriter = null;
        try {
            List<List<Object>> data = getSortDataCovert(dto, exportVOList);
            List<List<String>> headList = getHeadList(dto);

            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 背景设置为红色
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
            headWriteFont.setFontHeightInPoints((short) 15);
            // 设置水平居中
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            WriteFont contentWriteFont = new WriteFont();
            // 字体大小
            contentWriteFont.setFontHeightInPoints((short) 10);
            contentWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);


            String filePath = "D:\\mnt\\excel\\导出的表格.xlsx";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            WriteSheet writeSheet = EasyExcelFactory.writerSheet("sheet" + 0).build();
            excelWriter = EasyExcel.write(filePath).head(headList)
                    .registerWriteHandler(horizontalCellStyleStrategy).password("123456").build();

            for (int i = 0; i < 10; i++) {
                excelWriter.write(data, writeSheet);
            }

            return filePath;
        } finally {
            if (null != excelWriter) {
                excelWriter.finish();
                excelWriter.close();
            }
        }
    }

    /**
     * 异步excel动态列导出
     *
     * @param dto
     */
    public static <T> String dynamicAsyncExportTest(ExportDTO dto) {
        ExcelWriter excelWriter = null;
        try {
            List<List<String>> headList = getHeadList(dto);

            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 背景设置为红色
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
            headWriteFont.setFontHeightInPoints((short) 15);
            // 设置水平居中
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);


            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            WriteFont contentWriteFont = new WriteFont();
            // 字体大小
            contentWriteFont.setFontHeightInPoints((short) 10);
            contentWriteFont.setFontName("楷体"); // 设置字体名称，例如楷体
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);


            String filePath = "D:\\mnt\\excel\\导出的表格.xlsx";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            WriteSheet writeSheet = EasyExcelFactory.writerSheet("sheet" + 0).build();
            excelWriter = EasyExcel.write(filePath).head(headList)
                    .registerWriteHandler(horizontalCellStyleStrategy).password("123456").build();

//            for (int i = 0; i < 1000; i++) {
//                List<List<Object>> data = new ArrayList<>();
//                for (int j = 0; j < 1000; j++) {
//                    List<ExportVO> list = new ArrayList<>();
//                    ExportVO exportVO = new ExportVO();
//                    exportVO.setName("张一");
//                    exportVO.setAge(18);
//                    exportVO.setSex(1);
//                    list.add(exportVO);
//                    List<List<Object>> listList = getSortDataCovert(dto, list);
//                    data.addAll(listList);
//                }
//                excelWriter.write(data, writeSheet);
//            }

            List<ExportVO> list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < 1000; j++) {
                    ExportVO exportVO = new ExportVO();
                    exportVO.setName("张一");
                    exportVO.setAge(18);
                    exportVO.setSex(1);
                    list.add(exportVO);
                }
            }

            List<List<Object>> data = getSortDataCovert(dto, list);
            excelWriter.write(data, writeSheet);

            return filePath;
        } finally {
            if (null != excelWriter) {
                excelWriter.finish();
                excelWriter.close();
            }
        }
    }
}
