package com.cxylk;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @Classname com.cxylk.MyBeanFactoryPostProcessor
 * @Description 在执行流程里面想修改bean定义信息的话就实现该接口，然后重写方法
 * @Author likui
 * @Date 2021/1/15 22:43
 **/
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //得到一个对Bean描述信息的数据结构BeanDefinition
        BeanDefinition aa = configurableListableBeanFactory.getBeanDefinition("aa");
        //可以看到，拿到BeanDefinition后我们可以修改Bean的信息
//        aa.setBeanClassName("aaaa");
//        aa.setDependsOn("aaaa");
    }
}
