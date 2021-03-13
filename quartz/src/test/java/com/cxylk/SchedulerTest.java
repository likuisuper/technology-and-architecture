package com.cxylk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;

/**
 * @Classname SchedulerTest
 * @Description 任务调度测试类。cron表达式生成网站https://cron.qqe2.com/。cron一共有七位数,最后一位是年,SpringBoot定时方案只需要设置六位即可
 * @Author likui
 * @Date 2021/3/4 11:13
 **/
@SpringBootTest
public class SchedulerTest {
    private int count=0;

    @Test
    @Scheduled(cron = "*/6 * * * * ?")
    public void process() {

        System.out.println("this is scheduler task running:"+(count++));

    }
}
