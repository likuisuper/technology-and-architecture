package com.cxylk.simple;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname SampleRabbitConfig
 * @Description 简单模式下的rmq配置
 * @Author likui
 * @Date 2020/12/26 16:22
 **/
@Configuration
public class SimpleRabbitConfig {

    @Bean
    public Queue hello(){
        return new Queue("simple.hello");
    }

    //生产者
    @Bean
    public SimpleSender sender(){
        return new SimpleSender();
    }

    //消费者
    @Bean
    public SimpleReceiver receiver(){
        return new SimpleReceiver();
    }
}
