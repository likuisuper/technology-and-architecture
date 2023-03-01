package com.startdt.nuza.map.limit;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RateLimiterHandler {
    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        initApiEventLimit();
    }

    public void initApiEventLimit() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(ApiEventConstants.LIMIT_KEY);
        rateLimiter.trySetRate(RateType.OVERALL, 1000, 1, RateIntervalUnit.SECONDS);
    }

    /**
     * 需要注意，必须先设置rate否则会报错
     * @param key
     * @return
     */
    public boolean tryAcquire(String key){
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        return limiter.tryAcquire();
    }
    public boolean tryAcquire(String key, long permits){
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        return limiter.tryAcquire(permits);
    }

    /**
     * 重新设置配置需要先把旧的删除，才能设置成功，删除过程中，无法使用需要注意
     * @param key
     * @param rateType
     * @param limit
     * @param time
     * @param unit
     * @return
     */
    public boolean setRate(String key, RateType rateType, long limit, long time, RateIntervalUnit unit) {
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        limiter.delete();
        return limiter.trySetRate(rateType, limit, time, unit);
    }

    public RateLimiterConfig getConfig(String key) {
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        return limiter.getConfig();
    }
}
