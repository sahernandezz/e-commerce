FROM postgres:16

# Copiar el script de setup de réplica al contenedor
COPY setup-replica.sh /docker-entrypoint-initdb.d/setup-replica.sh

# Asegurar que el script sea ejecutable
RUN chmod +x /docker-entrypoint-initdb.d/setup-replica.sh

# La imagen base de postgres ejecutará el script automáticamente
