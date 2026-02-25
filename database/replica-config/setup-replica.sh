#!/bin/bash
set -e

echo "=========================================="
echo "Configurando PostgreSQL Replica"
echo "=========================================="

# Definir PGDATA si no está definido
export PGDATA=${PGDATA:-/var/lib/postgresql/data/pgdata}

# Esperar a que el master esté listo
echo "Esperando a que el master esté disponible..."
until PGPASSWORD=postgres pg_isready -h postgres-master -p 5432 -U postgres; do
  echo "Master no está listo - esperando..."
  sleep 2
done

echo "Master está listo. Iniciando configuración de réplica..."

# Limpiar directorio de datos si existe
if [ -d "$PGDATA" ] && [ "$(ls -A $PGDATA 2>/dev/null)" ]; then
    echo "Limpiando directorio de datos existente..."
    rm -rf "$PGDATA"/*
fi

# Crear directorio si no existe
mkdir -p "$PGDATA"

# Crear backup base desde el master
echo "Creando backup base desde el master..."
PGPASSWORD=replicator_password pg_basebackup \
    -h postgres-master \
    -p 5432 \
    -U replicator \
    -D "$PGDATA" \
    -Fp \
    -Xs \
    -P \
    -R

echo "Backup base completado."

# Configurar postgresql.conf para la réplica
cat >> "$PGDATA/postgresql.conf" <<EOF

# Configuración de Réplica
hot_standby = on
hot_standby_feedback = on
max_standby_streaming_delay = 30s
wal_receiver_status_interval = 10s
max_connections = 100
shared_buffers = 128MB
EOF

# Configurar pg_hba.conf para permitir conexiones
cat >> "$PGDATA/pg_hba.conf" <<EOF
# Permitir conexiones desde cualquier host
host all all all md5
EOF

echo "Configuración de réplica completada."

# Los permisos son manejados automáticamente por el entrypoint de PostgreSQL
# No necesitamos hacer chmod/chown aquí

echo "=========================================="
echo "PostgreSQL Replica configurada correctamente"
echo "=========================================="

