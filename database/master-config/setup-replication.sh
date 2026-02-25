#!/bin/bash
set -e

echo "=========================================="
echo "Configurando PostgreSQL Master para Replicación"
echo "=========================================="

# Crear usuario de replicación
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Crear usuario de replicación
    DO \$\$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'replicator') THEN
            CREATE ROLE replicator WITH REPLICATION PASSWORD 'replicator_password' LOGIN;
        END IF;
    END
    \$\$;

    -- Otorgar permisos necesarios
    GRANT CONNECT ON DATABASE ecommerce TO replicator;
EOSQL

# Configurar pg_hba.conf para permitir conexiones de réplica
echo "host replication replicator all md5" >> "$PGDATA/pg_hba.conf"
echo "host all all all md5" >> "$PGDATA/pg_hba.conf"

# Recargar configuración
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    SELECT pg_reload_conf();
EOSQL

echo "=========================================="
echo "PostgreSQL Master configurado correctamente"
echo "=========================================="

