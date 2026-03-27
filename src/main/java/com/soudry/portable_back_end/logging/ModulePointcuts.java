package com.soudry.portable_back_end.logging;

import org.aspectj.lang.annotation.Pointcut;
public final class ModulePointcuts {

    private ModulePointcuts() {}

    @Pointcut("""
        execution(* com.soudry.portable_back_end.user.controllers..*(..)) ||
        execution(* com.soudry.portable_back_end.user.loginLogic..*(..)) ||
        execution(* com.soudry.portable_back_end.user.registerLogic..*(..))
    """)
    public void userFlow() {}

    @Pointcut("execution(* com.soudry.portable_back_end.auth.services..*(..))")
    public void authFlow() {}

    @Pointcut("execution(* com.soudry.portable_back_end.jwt.services..*(..))")
    public void jwtFlow() {}
}