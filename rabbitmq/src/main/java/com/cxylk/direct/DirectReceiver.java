package com.cxylk.direct;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * @Classname DirectReceiver
 * @Description 路由模式下的消费者
 * @Author likui
 * @Date 2020/12/27 16:05
 **/
public class DirectReceiver {
    private static final Logger LOGGER= LoggerFactory.getLogger(DirectReceiver.class);

    @RabbitListener(queues = "#{directQueue1.name}")
    public void receiver1(String message){
        doReceive(message,1);
    }

    @RabbitListener(queues = "#{directQueue2.name}")
    public void receiver2(String message){
        doReceive(message,2);
    }

    private void doReceive(String message,int receiver){
        StopWatch watch=new StopWatch();
        watch.start();
        LOGGER.info("instance {} [x] Received '{}'",receiver,message);
        doWork(message);
        watch.stop();
        LOGGER.info("instance {} Done in {}s",receiver,watch.getTotalTimeSeconds());
    }

    private void doWork(String message) {
        for (char c : message.toCharArray()) {
            if(c=='.'){
                ThreadUtil.sleep(1000);
            }
        }
    }
}
