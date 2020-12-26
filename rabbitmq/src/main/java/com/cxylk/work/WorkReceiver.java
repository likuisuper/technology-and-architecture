package com.cxylk.work;

import cn.hutool.core.thread.ThreadUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @Classname WorkReceiver
 * @Description 工作模式下存在多个竞争的消费者
 * @Author likui
 * @Date 2020/12/26 19:54
 **/
@RabbitListener(queues = "work.hello")
public class WorkReceiver {
    private static final Logger LOGGER= LoggerFactory.getLogger(WorkReceiver.class);

    private final int instance;

    public WorkReceiver(int i){
        this.instance=i;
    }

    @RabbitHandler
    public void receiver(String in){
        StopWatch watch=new StopWatch();
        watch.start();
        LOGGER.info("instance {} [x] Received {}",this.instance,in);
        doWork(in);
        watch.stop();
        LOGGER.info("instance {} [x] Done in {}s",this.instance,watch.getTime());
    }

    private void doWork(String in) {
        for (char c : in.toCharArray()) {
            //包含.号越多，耗时越长
            if(c=='.'){
                ThreadUtil.sleep(1000);
            }
        }
    }
}
