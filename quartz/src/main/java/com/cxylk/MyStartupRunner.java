package com.cxylk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Classname MyStartupRunner
 * @Description 触发定时任务。方式1是在项目启动时执行。注意两种方式不能同时使用
 * @Author likui
 * @Date 2021/3/4 13:54
 **/
@Component
public class MyStartupRunner implements CommandLineRunner {
    @Autowired
    private CronSchedulerJob cronSchedulerJob;


    @Override
    public void run(String... args) throws Exception {
        cronSchedulerJob.scheduleJobs();
        System.out.println("<--------定时任务开始执行------->");
    }
}
