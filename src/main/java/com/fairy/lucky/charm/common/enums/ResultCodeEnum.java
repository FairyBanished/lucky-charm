package com.fairy.lucky.charm.common.enums;

public enum ResultCodeEnum {

    /**
     * 操作成功
     */
    RC200(200, "操作成功"),

    /**
     * 永久重定向
     */
    RC301(301, "永久重定向"),

    /**
     * 临时重定向
     */
    RC302(302, "临时重定向"),

    /**
     * 用户未登录
     */
    RC401(401, "用户未登录"),

    /**
     * 服务器异常
     */
    RC500(500, "服务器异常"),

    /**
     * 操作失败
     */
    RC999(999, "操作失败");

    private Integer code;

    private String message;

    ResultCodeEnum(Integer code, String message) {
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
