package com.cxylk.config;

import com.cxylk.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname LogAspect
 * @Description 配置日志切面
 * @Author likui
 * @Date 2020/12/6 16:08
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {
    long longTime = 10000;

    /**
     * 定义切入点，即定义何地
     */
    @Pointcut("execution(public * com.cxylk.controller.*.*(..))")
    public void log() {
    }

    /**
     * 执行方法前，获取client参数
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取客户端ip
        String ip = request.getRemoteAddr();
        //获取客户端主机
        String host = request.getRemoteHost();
        //获取请求方法
        String method = request.getMethod();
        //获取端口
        int port = request.getRemotePort();
        //获取url
        String url = request.getRequestURL().toString();
        //记录client信息
        log.info("client:ip=" + ip + ";host=" + host + ";method=" + method + ";port=" + port + ";url=" + url);
        //类名
        String classname = joinPoint.getTarget().getClass().getSimpleName();
        //方法
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        //日志记录，类，方法，参数
        log.info("**********class name:" + classname + ",method name:" + methodName + "*********");
    }

    @AfterReturning(value = "log()", returning = "keys")
    public void doAfterReturning(JoinPoint point, Object keys) {
        //获取类的全限定名
        String realName = point.getTarget().getClass().getName();
        log.info("*****" + realName + "类执行" + point.getSignature().getName() + "方法*****返回值:" + keys);
    }

    /**
     * 方法执行
     *
     * @param proceedingJoinPoint
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //创建一个秒表，用于对一段代码耗时检测
        StopWatch stopWatch = new StopWatch();
        Object result = null;
        try {
            stopWatch.start();
            //必须调用proceed方法，不然会导致目标方法没有执行
            result = proceedingJoinPoint.proceed();
            stopWatch.stop();
            //日志暂不记录参数，放在参数包含敏感信息
            long time = stopWatch.getTime();
            if (time > longTime) {
                log.info("执行方法:{},耗时:{}ms(毫秒),耗时较长", proceedingJoinPoint.getSignature(), time);
            }else {
                log.info("执行方法:{},耗时:{}ms(毫秒)",proceedingJoinPoint.getSignature(), time);
            }
        } catch (Throwable throwable) {
            result=handleException(proceedingJoinPoint,throwable);
        }
        return result;
    }

    /**
     * 后置异常通知
     * @param e
     */
    @AfterThrowing(pointcut = "log()",throwing = "e")
    public void doException(Throwable e){
        if(e!=null){
            log.error("doException系统异常"+e.getMessage(),e);
        }
    }

    private CommonResult<?> handleException(ProceedingJoinPoint pjp, Throwable e) throws Throwable {
        // 为了兼容 Log4J暂未启用slf4j做日志门面 故日志打印写两个错误输出
        // 若启用self4j可以直接使用
        log.error("Exception{方法：{}， 参数：{}}", pjp.getSignature(), pjp.getArgs());
        log.error(e.getMessage(), e);
        CommonResult<?> commonResult=new CommonResult<>();
        commonResult.setCode((long) 500);
        commonResult.setMessage("后端接口错误");
        commonResult.setData(null);
        return commonResult;
    }
}
