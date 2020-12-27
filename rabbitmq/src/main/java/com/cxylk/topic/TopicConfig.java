package com.cxylk.topic;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname TopicConfig
 * @Description 通配符模式(主题模式)，可以根据路由键匹配规则选择性给多个消费者发送消息的模式。两个消费者绑定
 * 到不同的队列上去，两个队列通过路由键匹配规则绑定到交换机上去。生产者发送消息到交换机，交换机根据
 * 路由键匹配规则转发到不同队列，队列绑定的消费者接收并消费信息
 * @Author likui
 * @Date 2020/12/27 16:37
 **/
@Configuration
public class TopicConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("exchange.topic");
    }

    @Bean
    public Queue topicQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue topicQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding topicBinding1a() {
        //匹配*.orange.*到队列1，路由键中 * 代表只能匹配有一个单词
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("*.orange.*");
    }

    @Bean
    public Binding topicBinding1b() {
        //匹配 *.*.rabbit到队列1
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("*.*.rabbit");
    }

    @Bean
    public Binding topicBinding2() {
        //匹配 lazy.#到队列2，# 代表匹配零个或多个单词
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lazy.#");
    }

    @Bean
    public TopicSender topicSender() {
        return new TopicSender();
    }

    @Bean
    public TopicReceiver topicReceiver() {
        return new TopicReceiver();
    }
}
