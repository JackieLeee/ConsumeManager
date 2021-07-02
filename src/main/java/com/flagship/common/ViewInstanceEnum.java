package com.flagship.common;

import com.flagship.view.*;

/**
 * @Author Flagship
 * @Date 2021/7/2 8:00
 * @Description 单例
 */
public enum ViewInstanceEnum {
    /**
     * 枚举字段
     */
    MAIN_VIEW("mainView", new MainView()),
    ADMIN_VIEW("adminView", new AdminView()),
    USER_VIEW("userView", new UserView()),
    UPDATE_VIEW("updateView", new UpdateView());
    private final String name;
    private final BaseView view;

    ViewInstanceEnum(String name, BaseView view) {
        this.name = name;
        this.view = view;
    }

    public BaseView getInstance() {
        return this.view;
    }

    public static BaseView getViewByName(String name) {
        for (ViewInstanceEnum value : ViewInstanceEnum.values()) {
            if (value.name.equals(name)) {
                return value.view;
            }
        }
        return null;
    }
}
