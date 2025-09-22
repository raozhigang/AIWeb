package com.ai.mode.school.beans.dto;

import lombok.Data;

import java.util.List;


@Data
public class UserGenerateReq {

    private String userName;

    private String batchNo;

    private String model;

    private int pageNum;

    private int pageSize;
}
