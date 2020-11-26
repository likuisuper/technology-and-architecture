package com.cxylk.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.cxylk.common.CommonResult;
import com.cxylk.common.ResultCode;
import com.cxylk.domain.PlayloadDto;
import com.cxylk.service.JwtTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @Classname JwtTokenController
 * @Description JWT令牌管理
 * @Author likui
 * @Date 2020/11/26 13:38
 **/
@Api(tags = "JWT令牌管理")
@RequestMapping("/token")
@RestController
public class JwtTokenController {
    @Autowired
    private JwtTokenService jwtTokenService;

    @ApiOperation(value = "使用对称加密(HMAC)算法生成token")
    @RequestMapping(value = "/hmac/generate",method = RequestMethod.GET)
    public CommonResult generateTokenByHMAC(){
        try {
            PlayloadDto playloadDto=jwtTokenService.getDefaultPayloadDto();
            //由于HMAC算法需要长度至少为32个字节的密钥，所以这里使用MD5加密下
            String token=jwtTokenService.generateTokenByHMAC(JSONUtil.toJsonStr(playloadDto),
                    SecureUtil.md5("test"));
            return CommonResult.success(token);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "使用对称加密(HMAC)算法验证token")
    @RequestMapping(value = "/hmac/verify",method = RequestMethod.GET)
    public CommonResult verifyTokenByHMAC(String token){
        try {
            PlayloadDto playloadDto=jwtTokenService.verifyTokenByHMAC(token,SecureUtil.md5("test"));
            return CommonResult.success(playloadDto);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取非对称加密(RSA)算法的公钥")
    @RequestMapping(value = "/rsa/publicKey",method = RequestMethod.GET)
    public Object getRSAPublicKey(){
        RSAKey rsaKey=jwtTokenService.getDefaultRSAKey();
        return new JWKSet(rsaKey).toJSONObject();
    }

    @ApiOperation(value = "使用非对称加密算法(RSA)生成token")
    @RequestMapping(value = "rsa/generate",method = RequestMethod.GET)
    public CommonResult generateTokenByRSA(){
        try {
            PlayloadDto playloadDto=jwtTokenService.getDefaultPayloadDto();
            RSAKey rsaKey=jwtTokenService.getDefaultRSAKey();
            String token=jwtTokenService.generateTokenByRSA(JSONUtil.toJsonStr(playloadDto),rsaKey);
            return CommonResult.success(token);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "使用非对称加密算法(RSA)校验token")
    @RequestMapping(value = "rsa/verify",method = RequestMethod.GET)
    public CommonResult verifyTokenByRSA(String token){
        try {
            PlayloadDto playloadDto=jwtTokenService.verifyTokenByRSA(token,jwtTokenService.getDefaultRSAKey());
            return CommonResult.success(playloadDto);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
