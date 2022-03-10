package com.cxylk.mqreliable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


//    public void sendTenant(TenantDTO data) {
//        MessageProperties properties = new MessageProperties();
//        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
//        Message message = new Message(JSONObject.toJSONBytes(data), properties);
//        rabbitTemplate.send("这里是交换机", "路由键", message);
//    }


}
