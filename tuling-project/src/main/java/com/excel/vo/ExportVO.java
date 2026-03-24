package com.excel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExportVO {

    @ExcelProperty("名字")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("性别")
    private Integer sex;
}
