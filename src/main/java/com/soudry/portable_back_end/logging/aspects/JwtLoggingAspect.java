package com.soudry.portable_back_end.logging.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JwtLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(JwtLoggingAspect.class);

    @Around("com.soudry.portable_back_end.logging.ModulePointcuts.jwtFlow()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            MDC.put("user", auth.getName());
        } else {
            MDC.put("user", "anonymous");
        }

        String method = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            log.info("[JWT] {}", method);
            Object result = pjp.proceed();
            log.info("[JWT DONE] {} {} ms", method, System.currentTimeMillis() - start);
            return result;

        } catch (Throwable ex) {
            log.error("[JWT ERROR] {}", method, ex);
            throw ex;

        } finally {
            // MDC.remove("user");
        }
    }
}