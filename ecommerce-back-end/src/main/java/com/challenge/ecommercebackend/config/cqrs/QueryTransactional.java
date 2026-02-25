package com.challenge.ecommercebackend.config.cqrs;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Anotación para operaciones de LECTURA (Query)
 * - Usa el transactionManager de lectura
 * - readOnly = true por defecto
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional(transactionManager = "queryTransactionManager", readOnly = true)
public @interface QueryTransactional {

    @AliasFor(annotation = Transactional.class, attribute = "readOnly")
    boolean readOnly() default true;

    @AliasFor(annotation = Transactional.class, attribute = "timeout")
    int timeout() default -1;
}

