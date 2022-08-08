package com.cxylk.spring.iocandaop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * @author likui
 * @date 2022/8/5 下午3:16
 **/
@Slf4j
@Service
//必须要加上代理模式才能让scope生效，变为原型模式，适用于有状态的bean
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SayHello extends SayService{
    @Override
    public void say() {
        super.say();
        log.info("hello");
    }
}
