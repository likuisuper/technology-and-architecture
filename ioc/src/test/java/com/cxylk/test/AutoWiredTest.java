package com.cxylk.test;

import com.cxylk.annotation.AutoWired;
import com.cxylk.controller.UserController;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * @Classname AutoWiredTest
 * @Description 实现Autowired注解功能
 * @Author likui
 * @Date 2021/1/18 16:10
 **/
public class AutoWiredTest {
    @Test
    public void test() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        UserController userController=new UserController();
        System.out.println(userController.getUserService());
        Class<? extends UserController> clazz = userController.getClass();
        Field userService = clazz.getDeclaredField("userService");
        userService.setAccessible(true);
        //获取注解
        AutoWired annotation = userService.getAnnotation(AutoWired.class);
        //如果确实存在注解
        if(annotation!=null){
            //获取字段类型
            Class<?> type = userService.getType();
            //拿到字段类型便可用获取构造函数
            Constructor<?> constructor = type.getConstructor();
            //创建实例
            Object o = constructor.newInstance();
            //设置字段值
            userService.set(userController,o);
        }
        System.out.println(userController.getUserService().hello());//利用反射实现依赖注入
    }
}
