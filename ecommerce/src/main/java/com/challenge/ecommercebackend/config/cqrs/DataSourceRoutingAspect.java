package com.challenge.ecommercebackend.config.cqrs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect para manejar el routing de DataSource basado en paquetes CQRS.
 *
 * Estructura:
 * - persisten/repository/query → DataSource de lectura (ecommerce_reader)
 * - persisten/repository/command → DataSource de escritura (ecommerce_writer)
 */
@Aspect
@Component
@Order(1)
public class DataSourceRoutingAspect {

    /**
     * Intercepta todos los métodos de repositorios en paquetes 'repository/query'
     * y usa el DataSource de lectura
     */
    @Around("execution(* com.challenge.ecommercebackend.modules..*.persisten.repository.query.*.*(..))")
    public Object routeQueryRepositoriesToReadDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setReadDataSource();
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * Intercepta todos los métodos de repositorios en paquetes 'repository/command'
     * y usa el DataSource de escritura
     */
    @Around("execution(* com.challenge.ecommercebackend.modules..*.persisten.repository.command.*.*(..))")
    public Object routeCommandRepositoriesToWriteDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setWriteDataSource();
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * Intercepta métodos anotados con @ReadOnly
     */
    @Around("@annotation(com.challenge.ecommercebackend.config.cqrs.ReadOnly)")
    public Object routeToReadDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setReadDataSource();
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * Intercepta clases anotadas con @ReadOnly
     */
    @Around("@within(com.challenge.ecommercebackend.config.cqrs.ReadOnly)")
    public Object routeClassToReadDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            DataSourceContextHolder.setReadDataSource();
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }
}

