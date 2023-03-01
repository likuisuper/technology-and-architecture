package com.startdt.court.common.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/12/26 16:12
 * @description redisson锁，目前等待功能有点问题，等待结束加锁不会成功
 *
 * 注意使用leasTime参数看门狗不生效,
 *
 *          RLock lock = redissonClient.getLock("guodong");    // 拿锁失败时会不停的重试
 *         // 具有Watch Dog 自动延期机制 默认续30s 每10s续期，即剩20秒重新刷新过期时间
 *         lock.lock();
 *         // 尝试拿锁10s后停止重试,返回false 具有Watch Dog 自动延期机制 默认续30s，每10s续期，即剩20秒重新刷新过期时间
 *         boolean res1 = lock.tryLock(10, TimeUnit.SECONDS);
 *         // 没有Watch Dog ，10s后自动释放
 *         lock.lock(10, TimeUnit.SECONDS);
 *         // 尝试拿锁100s后停止重试,返回false 没有Watch Dog ，10s后自动释放
 *         boolean res2 = lock.tryLock(100, 10, TimeUnit.SECONDS);
 */
@Service
@Slf4j
public class RedissonLock {

    private RedissonClient redisson;

    /**
     * 等待时间,默认为0秒不等待
     *
     * @return 轮询锁的等待时间
     * @since 1.0.0
     */
    private long waitTime = 0L;
    /**
     * 超时时间单位
     *
     * @return 秒
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public RedissonLock(RedissonClient redisson) {
        this.redisson = redisson;
    }

    public RLock getLock(String key){
        return redisson.getLock(key);
    }

    public boolean tryLock(RLock rLock) {
        return tryLock(rLock, waitTime, timeUnit);
    }

    public boolean tryLock(RLock rLock, TimeUnit timeUnit) {
        return tryLock(rLock, waitTime, timeUnit);
    }

    public boolean tryLock(RLock rLock, Long waitTime, TimeUnit timeUnit) {
        try {
            return rLock.tryLock(waitTime, timeUnit);
        } catch (InterruptedException ignore) {
            log.warn("tryLock InterruptedException");
            Thread.interrupted();
        } catch (Exception e) {
            log.error("tryLock exception, ", e);
        }
        return false;
    }

    public RedissonClient getRedisson() {
        return this.redisson;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
