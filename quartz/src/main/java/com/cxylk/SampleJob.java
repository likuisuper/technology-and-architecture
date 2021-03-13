package com.cxylk;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Classname SampleQuartzJob
 * @Description 定义一个Job
 * @Author likui
 * @Date 2021/3/4 13:17
 **/
public class SampleJob extends QuartzJobBean {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Quartz--->hello:"+this.name);
    }
}
