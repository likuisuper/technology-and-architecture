package com.cxylk.controller;

import com.cxylk.common.api.CommonResult;
import com.cxylk.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UmsMemberController
 * @Description TODO
 * @Author likui
 * @Date 2020/12/6 16:00
 **/
@Api(tags = "会员登录注册管理",value = "会员登录注册管理")
@RestController
@RequestMapping("/ums/login")
public class UmsMemberController {
    @Autowired
    private UmsMemberService umsMemberService;

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
    public CommonResult<Object> getAuthCode(String phone){
        return umsMemberService.generateAuthCode(phone);
    }

    @ApiOperation(value = "校验验证码")
    @RequestMapping(value = "/verifyCode",method = RequestMethod.POST)
    public CommonResult<Object> verifyCode(String phone,String code){
        return umsMemberService.verifyAuthCode(phone,code);
    }
}
