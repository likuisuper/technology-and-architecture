package com.cxylk.direct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname DirectSender
 * @Description 路由模式下的生产者
 * @Author likui
 * @Date 2020/12/27 15:56
 **/
public class DirectSender {
    private static final Logger LOGGER= LoggerFactory.getLogger(DirectSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchangeName="exchange.direct";

    private final String[] keys={"orange","green","black"};

    public void send(int index){
        int limitIndex=index%3;
        StringBuilder sb=new StringBuilder("hello to ");
        String key=keys[limitIndex];
        sb.append(key).append(' ');
        sb.append(index+1);
        String message=sb.toString();
        //路由模式下指定路由键发送消息
        rabbitTemplate.convertAndSend(exchangeName,key,message);
        LOGGER.info("[x] Send '{}'",message);
    }
}
