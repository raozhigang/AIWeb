package com.ai.mode.school.beans.dto;

import lombok.Data;


@Data
public class FontGenerateButtonReq {

    private String weights;

    private String referenceImage;

    private String model;

    private String targetWord;

}
