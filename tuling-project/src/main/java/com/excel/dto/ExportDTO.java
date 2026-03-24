package com.excel.dto;

import com.excel.vo.ExportAttributeDTO;
import lombok.Data;

import java.util.List;

@Data
public class ExportDTO {

    private List<ExportAttributeDTO> paramsList;
}
