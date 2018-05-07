package ru.javabegin.training.spring.aop.postprocessors;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    @Around("traceMethod()")
    public Object aroundMethods(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
        Object object = null;
        try {
            object = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("Around aspect finished............");
        return object;
    }

}
