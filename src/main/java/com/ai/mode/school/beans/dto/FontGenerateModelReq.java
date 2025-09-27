package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class FontGenerateModelReq {

    private String referenceImage;

    private String model;

    private String weights;

    private List<FontGenerateDto> targetWords;

}
