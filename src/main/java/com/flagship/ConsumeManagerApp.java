package com.flagship;

import com.flagship.common.ViewInstanceEnum;

import java.util.Objects;

/**
 * @Author Flagship
 * @Date 2021/7/1 23:33
 * @Description
 */
public class ConsumeManagerApp {
    public static void main(String[] args) {
        Objects.requireNonNull(ViewInstanceEnum.getViewByName("mainView")).show(null);
    }
}
