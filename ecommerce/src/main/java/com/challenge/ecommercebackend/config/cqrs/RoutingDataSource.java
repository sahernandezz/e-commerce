package com.challenge.ecommercebackend.config.cqrs;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DataSource que enruta las conexiones basado en el contexto actual
 * Permite cambiar entre DataSource de lectura y escritura dinámicamente
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}

