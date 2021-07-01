package com.flagship.util;

import com.flagship.common.Constant;

import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:33
 * @Description
 */
public class ExceptionUtils {
    private static final String FILE_PATH;
    private static final File LOG_FILE;

    static {
        FILE_PATH = Objects.requireNonNull(ExceptionUtils.class.getClassLoader().getResource(Constant.FilePath.EXCEPTION_LOG)).getPath();
        LOG_FILE = new File(FILE_PATH);;
    }

    public static void recordException(String description) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write("CLASS：" + className + "\t");
            writer.write("METHOD：" + methodName + "\t");
            writer.write("DATETIME：" + DateUtils.getCurrentDateString() + "\t");
            writer.write("DESCRIPTION：" + description + "\t");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
