package com.xinYuan.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 描述：     打印请求和响应信息（拦截请求），AOP统一打印请求和返回信息
 */
@Aspect
@Component  //为了让spring识别到
public class WebLogAspect {

    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.xinYuan.controller.*.*(..)))")
    public void webLog() {

    }

    //在拦截点前拦截（打印请求信息）; joinPoint 给我们提供请求参数(joinPoint记录了一些关于类的信息（如方法信息）)
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //打印信息到日志中
        log.info("URL : " + request.getRequestURL().toString());  //用info类别来记录请求信息
        log.info("HTTP_METHOD :" + request.getMethod());   //打印请求类型
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());  //获取类的一些信息以及签名信息
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));  //打印参数，joinPoint.getArgs()得到参数数组
    }

    //返回是也需要拦截住，因为返回时已经生成了请求响应，这个响应也很重要（Object res正是所需要返回的东西）
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容
        //ObjectMapper().writeValueAsString(res)是把对象转为JSON的工具
        log.info("RESPONSE : " + new ObjectMapper().writeValueAsString(res));
    }
}
