package com.cxylk.test;

import com.cxylk.controller.UserController;
import com.cxylk.service.UserService;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Classname DITest
 * @Description 利用反射实现ioc依赖注入的效果，给controller类中的service赋值。第一种方式是使用set方法
 * @Author likui
 * @Date 2021/1/18 15:09
 **/
public class DIBySetTest {
    @Test
    public void testDI() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserController userController=new UserController();
        System.out.println(userController.getUserService());//null
        //获取class对象
        Class<? extends UserController> clazz = userController.getClass();
        //获取其中的字段值
        Field userServiceField = clazz.getDeclaredField("userService");
        //因为是private的，所以设置为可访问
        userServiceField.setAccessible(true);
        UserService userService=new UserService();
//        System.out.println(userService);
        //获取字段名称
        String name = userServiceField.getName();
//        System.out.println(name);//userService
        //只有通过方法才能设置属性值
        //拼接方法
        String nameStr = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
//        System.out.println(nameStr);//UserService
        //方法名称
        String setMethodName="set"+nameStr;
        //前提是该类中存在该方法
//        Method setUserService = clazz.getMethod("setUserService", UserService.class);
        //不知道方法名称的前提下可用根据字段名来拼接方法名
        Method setUserService = clazz.getMethod(setMethodName, UserService.class);
        //反射调用
        setUserService.invoke(userController,userService);
        System.out.println(userController.getUserService());
        System.out.println(userController.getUserService().hello());//利用反射实现依赖注入
    }
}
