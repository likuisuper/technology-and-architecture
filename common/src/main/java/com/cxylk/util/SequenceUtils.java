package com.cxylk.util;

/**
 * @Classname SequenceUtils
 * @Description 雪花算法分布式唯一ID生成器<br>
 * 每个机器号最高支持每秒‭65535个序列, 当秒序列不足时启用备份机器号, 若备份机器也不足时借用备份机器下一秒可用序列<br>
 * 53 bits 趋势自增ID结构如下:
 * <p>
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |-----------|##########32bit 秒级时间戳##########|-----|-----------------|
 * |--------------------------------------5bit机器位|xxxxx|-----------------|
 * |-----------------------------------------16bit自增序列|xxxxxxxx|xxxxxxxx|
 * @Author likui
 * @Date 2021/4/16 13:29
 **/
public class SequenceUtils {
    /**
     * 初始偏移时间戳
     */
    private static final long OFFSET = 1546300800L;

    /**
     * 机器id所占位数 (5bit, 支持最大机器数 2^5 = 32)
     */
    private static final long WORKER_ID_BITS = 5L;
    /**
     * 自增序列所占位数 (16bit, 支持最大每秒生成 2^16 = ‭65536‬)
     */
    private static final long SEQUENCE_ID_BITS = 16L;
    /**
     * 机器id偏移位数
     */
    private static final long WORKER_SHIFT_BITS = SEQUENCE_ID_BITS;
    /**
     * 自增序列偏移位数
     */
    private static final long OFFSET_SHIFT_BITS = SEQUENCE_ID_BITS + WORKER_ID_BITS;
    /**
     * 机器标识最大值 (2^5 / 2 - 1 = 15)
     */
    private static final long WORKER_ID_MAX = -1L ^ (-1L << WORKER_ID_BITS);

    private static final long BACK_WORKER_ID_BEGIN = (1 << WORKER_ID_BITS) >> 1;
    /**
     * 自增序列最大值 (2^16 - 1 = ‭65535)
     */
    private static final long SEQUENCE_MAX = (1 << SEQUENCE_ID_BITS) - 1L;
    /**
     * 发生时间回拨时容忍的最大回拨时间 (秒)
     */
    private static final long BACK_TIME_MAX = 1L;

    /**
     * 上次生成ID的时间戳 (秒)
     */
    private static long lastTimestamp = 0L;
    /**
     * 当前秒内序列 (2^16)
     */
    private static long sequence = 0L;
    /**
     * 备份机器上次生成ID的时间戳 (秒)
     */
    private static long lastTimestampBak = 0L;
    /**
     * 备份机器当前秒内序列 (2^16)
     */
    private static long sequenceBak = 0L;

    private  long workerId ;

    private static long timeMultiplier = 1000L;


    public  SequenceUtils(long actualWorkId) {
        if (actualWorkId > WORKER_ID_MAX || actualWorkId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", WORKER_ID_MAX));
        }
        this.workerId = actualWorkId;
    }

    public SequenceUtils() {
        this.workerId = 0L;
    }

    /**
     * 获取自增序列
     *
     * @return long
     */
    public long nextId() {
        return nextId(System.currentTimeMillis() / timeMultiplier);
    }

    /**
     * 主机器自增序列
     *
     * @param timestamp 当前Unix时间戳
     * @return long
     */
    private synchronized long nextId(long timestamp) {
        // 时钟回拨检查
        if (timestamp < lastTimestamp) {
            // 发生时钟回拨
            return nextIdBackup(timestamp);
        }
        // 开始下一秒
        if (timestamp != lastTimestamp) {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
        if (0L == (++sequence & SEQUENCE_MAX)) {
            // 秒内序列用尽
            sequence--;
            return nextIdBackup(timestamp);
        }
        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | (workerId << WORKER_SHIFT_BITS) | sequence;
    }

    /**
     * 备份机器自增序列
     *
     * @param timestamp timestamp 当前Unix时间戳
     * @return long
     */
    private synchronized long nextIdBackup(long timestamp) {
        if (timestamp < lastTimestampBak) {
            if (lastTimestampBak - System.currentTimeMillis() / timeMultiplier <= BACK_TIME_MAX) {
                timestamp = lastTimestampBak;
            } else {
                throw new SequenceUtilsRuntimeException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
            }
        }
        if (timestamp != lastTimestampBak) {
            lastTimestampBak = timestamp;
            sequenceBak = 0L;
        }
        if (0L == (++sequenceBak & SEQUENCE_MAX)) {
            // 秒内序列用尽
            return nextIdBackup(timestamp + 1);
        }
        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | ((workerId ^ BACK_WORKER_ID_BEGIN) << WORKER_SHIFT_BITS) | sequenceBak;
    }

    private class SequenceUtilsRuntimeException extends RuntimeException{
        public SequenceUtilsRuntimeException(String message) {
            super(message);
        }
    }

}
