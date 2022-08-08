package com.cxylk.reflectionannotationtype;
import lombok.extern.slf4j.Slf4j;

/**
 * @author likui
 * @date 2022/8/5 上午10:49
 * 反射调用方法遇到重载的坑
 **/
@Slf4j
public class ReflectionIssueApplication {
    public static void main(String[] args) throws Exception {
        //1、不使用反射，走哪个重载方法通过参数可以很清晰的知道
        ReflectionIssueApplication application=new ReflectionIssueApplication();
//        application.age(36);
//        application.age(Integer.valueOf(36));
        //2、使用反射，根据入参确定方法重载
        application.wrong();
        //3.使用class，无论是包装类型还是基本类型，都会走Integer这个参数的方法
        application.right();
    }

    public void age(int age){
        log.info("int age = {}", age);
    }

    public void age(Integer age){
        log.info("Integer age = {}", age);
    }

    public void wrong() throws Exception {
        //Integer.TYPE还是int
        //输出结果还是 int age = 36
        getClass().getDeclaredMethod("age", Integer.TYPE).invoke(this, Integer.valueOf("36"));
    }

    public void right() throws Exception {
        //使用实际的参数类型
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, Integer.valueOf("36"));
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, 36);
    }

}
