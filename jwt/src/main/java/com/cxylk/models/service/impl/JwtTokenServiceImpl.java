package com.cxylk.models.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.cxylk.domain.PlayloadDto;
import com.cxylk.exception.JwtExpiredException;
import com.cxylk.exception.JwtInvalidException;
import com.cxylk.models.service.JwtTokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader=new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到Payload中
        Payload payload=new Payload(payloadStr);
        //创建JWS对象
        JWSObject jwsObject=new JWSObject(jwsHeader,payload);
        //创建RSA签名器
        JWSSigner jwsSigner=new RSASSASigner(rsaKey,true);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    @Override
    public PlayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws ParseException, JOSEException {
        //从token中解析JWS对象
        JWSObject jwsObject=JWSObject.parse(token);
        RSAKey publicRsaKey=rsaKey.toPublicJWK();
        //使用RSA公钥创建RSA验证器
        JWSVerifier jwsVerifier=new RSASSAVerifier(publicRsaKey);
        if(!jwsObject.verify(jwsVerifier)){
            throw new JwtInvalidException("token签名不合法");
        }
        String payload=jwsObject.getPayload().toString();
        PlayloadDto playloadDto=JSONUtil.toBean(payload,PlayloadDto.class);
        if(playloadDto.getExp()<new Date().getTime()){
            throw new JwtExpiredException("token已过期");
        }
        return playloadDto;
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
        //从classpath下获取RSA密钥对
        //在jdk的bin目录下打开cmd窗口输入keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks命令，密码设置为123456
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),
                "123456".toCharArray());
        KeyPair keyPair=keyStoreKeyFactory.getKeyPair("jwt","123456".toCharArray());
        //获取RSA公钥
        RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
        //获取RSA私钥
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    }

}
