package com.connectify.demo.Aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutExpression {

    @Pointcut("execution(* com.connectify.demo.Security.JwtAuthFilter.doFilterInternal(..))")
    public void jwtAuthenticationPointcut() {
    }

    @Pointcut("execution(* com.connectify.demo.Controller.*.*(..))")
    public void controllerLoggingPointcut() {
    }

    @Pointcut("execution(* com.connectify.demo.Service.*.*(..))")
    public void serviceLoggingPointcut() {
    }

    @Pointcut("execution(* com.connectify.demo.Security.Handler.*.*(..))")
    public void exceptionsLoggingPointcut() {
    }
}
