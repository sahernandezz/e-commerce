package com.challenge.ecommercebackend.config.cqrs;

/**
 * Holder para el contexto del DataSource actual
 * Usa ThreadLocal para mantener el tipo de DataSource por hilo
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

    /**
     * Establece el tipo de DataSource para el hilo actual
     */
    public static void setDataSourceType(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    /**
     * Obtiene el tipo de DataSource del hilo actual
     * Por defecto retorna WRITE si no está establecido
     */
    public static DataSourceType getDataSourceType() {
        DataSourceType type = contextHolder.get();
        return type != null ? type : DataSourceType.WRITE;
    }

    /**
     * Limpia el contexto del hilo actual
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    /**
     * Establece el DataSource de escritura
     */
    public static void setWriteDataSource() {
        setDataSourceType(DataSourceType.WRITE);
    }

    /**
     * Establece el DataSource de lectura
     */
    public static void setReadDataSource() {
        setDataSourceType(DataSourceType.READ);
    }
}

