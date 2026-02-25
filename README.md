# Ecommerce

## ARQUITECTURA Y TECNOLOGÍAS

![Screenshot 2023-06-05 232427](https://github.com/user-attachments/assets/0ea9178a-2010-4f37-8920-f20973093bc7)

Para esta prueba se ha decidido implementar una **arquitectura de monolito modular con CQRS**. La elección de esta arquitectura se debe a varios factores:

1. **Facilidad de Evolución**: Un monolito modular permite una transición más sencilla hacia una arquitectura de microservicios en el futuro.
2. **Escalabilidad Progresiva**: Esta arquitectura permite escalar componentes específicos según sea necesario.
3. **Modularización del Código**: Facilita la gestión y el mantenimiento del código al separar las funcionalidades en módulos bien definidos.
4. **CQRS (Command Query Responsibility Segregation)**: Separación de operaciones de lectura y escritura para mejor rendimiento y escalabilidad.

### Tecnologías Utilizadas
- **Backend**: Spring Boot 3.4.1, Java 23
- **Frontend**: Next.js 15.1, React 19
- **API**: GraphQL
- **Base de Datos**: PostgreSQL 16 (con Replicación Streaming + CQRS)
- **Cache/Tokens**: Redis 7
- **CI/CD**: Jenkins
- **Contenedores**: Docker & Docker Compose

### Módulos Implementados
- **Auth**: Autenticación y gestión de usuarios
- **Catalog**: Productos y categorías
- **Orders**: Órdenes de compra

---

## REPLICACIÓN POSTGRESQL + CQRS

Este proyecto implementa un sistema completo de **PostgreSQL Streaming Replication** integrado con **CQRS**:

### Arquitectura de Replicación

```
Backend (CQRS)
├── Command Repositories → PostgreSQL Master (Write + Read)  :5432
└── Query Repositories   → PostgreSQL Replica (Read Only)    :5433
                                    ↑
                            Streaming Replication
```

### Características
- ✅ **Master-Replica**: Replicación streaming en tiempo real
- ✅ **CQRS**: Separación de escritura (Master) y lectura (Replica)
- ✅ **Hot Standby**: La replica está siempre lista para ser promovida
- ✅ **Alta Disponibilidad**: Tolerancia a fallos y escalabilidad
- ✅ **Configuración Automática**: Scripts automatizados de setup

### Inicio Rápido
```bash
# Test rápido de replicación (60 segundos)
./test-replication-quick.sh

# Verificación completa
./verify-replication.sh

# Ver documentación completa
# - REPLICATION-SUMMARY.md    - Resumen completo
# - REPLICATION-QUICKSTART.md - Comandos útiles
# - database/README.md         - Documentación técnica
```

---

## ARQUITECTURA CQRS

El sistema implementa **CQRS (Command Query Responsibility Segregation)** con **replicación PostgreSQL streaming** para garantizar sincronización automática en tiempo real.

### Separación de DataSources con Replicación Streaming

```
┌─────────────────────────────────────────────────────────────┐
│                    Backend Spring Boot                       │
│  ┌─────────────────┐           ┌─────────────────┐          │
│  │ Command Service │           │  Query Service  │          │
│  │  (Escritura)    │           │   (Lectura)     │          │
│  └────────┬────────┘           └────────┬────────┘          │
│           │                             │                    │
│           ▼                             ▼                    │
│  ┌─────────────────┐           ┌─────────────────┐          │
│  │ Write DataSource│           │ Read DataSource │          │
│  │ (puerto 5432)   │           │ (puerto 5433)   │          │
│  └────────┬────────┘           └────────┬────────┘          │
└───────────┼─────────────────────────────┼────────────────────┘
            │                             │
            ▼                             ▼
┌─────────────────────┐         ┌─────────────────────┐
│ PostgreSQL MASTER   │  WAL    │ PostgreSQL REPLICA  │
│ (Read/Write)        │─────────►│ (Read-Only)         │
│ Puerto 5432         │Streaming│ Puerto 5433         │
└─────────────────────┘         └─────────────────────┘
```

### ✨ Características de la Replicación

✅ **Sincronización Automática**: Los cambios en el master se replican en milisegundos  
✅ **Hot Standby**: La réplica permite lecturas mientras recibe actualizaciones  
✅ **Lag Mínimo**: Latencia típica < 1MB  
✅ **Sin Código Adicional**: Spring Boot maneja la separación automáticamente  
✅ **Alta Disponibilidad**: Configuración lista para failover

### DataSources Configurados

| DataSource | Puerto | Uso | Modo | Usuario |
|------------|--------|-----|------|---------|
| `writeDataSource` (Master) | 5432 | Operaciones de escritura | Read/Write | postgres |
| `readDataSource` (Replica) | 5433 | Operaciones de lectura | Read-Only | postgres |

### Scripts de Gestión de Replicación

```bash
# Reiniciar y configurar replicación desde cero
./reset-replication.sh

# Verificar estado de la replicación
./check-replication.sh

# Iniciar servicios normalmente
docker-compose up -d
```

### Verificar Sincronización

```bash
# Ver estado de replicación
./check-replication.sh

# Insertar en master y verificar en réplica
docker exec ecommerce-postgres-master psql -U postgres -d ecommerce -c "INSERT INTO ..."
docker exec ecommerce-postgres-replica psql -U postgres -d ecommerce -c "SELECT * FROM ..."
```

### 📚 Documentación Detallada

- [DATABASE_SYNC_GUIDE.md](DATABASE_SYNC_GUIDE.md) - Guía completa de sincronización
- [ecommerce-back-end/CQRS_CONFIG.md](ecommerce-back-end/CQRS_CONFIG.md) - Configuración CQRS en Spring Boot
- [database/README.md](database/README.md) - Estructura y configuración de la base de datos

---

## ESTRUCTURA DEL PROYECTO

```
ecommerce/
├── docker-compose.yml                  # Docker Compose con Master-Replica
├── reset-replication.sh                # Script para reiniciar replicación
├── check-replication.sh                # Script para verificar estado
├── DATABASE_SYNC_GUIDE.md              # Guía completa de sincronización
├── Jenkinsfile                         # Pipeline de CI/CD
│
├── database/
│   ├── README.md                       # Documentación de base de datos
│   ├── init/                           # Scripts SQL (solo en master)
│   │   ├── 00-users-and-schemas.sql   # Esquemas y ENUMs
│   │   ├── 01-auth-schema.sql         # Tablas de autenticación
│   │   ├── 02-catalog-schema.sql      # Tablas de catálogo
│   │   ├── 03-orders-schema.sql       # Tablas de órdenes
│   │   ├── 04-auth-data.sql           # Datos iniciales
│   │   ├── 05-catalog-data.sql        # Productos de ejemplo
│   │   ├── 06-views.sql               # Vistas SQL
│   │   ├── 07-orders-data.sql         # Datos de órdenes
│   │   └── 08-homepage-config.sql     # Configuración homepage
│   │
│   ├── master-config/                  # Configuración PostgreSQL Master
│   │   ├── postgresql.conf            # Config WAL y replicación
│   │   ├── pg_hba.conf                # Autenticación
│   │   └── setup-replication.sh       # Crear usuario replicator
│   │
│   └── replica-config/                 # Configuración PostgreSQL Replica
│       └── setup-replica.sh           # Configurar streaming
│
├── ecommerce-back-end/                 # Backend (Spring Boot 3.4, Java 23)
│   ├── CQRS_CONFIG.md                 # Documentación CQRS
│   └── src/main/
│       ├── resources/
│       │   └── application-local.yml  # Config DataSources CQRS
│       │
│       └── java/.../
│           ├── config/cqrs/           # Configuración CQRS
│           │   ├── DataSourceConfig.java      # Define 2 DataSources
│           │   ├── CommandJpaConfig.java      # Config Write (Master)
│           │   └── QueryJpaConfig.java        # Config Read (Replica)
│           │
│           └── modules/
│               ├── product/persisten/
│               │   ├── entity/                # Entidades compartidas
│               │   │   ├── Product.java
│               │   │   └── Category.java
│               │   └── repository/
│               │       ├── command/           # Escritura (Master)
│               │       │   ├── IProductCommandRepository.java
│               │       │   └── ICategoryCommandRepository.java
│               │       └── query/             # Lectura (Replica)
│               │           ├── IProductQueryRepository.java
│               │           └── ICategoryQueryRepository.java
│               │
│               ├── user/persisten/
│               │   ├── entity/
│               │   │   └── User.java
│               │   └── repository/
│               │       ├── command/
│               │       │   └── IUserCommandRepository.java
│               │       └── query/
│               │           └── IUserQueryRepository.java
│               │
│               └── order/persisten/
│                   ├── entity/
│                   │   ├── Order.java
│                   │   └── OrderItem.java
│                   └── repository/
│                       ├── command/
│                       │   └── IOrderCommandRepository.java
│                       └── query/
│                           └── IOrderQueryRepository.java
│
└── ecommerce-frond-end-master/         # Frontend (Next.js)
```

### Estructura CQRS de Repositorios

| Paquete | Propósito | DataSource | Base de Datos |
|---------|-----------|------------|---------------|
| `persisten/repository/command/` | Escritura (Commands) | `writeDataSource` | PostgreSQL Master (5432) |
| `persisten/repository/query/` | Lectura (Queries) | `readDataSource` | PostgreSQL Replica (5433) |

**Nota**: Las entidades están en `persisten/entity/` y son compartidas entre command y query.

### Configuración Spring Boot CQRS

| Archivo | Propósito |
|---------|-----------|
| `DataSourceConfig.java` | Define `writeDataSource` y `readDataSource` |
| `CommandJpaConfig.java` | EntityManager para escritura (puerto 5432) |
| `QueryJpaConfig.java` | EntityManager para lectura (puerto 5433) |
| `application-local.yml` | URLs de conexión a Master y Replica |

### ENUMs en Base de Datos

Los estados se mapean directamente con tipos ENUM de PostgreSQL:

| Java Enum | PostgreSQL Type |
|-----------|-----------------|
| `UserStatus` | `auth.user_status` |
| `RoleStatus` | `auth.role_status` |
| `CategoryStatus` | `catalog.category_status` |
| `ProductStatus` | `catalog.product_status` |
| `OrderStatus` | `orders.order_status` |
| `PaymentMethod` | `orders.payment_method` |

---

## DOCKER COMPOSE

El proyecto usa múltiples archivos de Docker Compose para diferentes perfiles:

| Archivo | Descripción |
|---------|-------------|
| `docker-compose.yml` | Base (PostgreSQL + Redis) - **Default** |
| `docker-compose.dev.yml` | Herramientas de desarrollo (Adminer, Redis Commander) |
| `docker-compose.prod.yml` | Backend + Frontend |
| `docker-compose.ci.yml` | Jenkins CI/CD |
| `docker-compose.monitoring.yml` | Zipkin (tracing) |

### Comandos Rápidos

```bash
# Solo infraestructura (PostgreSQL + Redis)
docker compose up -d

# Con herramientas de desarrollo
docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d

# Stack completo de producción
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Todos los servicios
docker compose -f docker-compose.yml -f docker-compose.dev.yml -f docker-compose.prod.yml -f docker-compose.ci.yml -f docker-compose.monitoring.yml up -d

# Detener servicios
docker compose down
```

### Script de Ayuda

También puedes usar el script `docker.sh`:

```bash
./docker.sh up                    # Solo infraestructura (local)
./docker.sh up --dev              # Con herramientas de desarrollo
./docker.sh up --prod             # Con backend y frontend
./docker.sh up --all              # Todos los servicios
./docker.sh down                  # Detener servicios
./docker.sh logs backend          # Ver logs del backend
```

### Puertos de Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| PostgreSQL Master | 5432 | Base de datos (escritura) |
| PostgreSQL Replica | 5433 | Base de datos (lectura) |
| Redis | 6379 | Cache/Tokens |
| Backend | 8080 | API Spring Boot |
| Frontend | 3000 | Next.js |
| Jenkins | 8081 | CI/CD |
| Redis Commander | 8082 | UI de Redis |
| Adminer | 8083 | UI de PostgreSQL |
| Zipkin | 9411 | Distributed Tracing |

---

## CREDENCIALES

### Base de Datos
| Usuario | Password | Uso |
|---------|----------|-----|
| postgres | postgres | Admin / CQRS (Master & Replica) |
| replicator | replicator_password | Replicación streaming |

### Redis
- **Password**: redis_secure_password_123

### Usuario de Prueba
- **Email**: admin@ecommerce.com
- **Password**: admin123
