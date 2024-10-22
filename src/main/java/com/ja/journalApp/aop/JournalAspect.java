package com.ja.journalApp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class JournalAspect {

    @Before(value = "execution(* com.ja.journalApp.controller.UserController.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){
        log.info("Request to "+ joinPoint.getSignature() + " started at " + new Date());
    }
    @After(value = "execution(* com.ja.journalApp.controller.UserController.*(..))")
    public void afterAdvice(JoinPoint joinPoint){
        log.info("Request to "+ joinPoint.getSignature() + " ended at " + new Date());
    }

    @Before(value = "execution(* com.ja.journalApp.service.UserService.*(..))")
    public void beforeAdviceForService(JoinPoint joinPoint){
        log.info("Request to "+ joinPoint.getSignature() + " started at " + new Date());
    }
    @After(value = "execution(* com.ja.journalApp.service.UserService.*(..))")
    public void afterAdviceForService(JoinPoint joinPoint){
        log.info("Request to "+ joinPoint.getSignature() + " ended at " + new Date());
    }



}
