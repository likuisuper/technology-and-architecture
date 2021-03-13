package com.cxylk;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Classname CronSchedulerJob
 * @Description 构建Schedule来指定触发器去执行指定的任务。Scheduler将触发器和job关联起来
 * @Author likui
 * @Date 2021/3/4 13:28
 **/
@Component
public class CronSchedulerJob {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private void scheduleJob1(Scheduler scheduler) throws SchedulerException {
        //withIdentity 会自动生成一个独一无二的 TriggerKey 用来区分不同的 Trigger。
        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob1.class).withIdentity("job1", "group1").build();
        // 6的倍数秒执行 也就是 6 12 18 24 30 36 42 .... 每隔6s执行
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/6 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .usingJobData("name","lk1").withSchedule(cronScheduleBuilder).build();
        //调度器Scheduler指定Trigger去执行指定的Job
        scheduler.scheduleJob(jobDetail,cronTrigger);
    }

    private void scheduleJob2(Scheduler scheduler) throws SchedulerException {
        //withIdentity 会自动生成一个独一无二的 TriggerKey 用来区分不同的 Trigger。
        JobDetail jobDetai2 = JobBuilder.newJob(ScheduledJob2.class).withIdentity("job2", "group2").build();
        // // 12秒的倍数执行  12  24 36  48  60
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/12 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group2")
                .usingJobData("name","lk2").withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetai2,cronTrigger);
    }

    /**
     * 同时启动两个定时任务
     * @throws SchedulerException
     */
    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduleJob1(scheduler);
        scheduleJob2(scheduler);
    }
}
