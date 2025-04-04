package com.connectify.demo.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RestApiLogging {

    @Before("com.connectify.demo.Aspect.PoincutExpression.controllerLoggingPointcut()")
    public void controllerLoggingAspect(JoinPoint joinPoint) {
        String methodSignature = joinPoint.getSignature().toShortString();
        logInfo(methodSignature);
    }

    @Before("com.connectify.demo.Aspect.PoincutExpression.ServiceLoggingPointcut()")
    public void ServiceLoggingAspect(JoinPoint joinPoint) {
        String methodSignature = joinPoint.getSignature().toShortString();
        logInfo(methodSignature);
    }

    @Before("com.connectify.demo.Aspect.PoincutExpression.exceptionsLoggingPointcut()")
    public void exceptionsLoggingAspect(JoinPoint joinPoint) {
        String methodSignature = joinPoint.getSignature().toString();
        logInfo(methodSignature);
        Object[] args = joinPoint.getArgs();

        for (Object obj : args) {
            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                log.error("<------------------Connectify-ERROR-------------------->");
                logError(methodSignature, e.getMessage());
                if (e.getCause() != null) {
                    logTrace(e.getStackTrace());
                } else {
                    logTrace(e.getStackTrace());
                }
            }
        }
    }

    private void logTrace(Object message) {
        log.error("Trace: {}", message);
    }

    private void logError(String name, String message) {
        log.error("Error in {} - Message: {}", name, message);
    }

    private void logInfo(String message) {
        log.info("====>> {}", message);
    }
}
