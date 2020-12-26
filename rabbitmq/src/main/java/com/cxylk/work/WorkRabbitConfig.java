package com.cxylk.work;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname WorkRabbitConfig
 * @Description 工作模式下rmq的配置，一个生产者，两个消费者
 *              当消费者获取消息处理耗时任务时，空闲的消费者从队列中获取并消费信息
 * @Author likui
 * @Date 2020/12/26 20:23
 **/
@Configuration
public class WorkRabbitConfig {
    @Bean
    public Queue workSend(){
        return new Queue("work.hello");
    }

    @Bean
    public WorkSender workSender(){
        return new WorkSender();
    }

    @Bean
    public WorkReceiver workReceiver(){
        return new WorkReceiver(1);
    }

    @Bean
    public WorkReceiver workReceiver2(){
        return new WorkReceiver(2);
    }
}
