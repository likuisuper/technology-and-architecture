package com.cxylk.models.service;

import com.cxylk.domain.PlayloadDto;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;

import java.text.ParseException;

/**
 * @Classname JwtTokenService
 * @Description JwtToken接口
 * @Author likui
 * @Date 2020/11/26 10:16
 **/
public interface JwtTokenService {
    /**
     * 使用HMAC(对称加密)算法生成token
     * @param payloadStr 载荷：存放有效信息的地方
     * @param secret 私钥
     * @return 生成的token
     * @throws JOSEException
     */
    String generateTokenByHMAC(String payloadStr,String secret) throws JOSEException;

    /**
     * 使用HMAC算法校验token
     * @param token token
     * @param secret 私钥
     * @return
     * @throws ParseException
     */
    PlayloadDto verifyTokenByHMAC(String token,String secret) throws ParseException, JOSEException;

    /**
     * 使用RSA算法生成token
     * @param payloadStr 载荷
     * @param rsaKey
     * @return
     * @throws JOSEException
     */
    String generateTokenByRSA(String payloadStr, RSAKey rsaKey) throws JOSEException;

    /**
     * 使用RSA算法校验token
     * @param token
     * @param rsaKey
     * @return
     * @throws ParseException
     */
    PlayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws ParseException,JOSEException;

    /**
     * 获取默认payload
     * @return
     */
    PlayloadDto getDefaultPayloadDto();

    /**
     * 获取默认RSAKey
     * @return
     */
    com.nimbusds.jose.jwk.RSAKey getDefaultRSAKey();
}
