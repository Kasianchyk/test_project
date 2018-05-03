package ru.javabegin.training.spring.aop.postprocessors;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionAspect {

    @Pointcut("execution(int ru.javabegin.training.spring.impl.*.*(..))")
    public void traceMethod(){}

    @Before("traceMethod()")
    public void beforeMethodStart(JoinPoint joinPoint){
        System.out.println("Method " + joinPoint.getSignature().getName() + " started...");
    }

    @After("traceMethod()")
    public void afterMethodFinished(JoinPoint joinPoint){
        System.out.println("Method " + joinPoint.getSignature().getName() + " finished");
    }

}
