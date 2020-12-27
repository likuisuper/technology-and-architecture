package com.cxylk.fanout;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * @Classname FanoutReceiver
 * @Description 发布/订阅模式下的消费者，和工作模式不同，该消费者可以同时从队列中获取并消费信息
 * @Author likui
 * @Date 2020/12/27 14:39
 **/
public class FanoutReceiver {
    private static final Logger LOGGER= LoggerFactory.getLogger(FanoutReceiver.class);

    //因为是匿名队列，所以这样写,如果不是匿名队列，和前面几种模式写法一样
    @RabbitListener(queues = "#{fanoutQueue1.name}")
    public void receive1(String in){
        doReceiver(in,1);
    }

    @RabbitListener(queues = "#{fanoutQueue2.name}")
    public void receive2(String in){
        doReceiver(in,2);
    }

    private void doReceiver(String message,int receiver){
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        LOGGER.info("instance{} [x] Received '{}'",receiver,message);
        doWork(message);
        stopWatch.stop();
        LOGGER.info("instance{} [x] Done in {}s",receiver,stopWatch.getTotalTimeSeconds());

    }

    private void doWork(String message) {
        for (char c : message.toCharArray()) {
            //包含.号越多，耗时越长
            if(c=='.'){
                ThreadUtil.sleep(1000);
            }
        }
    }
}
