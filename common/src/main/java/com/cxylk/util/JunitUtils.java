package com.cxylk.util;

/**
 * @Classname JunitUtils
 * @Description 根据运行线程是否是junit.runners来判断当前是否在运行单元测试
 * @Author likui
 * @Date 2021/4/16 11:14
 **/
public class JunitUtils {
    /**
     * 检测是否在运行单元测试
     *
     * @return
     */
    public static boolean isRunningTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            String stackString = stackTraceElement.toString();
            if (stackString.lastIndexOf("junit.runners") > -1) {
                return true;
            }
        }
        return false;
    }

}
