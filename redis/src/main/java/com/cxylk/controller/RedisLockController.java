package com.cxylk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisLockController
 * @Description 使用redis实现分布式锁模拟抢票
 * @Author likui
 * @Date 2021/3/13 14:47
 **/
@RestController
@Slf4j
public class RedisLockController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 第一个版本：不加任何同步措施，此时将会出现超卖
     * 第二个版本：加synchronized,解决单机超卖问题
     *
     * @return
     */
    @GetMapping("/testTicket")
    public String getTicket() {
        synchronized (this) {
            //获取库存100张票
            int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
            //还有余票
            if (stock > 0) {
                //更新库存
                int remainStock = stock - 1;
                redisTemplate.opsForValue().set("stock", String.valueOf(remainStock));
                System.out.println("购票成功,你购买了第" + stock + "张票,余票----" + remainStock + "张");
            } else {
                System.out.println("对不起,票卖光了...");
            }
            return "end";
        }
    }

    @GetMapping("/redisLock")
    public String getTicketByRedisLock() {
        String lockKey = "lk";
        //设置唯一标识
        String uuid = UUID.randomUUID().toString();
        try {
            //Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, "1");//加锁，setnx命令
            //设置超时时间，否则出现异常或者宕机后，下面流程都不会走，也就是说上面设置的key就不会被删除，其他人永远获取不到这个key
//            redisTemplate.expire(lockKey,10, TimeUnit.SECONDS);
            //上面虽然设置了超时时间，但是当服务宕机后，依然不会走，相当于没设置，所以我们应该把这两步当成一个整体，即原子操作。
            //设置key和value的同时设置超时时间。
//            Boolean flag=redisTemplate.opsForValue().setIfAbsent(lockKey,"1",10,TimeUnit.SECONDS);//set lk 1 EX 10 NX
            //为了保证锁不被误删，即同一性，我加的锁我自己来删，所以将value设置为唯一标识
//            Boolean flag=redisTemplate.opsForValue().setIfAbsent(lockKey,uuid,30,TimeUnit.SECONDS);
            Boolean flag = lock(lockKey, uuid, 10);
            if (flag != null && flag) {
                int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
                if (stock > 0) {
                    //更新库存
                    redisTemplate.opsForValue().increment("stock", -1);//set("stock",value-1)
                    System.out.println("购票成功,你购买了第" + stock + "张票");
                } else {
                    System.out.println("对不起,票卖光了...");
                }
            }
        } finally {
//            redisTemplate.delete(lockKey);
            //根据唯一标识来删除。但是这个操作不是原子性的
//            if(uuid.equals(redisTemplate.opsForValue().get(lockKey))){
//                redisTemplate.delete(lockKey);
//            }
            unlock(lockKey, uuid);
        }
        return "end";
    }

    //保证可见性
    protected volatile boolean isOpenExpirationRenewal = true;

    /**
     * 加锁。原子性，锁误删(同一性),守护线程续命(续租)
     *
     * @param lockKey
     * @param clientId
     * @param expireTime
     * @return
     */
    private Boolean lock(String lockKey, String clientId, long expireTime) {
        //原子操作，加锁+失效时间
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, expireTime, TimeUnit.SECONDS);
        if (!flag) {
            return false;
        }
        System.out.println("线程id:" + Thread.currentThread().getId() + "加锁成功!时间:" + LocalTime.now());
        isOpenExpirationRenewal = true;
        //开启定时刷新时间()续租
        scheduleExpirationRenewal(lockKey, clientId, expireTime);
        System.out.println("线程id:" + Thread.currentThread().getId() + "获取锁失败，休眠10秒!时间:" + LocalTime.now());
        //休眠10秒（每隔10s续命）
        sleepBySencond(10);
        return flag;
    }


    /**
     * 开启定时刷新
     */
    protected void scheduleExpirationRenewal(String lockKey, String clientId, long expireTime) {
        Thread renewalThread = new Thread(new ExpirationRenewal(lockKey, clientId, expireTime));
        renewalThread.start();
    }

    class ExpirationRenewal implements Runnable {
        private String lockKey;
        private String clientId;
        private long expireTime;

        ExpirationRenewal(String lockKey, String clientId, long expireTime) {
            this.lockKey = lockKey;
            this.clientId = clientId;
            this.expireTime = expireTime;
        }

        @Override
        public void run() {
            while (isOpenExpirationRenewal) {
                System.out.println("执行延迟失效时间中...");
                //如果当前key还在，延长超时时间(续命)
//                String checkAndScript="if redis.call('get',KEYS[1])==ARGV[1] then " +
//                        "return redis.call('expire',KEYS[1],ARGV[2]) " +
//                        "else return 0 end";
                //通过构造函数设置lua脚本和返回类型
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("expireKey.lua")));
                redisScript.setResultType(Long.class);
                redisTemplate.execute(redisScript, Collections.singletonList(lockKey), clientId, String.valueOf(expireTime));

                //休眠10s
                sleepBySencond(10);
            }
        }
    }

    /**
     * 释放锁，为了保证该操作是原子性，所以采用lua脚本实现
     *
     * @param lockKey
     * @param clientId
     */
    private void unlock(String lockKey, String clientId) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("releaselock.lua")));
        //脚本的返回类型
        redisScript.setResultType(Long.class);
        //执行脚本
        redisTemplate.execute(redisScript, Collections.singletonList(lockKey), clientId);
    }

    public void sleepBySencond(int sencond) {
        try {
            Thread.sleep(sencond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long timeOut = 100;//重试时间 纳秒 1秒=1000毫秒 1毫秒=1000微秒 1微秒=1000纳秒
    boolean locked = false;

    //加锁失败，进行重试
    public boolean tryLock(String lockKey, String lockValue, long expireTime) {
        // 生成随机key
        lockValue = UUID.randomUUID().toString();
        // 请求锁超时时间，纳秒
        long timeout = timeOut * 1000000;
        // 系统当前时间，纳秒
        long nowTime = System.nanoTime();
        while ((System.nanoTime() - nowTime) < timeout) {
            if (this.lock(lockKey, lockValue, expireTime)) {
//            if (OK.equalsIgnoreCase(this.set(lockKey, lockValue, expireTime))) {
                locked = true;
                // 上锁成功结束请求
                return locked;
            }

            // 每次请求等待一段时间
            seleep(10, 50000);
        }
        return locked;
    }

    //随机时间后，开始重试，防止性能浪费
    private void seleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, new Random().nextInt(nanos));
        } catch (InterruptedException e) {
            log.info("获取分布式锁休眠被中断：", e);
        }
    }
}
