package com.cxylk.exception;

/**
 * @Classname JwtInvalidException
 * @Description 自定义token验证异常
 * @Author likui
 * @Date 2020/11/26 11:05
 **/
public class JwtInvalidException extends RuntimeException{
    public JwtInvalidException(String message){
        super(message);
    }
}
