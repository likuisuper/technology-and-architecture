package com.cxylk.test;

import com.cxylk.controller.UserController;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @Classname DIByConstructTest
 * @Description 第二种方式是使用构造函数完成注入
 * @Author likui
 * @Date 2021/1/18 15:33
 **/
public class DIByConstructTest {
    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //获取class对象
        UserController userController=new UserController();
        System.out.println(userController.getUserService());
        Class<? extends UserController> clazz = userController.getClass();
        Field userService = clazz.getDeclaredField("userService");
        userService.setAccessible(true);
        //获取字段类型
        Class<?> type = userService.getType();
//        System.out.println(type);//class com.cxylk.service.UserService
        //获取该类型实例
//        Object instance = type.newInstance();
//        userService.set(userController,instance);
//        System.out.println(userController.getUserService());
//        //能够调用该实例方法说明我们已经拿到了该类的实例
//        System.out.println(userController.getUserService().hello());

        //spring5以上建议使用这种方法获取类的实例
        Constructor<?> constructor = type.getConstructor();
        Object o = constructor.newInstance();
        userService.set(userController,o);
        System.out.println(userController.getUserService());
        //能够调用该实例方法说明我们已经拿到了该类的实例
        System.out.println(userController.getUserService().hello());
    }
}
