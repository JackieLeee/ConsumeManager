package com.flagship.common;


/**
 * @Author Flagship
 * @Date 2021/7/1 14:54
 * @Description 常量类
 */
public class Constant {
    /**
     * 数据分隔符
     */
    public static final String DELIMITER = "#";
    /**
     * 密码正则
     */
    public static final String PASSWORD_REGEX = "^[a-z0-9A-Z]{8,16}$";
    /**
     * 用户名正则
     */
    public static final String USERNAME_REGEX = "^[a-z0-9A-Z]{4,10}$";

    /**
     * 文件路径
     */
    public interface FilePath{
        /**
         * 用户数据
         */
        String USER_DB = "data/user.db";
        /**
         * 消费数据
         */
        String CONSUME_RECORD_DB = "data/consume_record.db";
        /**
         * 充值数据
         */
        String RECHARGE_RECORD_DB = "data/recharge_record.db";
        /**
         * 日志文件
         */
        String EXCEPTION_LOG = "log/exception.log";
    }

    /**
     * 用户状态
     */
    public interface UserStatus{
        /**
         * 启用
         */
        int ACTIVE = 1;
        /**
         * 冻结
         */
        int FROZEN = 0;
    }

    /**
     * 用户角色
     */
    public interface UserRole{
        /**
         * 管理员
         */
        int ADMIN = 1;
        /**
         * 用户
         */
        int USER = 0;
    }
}
