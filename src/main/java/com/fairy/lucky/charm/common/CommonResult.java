package com.fairy.lucky.charm.common;

import com.fairy.lucky.charm.common.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCodeEnum.RC200.getCode(), ResultCodeEnum.RC200.getMessage(), data);
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultCodeEnum.RC200.getCode(), ResultCodeEnum.RC200.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode
     * @param message
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> error(ResultCodeEnum resultCode, String message) {
        return new CommonResult<>(resultCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> error(ResultCodeEnum resultCode) {
        return new CommonResult<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(ResultCodeEnum.RC999.getCode(), message, null);
    }

}
