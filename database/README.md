# Sistema de Replicación PostgreSQL - Configuración CQRS

## Descripción

Este directorio contiene la configuración para implementar un sistema de replicación streaming de PostgreSQL, diseñado para trabajar con la arquitectura CQRS del backend.

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    Backend (Spring Boot)                 │
│                                                          │
│  ┌──────────────────┐        ┌──────────────────┐      │
│  │ Command Repos    │        │  Query Repos     │      │
│  │ (Escritura)      │        │  (Lectura)       │      │
│  └────────┬─────────┘        └────────┬─────────┘      │
│           │                           │                 │
└───────────┼───────────────────────────┼─────────────────┘
            │                           │
            ▼                           ▼
  ┌──────────────────┐        ┌──────────────────┐
  │ PostgreSQL       │        │ PostgreSQL       │
  │ MASTER           │───────▶│ REPLICA          │
  │ (puerto 5432)    │ Stream │ (puerto 5433)    │
  │                  │ Replic │                  │
  │ - Escritura      │        │ - Solo Lectura   │
  │ - Lectura        │        │ - Hot Standby    │
  └──────────────────┘        └──────────────────┘
```

## Componentes

### 1. PostgreSQL Master (postgres-master)
- **Puerto**: 5432
- **Rol**: Servidor principal que maneja todas las operaciones de escritura
- **Configuración**:
  - WAL Level: replica
  - Max WAL Senders: 10
  - Max Replication Slots: 10
  - Hot Standby: on
  - WAL Keep Size: 64MB

### 2. PostgreSQL Replica (postgres-replica)
- **Puerto**: 5433
- **Rol**: Servidor de solo lectura (Hot Standby)
- **Configuración**:
  - Hot Standby: on
  - Hot Standby Feedback: on
  - Replicación Streaming en tiempo real

### 3. Usuario de Replicación
- **Usuario**: replicator
- **Password**: replicator_password
- **Permisos**: REPLICATION

## Archivos de Configuración

### master-config/setup-replication.sh
Script que se ejecuta al iniciar el contenedor master:
- Crea el usuario de replicación `replicator`
- Configura `pg_hba.conf` para permitir conexiones de replicación
- Otorga permisos necesarios

### replica-config/setup-replica.sh
Script que se ejecuta al iniciar el contenedor replica:
- Espera a que el master esté disponible
- Ejecuta `pg_basebackup` para copiar datos del master
- Configura el servidor como Hot Standby
- Establece la conexión de streaming con el master

## Flujo de Replicación

1. **Inicio del Master**:
   - PostgreSQL inicia con configuración de replicación
   - Se ejecuta `setup-replication.sh`
   - Se crea el usuario `replicator`
   - Se configuran permisos en `pg_hba.conf`

2. **Inicio de la Replica**:
   - Espera a que el master esté healthy
   - Ejecuta `pg_basebackup` para copiar datos iniciales
   - Configura `postgresql.auto.conf` con parámetros de replica
   - Inicia en modo Hot Standby
   - Establece conexión streaming con el master

3. **Replicación Continua**:
   - Todas las escrituras en el master se replican automáticamente
   - La replica está en modo de solo lectura
   - Lag de replicación típicamente < 1 segundo

## Uso con el Backend

El backend está configurado con CQRS para utilizar ambas bases de datos:

### Escritura (Command)
```yaml
spring.datasource.write:
  url: jdbc:postgresql://postgres-master:5432/ecommerce
```

### Lectura (Query)
```yaml
spring.datasource.read:
  url: jdbc:postgresql://postgres-replica:5432/ecommerce
```

## Comandos Útiles

### Verificar estado de replicación en el Master
```bash
docker exec -it ecommerce-postgres-master psql -U postgres -c "SELECT * FROM pg_stat_replication;"
```

### Verificar estado de replicación en la Replica
```bash
docker exec -it ecommerce-postgres-replica psql -U postgres -c "SELECT * FROM pg_stat_wal_receiver;"
```

### Ver lag de replicación
```bash
docker exec -it ecommerce-postgres-master psql -U postgres -c "SELECT client_addr, state, sent_lsn, write_lsn, flush_lsn, replay_lsn, sync_state FROM pg_stat_replication;"
```

### Verificar que la replica está en modo standby
```bash
docker exec -it ecommerce-postgres-replica psql -U postgres -c "SELECT pg_is_in_recovery();"
```
Resultado esperado: `t` (true)

### Ver logs del Master
```bash
docker logs ecommerce-postgres-master -f
```

### Ver logs de la Replica
```bash
docker logs ecommerce-postgres-replica -f
```

## Solución de Problemas

### La replica no puede conectarse al master
1. Verificar que el master está healthy:
   ```bash
   docker ps
   ```
2. Verificar conectividad de red:
   ```bash
   docker exec -it ecommerce-postgres-replica ping postgres-master
   ```

### La replica no está replicando
1. Verificar slots de replicación en el master:
   ```bash
   docker exec -it ecommerce-postgres-master psql -U postgres -c "SELECT * FROM pg_replication_slots;"
   ```

2. Verificar logs de la replica:
   ```bash
   docker logs ecommerce-postgres-replica --tail 50
   ```

### Resetear la replicación
```bash
# Detener servicios
docker-compose down

# Eliminar volúmenes
docker volume rm ecommerce_postgres_replica_data

# Reiniciar
docker-compose up -d postgres-master postgres-replica
```

## Ventajas de esta Implementación

1. **Escalabilidad de Lecturas**: Las consultas se distribuyen en la replica
2. **Alta Disponibilidad**: La replica puede promovida a master en caso de fallo
3. **Reducción de Carga**: El master se libera de consultas pesadas de lectura
4. **CQRS Natural**: Separación física de Command y Query
5. **Hot Standby**: La replica está siempre lista para ser promovida

## Monitoreo

Se recomienda monitorear:
- Lag de replicación (debe ser < 1 segundo en condiciones normales)
- Uso de WAL en el master
- Estado de conexión de la replica
- Rendimiento de consultas en ambos servidores

## Backup y Recuperación

La replica también sirve como backup en tiempo real. Para crear backups adicionales:

```bash
# Backup desde la replica (no afecta al master)
docker exec -it ecommerce-postgres-replica pg_dump -U postgres ecommerce > backup.sql
```

## Próximos Pasos (Opcional)

1. **Múltiples Replicas**: Agregar más replicas para mayor escalabilidad
2. **Failover Automático**: Implementar herramientas como Patroni o repmgr
3. **Load Balancer**: Distribuir lecturas entre múltiples replicas
4. **Monitoreo**: Integrar con Prometheus/Grafana

