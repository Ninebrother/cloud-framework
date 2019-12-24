package com.cloud.framework.core.aspect;


import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class HttpAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 请求参数
     */
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController)")
    @Pointcut("execution(* com..controller.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MDC.put("requestId", String.valueOf(System.nanoTime()));
        long startTime = System.currentTimeMillis();
        logger.info("请求方法：{}；请求参数：{}", point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName()
                , Arrays.toString(point.getArgs()));
        try {
            Object obj = point.proceed();
            logger.info("返回值：{}；耗时：{}", JSON.toJSONString(obj), (System.currentTimeMillis() - startTime));
            return obj;
        } finally {
            MDC.clear();
        }
    }
}



