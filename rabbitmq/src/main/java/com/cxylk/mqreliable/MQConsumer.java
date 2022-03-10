package com.cxylk.mqreliable;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author likui
 * @date 2022/3/1 下午2:38
 **/
@Service
@Slf4j
public class MQConsumer {
    //@RabbitListener注解加在类上会报错，所以放在方法上，并且接收的消息使用@Payload注解标识
//    @RabbitListener(queues = "queue",
//            containerFactory = "systemRabbitListenerContainerFactory")
//    @RabbitHandler
//    public void portraitCompute(Channel channel, @Payload TenantMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        log.info("rabbitmq tenant {}", message);
//
//        try {
//            //业务处理
//
//            // 成功，回传ack
//            channel.basicAck(tag, false);
//
//        } catch (Exception e) {
//            log.error("消息监听：异常", e);
//            try {
//                //回传失败ack
//                channel.basicNack(tag, false, false);
//            } catch (IOException ee) {
//                log.error("rabbitmq nack", e);
//            }
//        }
//    }
}
