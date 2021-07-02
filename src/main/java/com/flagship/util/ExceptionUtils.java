package com.flagship.util;

import com.flagship.common.Constant;

import java.io.*;
import java.util.Objects;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:33
 * @Description 异常工具类
 */
public class ExceptionUtils {
    /**
     * 日志文件的路径
     */
    private static final String FILE_PATH;
    /**
     * 日志文件
     */
    private static final File LOG_FILE;

    static {
        //静态数据初始化
        FILE_PATH = Objects.requireNonNull(ExceptionUtils.class.getClassLoader().getResource(Constant.FilePath.EXCEPTION_LOG)).getPath();
        LOG_FILE = new File(FILE_PATH);
    }

    /**
     * 异常记录
     */
    public static void recordException(String description) {
        //获取出现异常的类全限定路径名，以及出现错误的方法
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        //输出流
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            //写入日志
            writer.write("CLASS：" + className + "\t");
            writer.write("METHOD：" + methodName + "\t");
            writer.write("DATETIME：" + DateUtils.getCurrentDateString() + "\t");
            writer.write("DESCRIPTION：" + description + "\t");
            writer.newLine();
            //刷新缓存
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
