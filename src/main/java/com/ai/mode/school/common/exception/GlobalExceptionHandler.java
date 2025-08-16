package com.ai.mode.school.common.exception;

import com.ai.mode.school.common.response.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 拦截所有 Controller 抛出的异常，统一返回 Result 格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截 BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 拦截其他未处理的异常（如 NullPointerException 等）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 生产环境建议不要暴露详细错误信息
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}