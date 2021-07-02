package com.flagship.common;

import com.flagship.service.BaseService;
import com.flagship.service.ConsumeRecordService;
import com.flagship.service.RechargeRecordService;
import com.flagship.service.UserService;

/**
 * @Author Flagship
 * @Date 2021/7/2 8:31
 * @Description
 */
public enum ServiceInstanceEnum {
    /**
     * 枚举字段
     */
    USER_SERVICE("userService", new UserService()),
    CONSUME_SERVICE("consumeRecordService", new ConsumeRecordService()),
    RECHARGE_RECORD("rechargeRecordService", new RechargeRecordService());
    private final String name;
    private final BaseService service;

    ServiceInstanceEnum(String name, BaseService view) {
        this.name = name;
        this.service = view;
    }

    public static BaseService getViewByName(String name) {
        for (ServiceInstanceEnum value : ServiceInstanceEnum.values()) {
            if (value.name.equals(name)) {
                return value.service;
            }
        }
        return null;
    }
}
