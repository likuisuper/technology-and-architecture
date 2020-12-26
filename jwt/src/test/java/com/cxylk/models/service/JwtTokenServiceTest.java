package com.cxylk.models.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.cxylk.domain.PlayloadDto;
import com.nimbusds.jose.JOSEException;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Classname JwtTokenServiceTest
 * @Description TODO
 * @Author likui
 * @Date 2020/12/7 17:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenServiceTest {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void generateTokenByHMAC() throws JOSEException {
//        PlayloadDto playloadDto=jwtTokenService.getDefaultPayloadDto();
//        String token=jwtTokenService.generateTokenByHMAC(JSONUtil.toJsonStr(playloadDto),
//                SecureUtil.md5("test"));
//        System.out.println(token);
        System.out.println(3);
    }
}