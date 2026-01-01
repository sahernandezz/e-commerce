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
- **Base de Datos**: PostgreSQL 16 (con CQRS)
- **Cache/Tokens**: Redis 7
- **CI/CD**: Jenkins
- **Contenedores**: Docker & Docker Compose

### Módulos Implementados
- **Auth**: Autenticación y gestión de usuarios
- **Catalog**: Productos y categorías
- **Orders**: Órdenes de compra

---

## ARQUITECTURA CQRS

El sistema implementa **CQRS (Command Query Responsibility Segregation)** con las siguientes características:

### Separación de DataSources

```
┌─────────────────────────────────────────────────────────────┐
│                        Backend                               │
│  ┌─────────────────┐           ┌─────────────────┐          │
│  │  Write Service  │           │  Read Service   │          │
│  │  (Commands)     │           │  (Queries)      │          │
│  └────────┬────────┘           └────────┬────────┘          │
│           │                             │                    │
│           ▼                             ▼                    │
│  ┌─────────────────┐           ┌─────────────────┐          │
│  │ Write DataSource│           │ Read DataSource │          │
│  │ (ecommerce_     │           │ (ecommerce_     │          │
│  │  writer)        │           │  reader)        │          │
│  └────────┬────────┘           └────────┬────────┘          │
└───────────┼─────────────────────────────┼────────────────────┘
            │                             │
            ▼                             ▼
┌─────────────────────┐         ┌─────────────────────┐
│   PostgreSQL Master │         │ Vistas Materializadas│
│   (Escritura)       │◄───────►│ (Lectura optimizada) │
└─────────────────────┘         └─────────────────────┘
```

### Usuarios de Base de Datos

| Usuario | Propósito | Permisos |
|---------|-----------|----------|
| `ecommerce_writer` | Operaciones de escritura | INSERT, UPDATE, DELETE |
| `ecommerce_reader` | Operaciones de lectura | SELECT (solo vistas materializadas) |

### Vistas Materializadas para Soft Delete

Las operaciones de lectura utilizan vistas materializadas que filtran automáticamente los registros eliminados (soft delete):

- `auth.customer_view` - Usuarios activos
- `auth.role_view` - Roles activos
- `catalog.category_view` - Categorías activas
- `catalog.product_view` - Productos activos
- `orders.orders_view` - Órdenes no canceladas

### Almacenamiento de Tokens en Redis

Los tokens JWT se almacenan en **Redis** en lugar de PostgreSQL para:
- Mayor velocidad de validación
- TTL automático (expiración automática)
- Menor carga en la base de datos
- Escalabilidad horizontal

---

## ESTRUCTURA DEL PROYECTO

```
ecommerce/
├── docker-compose.yml              # Docker Compose general
├── Jenkinsfile                     # Pipeline de CI/CD
├── database/
│   └── init/                       # Scripts SQL de inicialización
│       ├── 00-users-and-schemas.sql # Usuarios CQRS, esquemas y ENUMs
│       ├── 01-auth-schema.sql      # Tablas de autenticación
│       ├── 02-catalog-schema.sql   # Tablas de catálogo
│       ├── 03-orders-schema.sql    # Tablas de órdenes
│       ├── 04-auth-data.sql        # Datos iniciales
│       ├── 05-catalog-data.sql     # Productos de ejemplo
│       └── 06-materialized-views.sql # Vistas materializadas
├── ecommerce-back-end-master/      # Backend (Spring Boot 3.4, Java 21)
│   └── src/main/java/.../
│       ├── config/
│       │   ├── cqrs/               # Configuración CQRS
│       │   │   ├── DataSourceConfig.java
│       │   │   ├── DataSourceContextHolder.java
│       │   │   ├── DataSourceRoutingAspect.java
│       │   │   ├── DataSourceType.java
│       │   │   ├── ReadOnly.java
│       │   │   └── RoutingDataSource.java
│       │   └── redis/              # Configuración Redis
│       └── modules/
│           ├── product/persisten/
│           │   ├── entity/         # Entidades JPA
│           │   └── repository/
│           │       ├── command/    # Repositorios de ESCRITURA
│           │       └── query/      # Repositorios de LECTURA
│           ├── user/persisten/
│           │   ├── entity/
│           │   └── repository/
│           │       ├── command/
│           │       └── query/
│           └── order/persisten/
│               ├── entity/
│               └── repository/
│                   ├── command/
│                   └── query/
└── ecommerce-frond-end-master/     # Frontend (Next.js)
```

### Estructura CQRS de Repositorios

| Paquete | Propósito | DataSource | Tabla/Vista |
|---------|-----------|------------|-------------|
| `persisten/repository/command/` | Escritura (Commands) | `ecommerce_writer` | Tablas directas |
| `persisten/repository/query/` | Lectura (Queries) | `ecommerce_reader` | Vistas materializadas |

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
| PostgreSQL | 5432 | Base de datos |
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
| postgres | postgres | Admin |
| ecommerce_writer | writer_secure_password_123 | Escritura (CQRS) |
| ecommerce_reader | reader_secure_password_123 | Lectura (CQRS) |

### Redis
- **Password**: redis_secure_password_123

### Usuario de Prueba
- **Email**: admin@ecommerce.com
- **Password**: admin123
