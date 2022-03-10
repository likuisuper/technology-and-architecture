package com.cxylk.mqreliable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author likui
 * @date 2022/3/1 下午2:17
 * 企业中的rabbitmq真实使用配置
 **/
@Configuration
@Slf4j
public class MQConfig {
    //各种队列、交换机、路由key、以及绑定这里省略
    //需要注意的是，真实生产中用的都是MQ的路由模式

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //使用confirm机制确保生产者不会丢数据
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
                log.info("ConfirmCallback:     " + "确认情况：" + ack);
                log.info("ConfirmCallback:     " + "原因：" + cause);
            }
        });

        //回调函数
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback(){

            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.info("ReturnCallback:     " + "消息：" + returned.getMessage());
                log.info("ReturnCallback:     " + "回应码：" + returned.getReplyCode());
                log.info("ReturnCallback:     " + "回应信息：" + returned.getReplyText());
                log.info("ReturnCallback:     " + "交换机：" + returned.getExchange());
                log.info("ReturnCallback:     " + "路由键：" + returned.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }


    @Bean(name = "systemRabbitListenerContainerFactory")
    public RabbitListenerContainerFactory<?> systemRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 指定最小的消费者数量。根据实际情况定义
        factory.setConcurrentConsumers(8);
        // 指定最大的消费者数量。根据实际情况定义
        factory.setMaxConcurrentConsumers(16);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 手动。确保消息处理完之后手动ack，防止消费者弄丢数据
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
