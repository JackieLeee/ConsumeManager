package com.flagship.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:35
 * @Description
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    /**
     *
     */
    SYSTEM_ERROR(10000, "系统异常"),
    INPUT_ILLEGAL(10001, "输入不合法");

    private final Integer errCode;
    private final String errMsg;
}
