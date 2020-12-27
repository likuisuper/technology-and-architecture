package com.cxylk.direct;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname DirectRabbitConfig
 * @Description 路由模式，根据路由键给多个消费者发送消息的模式。两个消费者同时绑定到不同的队列上去
 *              两个队列通过路由键绑定到交换机上，生产者发动到消息到交换机，交换机通过路由键发送到
 *              不同的队列，队列绑定的消费者获取并消费信息
 * @Author likui
 * @Date 2020/12/27 15:42
 **/
@Configuration
public class DirectRabbitConfig {
    /**
     * 创建一个type=direct的交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("exchange.direct");
    }

    @Bean
    public Queue directQueue1(){
        //匿名队列不需要指定名字，会有一个默认名称
        return new AnonymousQueue();
    }

    @Bean
    public Queue directQueue2(){
        return new AnonymousQueue();
    }

    @Bean
    public Binding directBinding1a(){
        //将匿名队列1绑定到交换机，指定路由建为orange
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("orange");
    }

    @Bean
    public Binding directBinding1b(){
        //将匿名队列1绑定到交换机，指定路由键为black
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("black");
    }

    @Bean
    public Binding directBinding2a(){
        //将匿名队列2绑定到交换机，指定路由键为green
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("green");
    }

    @Bean
    public Binding directBinding2b(){
        //将匿名队列2绑定到交换机，指定路由键为black
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("black");
    }

    @Bean
    public DirectSender directSender(){
        return new DirectSender();
    }

    @Bean
    public DirectReceiver directReceiver(){
        return new DirectReceiver();
    }
}
