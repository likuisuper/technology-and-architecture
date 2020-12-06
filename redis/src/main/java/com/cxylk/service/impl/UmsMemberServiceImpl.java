package com.cxylk.service.impl;

import com.cxylk.common.api.CommonResult;
import com.cxylk.service.RedisService;
import com.cxylk.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Classname UmsMemberServiceImpl
 * @Description TODO
 * @Author likui
 * @Date 2020/12/6 15:32
 **/
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTHCODE;

    @Value("${redis.key.expire.authCode}")
    private Long AUTHCODE_EXPIRE_SECONDS;

    @Override
    public CommonResult<Object> generateAuthCode(String phone) {
        if(StringUtils.isEmpty(phone)){
            return CommonResult.failed("请输入手机号码");
        }
        StringBuilder builder=new StringBuilder();
        ThreadLocalRandom random=ThreadLocalRandom.current();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        //验证码帮绑定手机号并存储到redis中
        redisService.set(REDIS_KEY_PREFIX_AUTHCODE+phone,builder.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTHCODE+phone,AUTHCODE_EXPIRE_SECONDS);
        return CommonResult.success(builder.toString(),"获取验证码成功");
    }

    @Override
    public CommonResult<Object> verifyAuthCode(String phone, String code) {
        if(StringUtils.isEmpty(code)){
            return CommonResult.failed("请输入验证码");
        }
        if(!redisService.expire(REDIS_KEY_PREFIX_AUTHCODE+phone,AUTHCODE_EXPIRE_SECONDS)){
            return CommonResult.failed("验证码失效");
        }
        String realCode=redisService.get(REDIS_KEY_PREFIX_AUTHCODE+phone);
        if(code.equals(realCode)){
            return CommonResult.success(null,"验证码验证成功");
        } else{
           return CommonResult.failed("验证失败");
        }
    }
}
