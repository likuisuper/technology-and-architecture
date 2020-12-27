package com.cxylk.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname TopicSender
 * @Description 通配符模式下的生产者
 * @Author likui
 * @Date 2020/12/27 16:47
 **/
public class TopicSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchangeName = "exchange.topic";

    private final String[] keys = {"quick.orange.rabbit", "lazy.orange.element", "quick.orange.fox",
            "lazy.brow.fox", "lazy.pink.rabbit", "quick.brow.fox"};

    public void send(int index){
        int limitIndex=index%keys.length;
        StringBuilder sb=new StringBuilder("hello to ");
        String key=keys[limitIndex];
        sb.append(key).append(' ');
        sb.append(index+1);
        String message=sb.toString();
        //指定路由键
        rabbitTemplate.convertAndSend(exchangeName,key,message);
        LOGGER.info("[x] Send '{}'",message);
    }
}
