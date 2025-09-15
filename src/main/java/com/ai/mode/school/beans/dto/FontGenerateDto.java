package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class FontGenerateDto {

    private String keyword;

    private String url;

    private String imagePath;
    
    private List<String> basisPath;
}
