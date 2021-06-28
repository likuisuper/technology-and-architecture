package com.cxylk.model1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @Classname MyBeanFactoryPostProcessor
 * @Description BeanFactoryPostProcessor执行时间，在扫描完BeanDefinition之后，初始化之前
 *              不加注解的话，spring容器是识别不了的
 * @Author likui
 * @Date 2021/6/26 18:17
 **/
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    /**
     * 执行时机：扫描完成之后，也就是类变成BeanDefinition之后，在实例化之前
     * @param beanFactory
     * @throws BeansException
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //它在Test类中的输出之前输入
//        System.out.println(beanFactory.getBean("y")+"后置处理器");
        //拿到类Y的BeanDefinition,并将这个bd中关联的class设置为x
//        GenericBeanDefinition y = (GenericBeanDefinition) beanFactory.getBeanDefinition("y");
//        y.setBeanClass(X.class);
        System.out.println("-----beanFactoryPostProcessor-----");
    }
}
