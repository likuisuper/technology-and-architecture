package com.cxylk.controller;

import com.cxylk.common.api.CommonResult;
import com.cxylk.simple.SimpleSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname RabbitController
 * @Description rmq测试接口
 * @Author likui
 * @Date 2020/12/26 16:43
 **/
@Api(value = "RabbitController",tags = {"RabbitController"})
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    @Autowired
    private SimpleSender simpleSender;

    @ApiOperation(value = "简单模式")
    @RequestMapping(value = "/simple",method = RequestMethod.GET)
    public CommonResult<Object> simpleTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            simpleSender.send();
            Thread.sleep(1000);
        }
        return CommonResult.success(null);
    }
}
