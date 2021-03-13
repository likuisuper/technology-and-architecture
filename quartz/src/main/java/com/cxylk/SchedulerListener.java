package com.cxylk;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname SchedulerListener
 * @Description 触发定时任务。方式2是定时执行。注意两种方式不能同时使用
 * @Author likui
 * @Date 2021/3/4 14:08
 **/
@Configuration
@EnableScheduling
@Component
public class SchedulerListener {
    @Autowired
    private CronSchedulerJob cronSchedulerJob;

    @Scheduled(cron = "0/12 * * * * ?")
    public void schedule() throws SchedulerException {
        cronSchedulerJob.scheduleJobs();
        System.out.println("<--------定时任务开始执行-------->");
    }
}
