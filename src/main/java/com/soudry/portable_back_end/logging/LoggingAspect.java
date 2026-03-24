package com.soudry.portable_back_end.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

   @Around("execution(* com.soudry.portable_back_end..*(..))")
    public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {

        String method = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            log.info("START {}", method);
            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - start;
            log.info("END {} took {} ms", method, time);
            return result;

        } catch (Throwable ex) {
            long time = System.currentTimeMillis() - start;
            log.error("ERROR {} after {} ms", method, time, ex);
            throw ex;
        }
    }
}