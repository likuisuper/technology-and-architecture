package com.cxylk.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.cxylk.dto.WebLog;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname WebLogAspect 统一日志处理切面
 * @Description 定义一个日志切面类，在环绕通知中获取日志需要的信息，并应用到controller层中所有的public方法上去
 * @Author likui
 * @Date 2020/12/2 15:47
 **/
@Aspect
@Component
@Order(1) //指定切面的优先级，值越小，优先级越高
public class WebLogAspect {
    private static final Logger LOGGER= LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(public * com.cxylk.controller.*.*(..))")
    public void webLog(){
    }

    /**
     * 前置通知，在目标方法调用前调用通知功能
     * @param joinPoint 连接点，程序执行的特定位置，通知功能被应用的时机
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        System.out.println("the method begins...");
    }

    /**
     * 返回通知在方法返回结果之后执行，是可以访问方法的返回值的
     * 在返回通知中，只要将returning属性添加到AfterReturning注解中，就可以访问连接点的返回值，
     * 该属性的值即为用来传入返回值的参数名称。必须在方法的签名中添加一个同名参数
     * @param result 在运行时，AOP会通过这个参数传递返回值
     * @throws Throwable
     */
    @AfterReturning(value = "webLog()",returning = "result")
    public void doAfterReturning(Object result) throws Throwable{
        System.out.println("返回通知");
    }

    /**
     * 在环绕通知中需要明确调用ProceedingJoinPoint的proceed()方法来执行被代理的方法，
     * 如果忘记这样做就会导致通知被执行了，但目标方法没有被执行
     * 环绕通知的方法需要返回目标方法执行之后的结果，即调用joinPoint.proceed()的返回值，
     * 否则会出现空指针异常
     * @param joinPoint 该类型的参数决定是否执行目标方法
     * @return 环绕通知必须有返回值，返回值即为目标方法的返回值
     * @throws Throwable
     */
    @Around("webLog()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime=System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        //记录请求信息
        WebLog webLog=new WebLog();
        Object result=joinPoint.proceed();
        //获取方法签名
        Signature signature=joinPoint.getSignature();
        MethodSignature methodSignature= (MethodSignature) signature;
        //获取代理类的目标对象
        Method method=methodSignature.getMethod();
        //判断当前方法是否加了ApiOperation注解，如果是则返回true
        if(method.isAnnotationPresent(ApiOperation.class)){
            //取出对应的注解
            ApiOperation apiOperation=method.getAnnotation(ApiOperation.class);
            //将注解值设置到webLog的description属性
            webLog.setDescription(apiOperation.value());
        }
        long endTime=System.currentTimeMillis();
        //获取url
        String urlStr=request.getRequestURL().toString();
        //设置根路径，将url中的路径以及参数去掉后的根路径(协议加主机名，端口号)
        //该项目中得到的根路径是 http://localhost:8080
        webLog.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(getParameter(method,joinPoint.getArgs()));
        webLog.setResult(result);
        webLog.setSpendTime((int) (endTime-startTime));
        webLog.setStartTime(startTime);
        webLog.setUri(request.getRequestURI());//以controller中查询为例，这里是"/brand/list"
        webLog.setUrl(request.getRequestURL().toString());//"http://localhost:8080/brand/list"
        LOGGER.info("{}", JSONUtil.parse(webLog));
        return result;
    }

    /**
     * 根据注解和传入的参数获取请求参数
     * @param method
     * @param args
     * @return
     */
    private Object getParameter(Method method,Object[] args){
        List<Object> argList=new ArrayList<>();
        Parameter[] parameters=method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBodies=parameters[i].getAnnotation(RequestBody.class);
            if(requestBodies!=null){
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParams=parameters[i].getAnnotation(RequestParam.class);
            if(requestParams!=null){
                Map<String,Object> map=new HashMap<>();
                String key=parameters[i].getName();
                if(!StringUtils.isEmpty(requestParams.value())){
                    key=requestParams.value();
                }
                map.put(key,args[i]);
                argList.add(map);
            }
        }
        if(argList.size()==0){
            return null;
        }else if(argList.size()==1){
            return argList.get(0);
        }else {
            return argList;
        }
    }
}
