package com.flagship.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Flagship
 * @Date 2021/7/1 11:11
 * @Description
 */
public class DateUtils {
    public static String getCurrentDateString() {
        return getDateString(new Date());
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
