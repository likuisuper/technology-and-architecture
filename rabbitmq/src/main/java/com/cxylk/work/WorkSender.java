package com.cxylk.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname WorkSender
 * @Description work模式下的生产者
 * @Author likui
 * @Date 2020/12/26 19:35
 **/
public class WorkSender {
    private static final Logger LOGGER= LoggerFactory.getLogger(WorkSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String queueName="work.hello";


    /**
     * 往队列中发送一定的.号
     * @param index
     */
    public void send(int index){
        StringBuilder sb=new StringBuilder("hello");
        //控制.号在3个以内
        int limitIndex=index%3+1;
        for (int i = 0; i < limitIndex; i++) {
            sb.append(".");
        }
        sb.append(index+1);
        String message=sb.toString();
        rabbitTemplate.convertAndSend(queueName,message);
        LOGGER.info("[x] Sent '{}'",message);
    }
}
