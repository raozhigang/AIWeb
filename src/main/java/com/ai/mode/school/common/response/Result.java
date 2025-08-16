package com.ai.mode.school.common.response;

import com.ai.mode.school.common.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 统一 API 响应结果（无 Lombok 依赖）
 * @param <T> 数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码（如 200 成功，400 参数错误，500 服务器异常） */
    private Integer code;

    /** 响应消息（描述信息，如 "操作成功"） */
    private String message;

    /** 响应数据（业务数据，可能为 null） */
    private T data;

    // ---------------------- 无参/全参构造器 ----------------------
    public Result() {}

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ---------------------- 静态工厂方法 ----------------------
    /**
     * 成功响应（无数据）
     * @return Result<Void>
     */
    public static Result<Void> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应（带数据）
     * @param data 业务数据
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 失败响应（自定义状态码和消息）
     * @param code 状态码
     * @param message 消息
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败响应（使用预定义状态码）
     * @param resultCode 预定义状态码枚举
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败响应（自定义消息 + 预定义状态码）
     * @param resultCode 预定义状态码枚举
     * @param message 自定义消息（覆盖枚举默认消息）
     * @param <T> 数据类型
     * @return Result<T>
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    // ---------------------- Getter/Setter ----------------------
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}