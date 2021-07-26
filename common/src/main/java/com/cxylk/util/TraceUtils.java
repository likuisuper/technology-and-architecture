package com.cxylk.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * trace工具
 * 跟踪id
 * @author admin
 * @date 2017年3月10日
 * @since 1.0.0
 */
public class TraceUtils {

    /**
     * 线程保存当前信息
     */
    private static final ThreadLocal<String> TRACE_ID_HOLDER = new ThreadLocal<String>();


    private TraceUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 设置traceid，初始化
     */
    public static void traceStart() {
        String traceId = generateTraceId();
        traceStart(traceId);
    }

    /**
     * 设置traceid，初始化
     *
     * @param traceId
     */
    public static void traceStart(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = generateTraceId();
        }
        MDC.put("traceId", traceId);
        TRACE_ID_HOLDER.set(traceId);
    }

    /**
     * 移除traceid
     */
    public static void traceEnd() {
        MDC.remove("traceId");
        TRACE_ID_HOLDER.remove();
    }

    /**
     * 获得当前线程的traceid
     *
     * @return
     */
    public static String getCurrentTrace() {
        return TRACE_ID_HOLDER.get();
    }

    /**
     * 生成跟踪ID
     *
     * @return
     */
    private static String generateTraceId() {
        return String.valueOf(IdUtils.nextId());
    }
}
