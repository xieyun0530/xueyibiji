package com.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by vn0790t on 2020/6/9.
 */
//@Aspect
//@Component
public class LoginAop {
    /**
     * @Pointcut 定义切入点
     *
     * @Pointcut(value = "execution (* com.lming.service.*..*.*(..))")  service.所有子包 下面所有的类所有的方案
     */
    @Pointcut("execution (* com.spring..*.*(..))")
    public void loginAop() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before("loginAop()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println(">>>>>>>前置通知<<<<<<<<<<< ");
    }

    /**
     * 后置通知
     */
    @After("loginAop()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.println(">>>>>>>>后置通知<<<<<<<<<");
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     */
    @Around("loginAop()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(">>>>环绕通知之前执行...>>>>>>");
        joinPoint.proceed();// 执行目标方案
        System.out.println(">>>>环绕通知之后执行...>>>>>>");
    }

    /**
     * 运行通知
     */
    @AfterReturning("loginAop()")
    public void afterReturning(JoinPoint joinPoint) {
        System.out.println("运行通知执行.....");
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     */
    @AfterThrowing("loginAop()")
    public void afterThrowing(JoinPoint joinPoint) {
        System.out.println(">>>>>异常通知");
    }
}
