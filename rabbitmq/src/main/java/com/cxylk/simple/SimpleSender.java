package com.cxylk.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname SampleSender
 * @Description 生产者
 * @Author likui
 * @Date 2020/12/26 16:27
 **/
public class SimpleSender {
    private static final Logger LOGGER= LoggerFactory.getLogger(SimpleSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String queueName="simple.hello";

    /**
     * 生产者通过向队列发送消息
     */
    public void send(){
        String message="hello";
        rabbitTemplate.convertAndSend(queueName,message);
        LOGGER.info("[x] Send '{}'",message);
    }
}
