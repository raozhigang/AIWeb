package com.ai.mode.school.common.exception;

import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    private String code;
    private String message;

    public BusinessException(BusinessException e) {
        this.message = e.getMessage();
        this.code = e.getCode();
    }

    public BusinessException(LocalError serviceExceptionEnum) {
        this.message = serviceExceptionEnum.getMessage();
        this.code = serviceExceptionEnum.getCode();
    }

    public BusinessException(LocalError serviceExceptionEnum, String message) {
        this.message = serviceExceptionEnum.getMessage() + "：" + message;
        this.code = serviceExceptionEnum.getCode();
    }

    public BusinessException(String code, String message) {
        this.message = message;
        this.code = code;
    }
    public BusinessException(String message) {
        this.message = message;
        this.code = BusinessEnum.FAILED.getCode();
    }

}
