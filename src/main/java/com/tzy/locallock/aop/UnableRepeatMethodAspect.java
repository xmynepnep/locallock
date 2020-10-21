package com.tzy.locallock.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.tzy.locallock.annotation.LocalLock;
import com.tzy.locallock.exception.AspectException;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
public class UnableRepeatMethodAspect {
    private static final Cache<String,String> CACHE  = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.SECONDS).build();

/*    @Pointcut
    public void lockPoint(){}*/

    @Pointcut("@annotation(com.tzy.locallock.annotation.LocalLock)")
    public void annotationPoint(){}

    @Before("annotationPoint()")
    public void lockRepeatLock(JoinPoint point){

        ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=sra.getRequest();

        String sessionId = request.getSession().getId();

        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        Class methodClass = method.getClass();
        String className = methodClass.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName).append("-").append(sessionId);

        String lockKey = sb.toString();
        String lockValue = "isLocking";
        //说明首次请求 没被锁
        if(StringUtils.isBlank( CACHE.getIfPresent(lockKey) )){
            CACHE.put(lockKey,lockValue);
        }
        //重复提交
        else {
            throw new AspectException("-1","重复提交");
        }


    }

    //反射获取注解的参数
    /*@Before("annotationPoint()")
    public void annotationPointTest(JoinPoint point){
        System.out.println("自定义注解被调用");
        ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=sra.getRequest();
        String sessionId = request.getSession().getId();

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = methodSignature.getMethod().getName();
        System.out.println("方法名:"+methodName);
        String[] parArr = methodSignature.getParameterNames();
        System.out.println("参数名称:"+parArr);
     //   String annoName = methodSignature.getMethod().getAnnotated

        LocalLock anno = method.getAnnotation(LocalLock.class);
        String key1 = anno.key();
        String key2 = anno.key2();
        String key3 = anno.key3();
        System.out.println("key1 : " + key1);
        System.out.println("key2 : " + key2);
        System.out.println("key3 : " + key3);

        Class c = method.getDeclaringClass();
        System.out.println(c.getName());
        System.out.println(c.getCanonicalName());
        System.out.println(c.getSimpleName());
        System.out.println(c.getTypeName());

    }*/


}
