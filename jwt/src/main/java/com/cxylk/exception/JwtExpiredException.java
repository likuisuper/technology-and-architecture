package com.cxylk.exception;

/**
 * @Classname JwtExpiredException
 * @Description 自定义token过期异常
 * @Author likui
 * @Date 2020/11/26 11:10
 **/
public class JwtExpiredException extends RuntimeException{
    public JwtExpiredException(String message){
        super(message);
    }
}
