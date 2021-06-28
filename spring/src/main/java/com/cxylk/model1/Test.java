package com.cxylk.model1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Classname Test
 * @Description TODO
 * @Author likui
 * @Date 2021/6/26 18:10
 **/
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(App.class);
        //测试环境是否正常
//        System.out.println(ac.getBean("app"));
        //获取x,失败，因为没加注解
//        System.out.println(ac.getBean("x"));
        //获取y，加了注解，能正常获取
        //postProcessBeanFactory方法中将Y关联的bd设置为X，就会报错
//        System.out.println(ac.getBean(Y.class));
        //此时X能获取到
        System.out.println(ac.getBean(X.class));
    }
}
