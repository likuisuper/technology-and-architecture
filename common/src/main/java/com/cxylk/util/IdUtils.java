package com.cxylk.util;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @program: edu-commom
 * @description: 主键生成
 * @author: miles
 * @create: 2020-03-25 17:49
 **/
@Slf4j
public class IdUtils {

    /**
     *
     */
    private static SequenceUtils sequenceUtils = new SequenceUtils();

    private IdUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 初始化ID
     *
     * @param actualWorkId
     * @return
     */
    public static SequenceUtils init(long actualWorkId) {
        sequenceUtils = new SequenceUtils(actualWorkId);
        log.info("IdUtil use actualWorkId: {}", actualWorkId);
        return sequenceUtils;
    }

    public static synchronized long nextId() {
        return sequenceUtils.nextId();
    }

    /**
     * 获得uudi
     *
     * @return
     */
    public static String nextUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
