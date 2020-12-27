package com.cxylk.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname FanoutSender
 * @Description 发布/订阅模式下的生产者
 * @Author likui
 * @Date 2020/12/27 14:15
 **/
public class FanoutSender {
    private static final Logger LOGGER= LoggerFactory.getLogger(FanoutSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchangeName="exchange.fanout";

    public void send(int index){
        int limitIndex=index%3+1;
        StringBuilder sb=new StringBuilder("hell0");
        for (int i = 0; i < limitIndex; i++) {
            sb.append(".");
        }
        sb.append(index);
        String message=sb.toString();
        //扇形交换机(即发布订阅模式下)第二个参数路由键无需配置，配置也不起作用
        rabbitTemplate.convertAndSend(exchangeName,null,message);
        LOGGER.info("[x] Sent '{}'",message);
    }
}
