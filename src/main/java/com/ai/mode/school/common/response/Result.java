package com.ai.mode.school.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 通用返回结果封装类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {

    private int code;
    private String message;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 构造函数私有，通过静态方法创建
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ✅ 成功返回（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // ✅ 成功返回（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }
}