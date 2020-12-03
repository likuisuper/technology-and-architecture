package com.cxylk.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Classname WebLog
 * @Description controller层的日志信息封装类，用于封装需要记录的日志信息，包括操作的描述、
 *              时间、消耗时间、url、请求参数和返回结果等信息
 * @Author likui
 * @Date 2020/12/2 15:39
 **/
@Data
public class WebLog {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 请求返回结果
     */
    private Object result;
}
