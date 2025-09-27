package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class FontGenerateButtonReq {

    private String styleName;

    private List<String> targetWords;

}
