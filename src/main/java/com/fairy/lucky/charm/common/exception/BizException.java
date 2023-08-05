package com.fairy.lucky.charm.common.exception;

import com.fairy.lucky.charm.common.enums.ResultCodeEnum;
import lombok.Data;

import java.util.Objects;


@Data
public class BizException extends RuntimeException {

    private Integer code;

    private String message;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ResultCodeEnum resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BizException(ResultCodeEnum resultCode, String message) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = Objects.isNull(message) ? resultCode.getMessage() : message;
    }

    public BizException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BizException(ResultCodeEnum resultCode, Throwable cause) {
        super(cause);
        this.code = resultCode.getCode();
        this.message = cause.getMessage();
    }

}
