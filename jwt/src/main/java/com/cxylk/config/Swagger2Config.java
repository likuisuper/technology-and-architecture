package com.cxylk.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname Swagger2Config
 * @Description Swagger2配置：Swagger对生成API文档范围有三种不同的选择
 *              1.生成指定包下面的类的API文档
 *              2.生成带有指定注解的类的API文档
 *              3.生成带有指定注解的方法的API文档
 * @Author likui
 * @Date 2020/11/26 9:51
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {
    public Docket creatRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包下Controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.cxylk.models.controller"))
                //为有@Api注解的Controller生成API文档
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //为有@ApiOperation注解的方法生成API文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger文档")
                .description("jwt实战")
                .contact(new Contact("cxylk","https://github.com/likuisuper","cxylikui@163.com"))
                .version("1.0")
                .build();
    }
}
