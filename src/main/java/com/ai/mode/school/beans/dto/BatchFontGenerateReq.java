package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class BatchFontGenerateReq {

    private String model;

    private List<FontGenerateDto> data;
}
