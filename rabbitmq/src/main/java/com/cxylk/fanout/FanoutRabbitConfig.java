package com.cxylk.fanout;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname FanoutRabbitConfig
 * @Description 发布/订阅模式，类似广播，包含一个生产者，一个交换机，两个队列，队列绑定到交换机上，
 *              模拟两个消费者同时绑定到不同的队列上去
 * @Author likui
 * @Date 2020/12/27 13:54
 **/
@Configuration
public class FanoutRabbitConfig {

    /**
     * 创建一个交换机，第一个参数是name,当没有指定后面两个参数durable(是否持久化)和autoDelete(是否自动删除)时，
     * 这两个参数默认是true和false,代表开启持久化，存储在磁盘上，关闭自动删除，即没有生产者和消费者使用此队列时，
     *不会自动删除
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("exchange.fanout");
    }

    /**
     * 创建第一个匿名队列
     * @return
     */
    @Bean
    public Queue fanoutQueue1(){
        return new AnonymousQueue();
    }

    /**
     * 创建第二个匿名队列
     * @return
     */
    @Bean
    public Queue fanoutQueue2(){
        return new AnonymousQueue();
    }

    /**
     * 将第一个匿名队列(也可以指定队列名称像简单模式那样)绑定到交换机上
     * @return
     */
    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    /**
     * 将第二个匿名队列(也可以指定队列名称像简单模式那样)绑定到交换机上
     * @return
     */
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    @Bean
    public FanoutSender fanoutSender(){
        return new FanoutSender();
    }

    @Bean
    public FanoutReceiver fanoutReceiver(){
        return new FanoutReceiver();
    }
}
