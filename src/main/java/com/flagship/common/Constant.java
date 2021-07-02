package com.flagship.common;


/**
 * @Author Flagship
 * @Date 2021/7/1 14:54
 * @Description 常量类
 */
public class Constant {
    public static String DELIMITER = "#";

    public interface FilePath{
        String USER_DB = "data/user.db";
        String CONSUME_RECORD_DB = "data/consume_record.db";
        String RECHARGE_RECORD_DB = "data/recharge_record.db";
        String EXCEPTION_LOG = "log/exception.log";
    }

    public interface UserStatus{
        int ACTIVE = 1;
        int FROZEN = 0;
    }

    public interface UserRole{
        int ADMIN = 1;
        int USER = 0;
    }
}
