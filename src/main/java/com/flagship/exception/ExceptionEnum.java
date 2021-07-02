package com.flagship.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:35
 * @Description
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    /**
     * 错误说明
     */
    INIT_CONSUME_RECORD_ERR("初始化消费数据异常"),
    ADD_CONSUME_RECORD_ERR("新增消费数据保存异常"),
    INIT_RECHARGE_RECORD_ERR("初始化充值数据异常"),
    ADD_RECHARGE_RECORD_ERR("新增充值数据保存异常"),
    INIT_USER_ERR("初始化用户数据异常"),
    ADD_USER_ERR("新增用户数据保存异常");

    private final String errMsg;
}
