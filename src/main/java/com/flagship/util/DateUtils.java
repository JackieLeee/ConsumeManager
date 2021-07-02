package com.flagship.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Flagship
 * @Date 2021/7/1 11:11
 * @Description 日期工具类
 */
public class DateUtils {
    /**
     * 获取当前时间字符串
     */
    public static String getCurrentDateString() {
        return getDateString(new Date());
    }

    /**
     * 获取指定时间字符串
     */
    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
