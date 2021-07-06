package com.flagship.util;

import com.flagship.common.Constant;

/**
 * @Author Flagship
 * @Date 2021/7/2 15:49
 * @Description 验证器
 */
public class ValidatorUtils {

    public static boolean validPassword(String password) {
        return password.matches(Constant.PASSWORD_REGEX) ;
    }

    public static boolean validUserName(String userName) {
        return userName.matches(Constant.USERNAME_REGEX) ;
    }
}
