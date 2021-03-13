package com.cxylk.controller;

import com.cxylk.annotation.AutoWired;
import com.cxylk.service.UserService;

/**
 * @Classname UserController
 * @Description 利用反射实现ioc依赖注入的效果，实现AutoWired注解
 * @Author likui
 * @Date 2021/1/18 15:08
 **/
public class UserController {
    @AutoWired
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
