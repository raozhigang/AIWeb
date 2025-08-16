package com.ai.mode.school.common.enums;

/**
 * 通用状态码枚举
 */
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "操作成功"),

    /** 参数校验失败 */
    PARAM_ERROR(400, "参数错误"),

    /** 认证失败（如 token 过期） */
    UNAUTHORIZED(401, "未认证或认证失败"),

    /** 权限不足 */
    FORBIDDEN(403, "权限不足"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 业务逻辑失败（如重复提交、库存不足） */
    BUSINESS_ERROR(409, "业务逻辑失败"),

    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /** 网络异常 */
    NETWORK_ERROR(502, "网络异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}