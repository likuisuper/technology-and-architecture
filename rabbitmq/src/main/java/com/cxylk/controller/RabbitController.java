package com.cxylk.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.cxylk.common.api.CommonResult;
import com.cxylk.simple.SimpleSender;
import com.cxylk.work.WorkSender;
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
    //简单模式发送
    @Autowired
    private SimpleSender simpleSender;

    //工作模式发送
    @Autowired
    private WorkSender workSender;

    @ApiOperation(value = "简单模式")
    @RequestMapping(value = "/simple",method = RequestMethod.GET)
    public CommonResult<Object> simpleTest() {
        for (int i = 0; i < 10; i++) {
            simpleSender.send();
            ThreadUtil.sleep(1000);
        }
        return CommonResult.success(null);
    }

    @ApiOperation(value = "工作模式")
    @RequestMapping(value = "/work",method = RequestMethod.GET)
    public CommonResult<Object> workTest(){
        for (int i = 0; i < 10; i++) {
            workSender.send(i);
            ThreadUtil.sleep(1000);
        }
        return CommonResult.success(null);
    }
}
