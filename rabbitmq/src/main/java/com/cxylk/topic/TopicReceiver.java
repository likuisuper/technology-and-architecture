package com.cxylk.topic;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * @Classname TopicReceiver
 * @Description 通配符模式下的消费者。由于该消费者可以同时从两个队列获取并消费信息，可以看做两个消费者，
 *              分别为instance1和instance2
 * @Author likui
 * @Date 2020/12/27 16:55
 **/
public class TopicReceiver {
    private static final Logger LOGGER=LoggerFactory.getLogger(TopicReceiver.class);

    @RabbitListener(queues = "#{topicQueue1.name}")
    public void receive1(String in){
        doReceive(in,1);
    }

    @RabbitListener(queues = "#{topicQueue2.name}")
    public void receive2(String in){
        doReceive(in,2);
    }

    public void doReceive(String in,int receiver){
        StopWatch watch=new StopWatch();
        watch.start();
        LOGGER.info("instance {} [x] Received '{}'",receiver,in);
        doWork(in);
        watch.stop();
        LOGGER.info("instance {} 耗时 {}s",receiver,watch.getTotalTimeSeconds());
    }

    private void doWork(String in) {
        for (char c : in.toCharArray()) {
            if (c == '.') {
                ThreadUtil.sleep(1000);
            }
        }
    }
}
