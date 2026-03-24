package com.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelRunnerDTO {


    @ExcelProperty(value = "real1", order = 0)
    private String real1;

    @ExcelProperty(value = "real2", order = 1)
    private Integer real2;

    @ExcelProperty(value = "real3", order = 2)
    private Integer real3;

    @ExcelProperty(value = "real4", order = 3)
    private Integer real4;

    @ExcelProperty(value = "real5", order = 4)
    private Integer real5;

    @ExcelProperty(value = "real6", order = 5)
    private Integer real6;

    @ExcelProperty(value = "real7", order = 6)
    private Integer real7;

    @ExcelProperty(value = "real8", order = 7)
    private Integer real8;

    @ExcelProperty(value = "real9", order = 8)
    private Integer real9;

    @ExcelProperty(value = "real10", order = 9)
    private Integer real10;

    @ExcelProperty(value = "real11", order = 10)
    private Integer real11;

    @ExcelProperty(value = "real12", order = 11)
    private Integer real12;

    @ExcelProperty(value = "real13", order = 12)
    private Integer real13;

    @ExcelProperty(value = "real14", order = 13)
    private Integer real14;
}
