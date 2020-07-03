package com.cnnc.springbootmybatis.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class DatasourceAop {

    @Pointcut("execution(* com.cnnc.springbootmybatis.service..*.get*(..))")
    public void readPointcut() {

    }

    @Pointcut("execution(* com.cnnc.springbootmybatis.service..*.save*(..)) " +
            "|| execution(* com.cnnc.springbootmybatis.service..*.add*(..)) " +
            "|| execution(* com.cnnc.springbootmybatis.service..*.update*(..)) " +
            "|| execution(* com.cnnc.springbootmybatis.service..*.edit*(..)) " +
            "|| execution(* com.cnnc.springbootmybatis.service..*.delete*(..)) " +
            "|| execution(* com.cnnc.springbootmybatis.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        System.out.println("read AOP");
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

}
