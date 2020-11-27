package com.cxylk.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MybatisConfig
 * @Description Mybatis配置
 * @Author likui
 * @Date 2020/11/26 10:10
 **/
@Configuration
@MapperScan({"com.cxylk.mbg.mapper","com.cxylk.dao"})
public class MybatisConfig {
}
