package com.cxylk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.cxylk.domain.PlayloadDto;
import com.cxylk.exception.JwtExpiredException;
import com.cxylk.exception.JwtInvalidException;
import com.cxylk.service.JwtTokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAKey;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * @Classname JwtTokenServiceImpl
 * @Description TODO
 * @Author likui
 * @Date 2020/11/26 10:50
 **/
@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    @Override
    public String generateTokenByHMAC(String payloadStr, String secret) throws JOSEException {
        //创建JWS(签名)头，设置签名算法和类型
        JWSHeader jwsHeader=new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到Payload中
        Payload payload=new Payload(payloadStr);
        //创建JWS对象
        JWSObject jwsObject=new JWSObject(jwsHeader,payload);
        //创建HMAC签名器
        JWSSigner jwsSigner=new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    @Override
    public PlayloadDto verifyTokenByHMAC(String token, String secret) throws ParseException, JOSEException {
        //从token中解析JWS对象
        JWSObject jwsObject=JWSObject.parse(token);
        //创建HMAC验证器
        JWSVerifier jwsVerifier=new MACVerifier(secret);
        if(!jwsObject.verify(jwsVerifier)){
            throw new JwtInvalidException("token签名不合法");
        }
        String payload=jwsObject.getPayload().toString();
        PlayloadDto playloadDto= JSONUtil.toBean(payload,PlayloadDto.class);
        if(playloadDto.getExp()<new Date().getTime()){
            throw new JwtExpiredException("token已过期");
        }
        return playloadDto;
    }

    @Override
    public String generateTokenByRSA(String payloadStr, RSAKey rsaKey) throws JOSEException {
        return null;
    }

    @Override
    public PlayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws ParseException {
        return null;
    }

    @Override
    public PlayloadDto getDefaultPayloadDto() {
        Date now=new Date();
        Date exp= DateUtil.offsetSecond(now,60*60);
        return PlayloadDto.builder()
                .sub("cxylk")
                .iat(now.getTime())
                .exp(exp.getTime())
                .jti(UUID.randomUUID().toString())
                .username("cxylk")
                .authorities(CollUtil.toList("ADMIN"))
                .build();
    }

    @Override
    public RSAKey getDefaultRSAKey() {
        return null;
    }
}
