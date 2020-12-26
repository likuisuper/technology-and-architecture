package com.cxylk.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @Classname SampleRecevier
 * @Description 消费者
 * @Author likui
 * @Date 2020/12/26 16:28
 **/
@RabbitListener(queues = "simple.hello") //监听该队列中的信息，交给有RabbitHandler的方法处理
public class SimpleReceiver {
    private static final Logger LOGGER= LoggerFactory.getLogger(SimpleReceiver.class);

    @RabbitHandler
    public void receiver(String in){
        LOGGER.info("[x] Receiver '{}'",in);
    }
}
