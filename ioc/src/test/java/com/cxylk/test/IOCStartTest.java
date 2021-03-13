package com.cxylk.test;

import com.cxylk.text.Car;
import com.cxylk.text.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Classname IOCStartTest
 * @Description 测试ioc容器
 * @Author likui
 * @Date 2021/1/16 23:26
 **/
public class IOCStartTest {
    @Test
    public void testRun(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("car.xml");
        Car car= (Car) applicationContext.getBean("car");
//        Car car=applicationContext.getBean(Car.class);
//        Car car=applicationContext.getBean("car",Car.class);
//        User user= (User) applicationContext.getBean("user");
//        System.out.println(user.getName());
        System.out.println(car.getName());
    }
}
