package com.cxylk.common.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname AopConfig
 * @Description impl切面配置
 * @Author likui
 * @Date 2020/12/6 20:06
 **/
@Aspect
@Configuration
public class AopConfig {
    private static final Logger log= LoggerFactory.getLogger(AopConfig.class);

    @Pointcut("execution(public * com.cxylk.service.impl.*.*(..))")
    public void aopLog(){
    }

    @Before("aopLog()")
    public void invokeBefore(JoinPoint joinPoint){
        String realClassName=getRealClassName(joinPoint);
        List<Object> args=Arrays.asList(joinPoint.getArgs());
        log.info("*****"+realClassName+"类执行"+getMethod(joinPoint)+"方法之前,传入参数为:"+args+"*****");
    }

    /**
     * 后置通知，在该通知中还不能返回目标方法执行的结果
     * @param joinPoint 连接点对象
     */
    @After("aopLog()")
    public void after(JoinPoint joinPoint){
        String realClassName=getRealClassName(joinPoint);
        log.info("*****"+realClassName+"类执行"+getMethod(joinPoint)+"方法之后"+"*****");
    }

    @AfterReturning(value = "aopLog()",returning = "keys")
    public void afterReturning(JoinPoint joinPoint,Object keys){
        String realClassName=getRealClassName(joinPoint);
        log.info("*****"+realClassName+"类执行"+getMethod(joinPoint)+"方法之后,返回:"+keys+"*****");
    }

    /**
     * 这里的切入点变了，不再限制为public方法
     * @param e
     */
    @AfterThrowing(value = "execution(* com.cxylk.service.impl.*.*(..))",throwing = "e")
    public void throwsException(JoinPoint joinPoint,Throwable e){
        String realClassName=getRealClassName(joinPoint);
        log.info("*****"+"Exception所在方法"+realClassName+"."+getMethod(joinPoint)+
                "Exception信息:"+e.getMessage());
    }

    /**
     * 获取被代理对象的全限定名
     * @param joinPoint 连接点对象
     * @return
     */
    private String getRealClassName(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass().getName();
    }

    /**
     * 获取代理执行的方法名(不是全名，省略了修饰符返回类型等)
     * @param joinPoint 连接点对象
     * @return
     */
    private String getMethod(JoinPoint joinPoint){
        return joinPoint.getSignature().getName();
    }
}
