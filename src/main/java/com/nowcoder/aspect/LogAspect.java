package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定义切面
 * @author 86156
 *
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {   //JoinPoint对象封装了AOP中切面方法的信息
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {  //getArgs()获取传入切面方法的参数信息
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method:" + sb.toString());
    }

    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void afterMethod() {
        logger.info("after method" + new Date());
    }
}
