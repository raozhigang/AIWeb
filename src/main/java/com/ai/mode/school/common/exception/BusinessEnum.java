package com.ai.mode.school.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessEnum implements LocalError {

    SUCCESS("0", "成功"),
    SUCCESS_4("0000", "成功"),

    FAILED("1", "失败"),
    SYS_ERROR("1", "系统错误"),

    PARAM_ERROR("1000001", "参数错误"),
    ;

    private String code;
    private String message;

}
