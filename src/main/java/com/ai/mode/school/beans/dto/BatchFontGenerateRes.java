package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class BatchFontGenerateRes {

    private String batchNo;

    private String model;

    private List<FontGenerateDto> data;
}
