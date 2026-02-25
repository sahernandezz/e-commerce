package com.challenge.ecommercebackend.config.cqrs;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración del EntityManagerFactoryBuilder
 * Necesario para construir los EntityManagerFactory de Command y Query
 */
@Configuration
public class EntityManagerFactoryBuilderConfig {

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return new EntityManagerFactoryBuilder(vendorAdapter, properties, null);
    }
}

