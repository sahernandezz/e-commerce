# E-commerce

[![License: CC BY-NC 4.0](https://img.shields.io/badge/License-CC%20BY--NC%204.0-lightgrey.svg)](./LICENSE)

Plataforma de comercio electrónico full-stack con arquitectura de **monolito modular + CQRS**, replicación PostgreSQL streaming, autenticación JWT, panel de administración y API GraphQL.

---

## ARQUITECTURA DEL SISTEMA (C4 — Contenedores)

```
┌──────────┐       ┌──────────┐
│  Cliente │       │  Admin   │
└────┬─────┘       └────┬─────┘
     │    HTTPS         │ HTTPS
     └────────┬─────────┘
              ▼
┌──────────────────────────────────────────────────────────────────┐
│                        Frontend (Next.js)                        │
│             React 19 · TypeScript · Tailwind CSS                │
│                       Puerto 3000                                │
└──────────────────────────┬───────────────────────────────────────┘
                           │ GraphQL / HTTP
                           ▼
┌──────────────────────────────────────────────────────────────────┐
│                    Backend API (Spring Boot)                      │
│          Java 21 · GraphQL · JWT · CQRS · Spring Security        │
│                       Puerto 8080                                │
│  ┌────────────┐ ┌────────────┐ ┌──────────┐ ┌─────────────┐     │
│  │  Product   │ │   Order    │ │   User   │ │    Admin    │     │
│  │  Module    │ │   Module   │ │  Module  │ │   Module    │     │
│  └────────────┘ └────────────┘ └──────────┘ └─────────────┘     │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │   Transversal: JWT · Email · CQRS DataSources · AOP    │     │
│  └─────────────────────────────────────────────────────────┘     │
└──────────┬──────────────────────────────────┬────────────────────┘
           │ JDBC (Write)                     │ JDBC (Read)
           ▼                                  ▼
┌─────────────────────┐  WAL Streaming  ┌─────────────────────┐
│ PostgreSQL MASTER   │────────────────►│ PostgreSQL REPLICA  │
│ Read/Write · 5432   │                 │ Read-Only · 5433    │
│ Schemas: auth,      │                 │ Hot Standby         │
│ catalog, orders     │                 │                     │
└─────────────────────┘                 └─────────────────────┘

         ┌──────────────┐
         │   Jenkins    │  CI/CD Pipeline
         │  Puerto 8081 │  Build → Test → Deploy
         └──────────────┘
```

---

## TECNOLOGÍAS

### Backend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.4.1 | Framework backend |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia (Hibernate) |
| Spring GraphQL | 1.x | API GraphQL |
| Spring Mail + Thymeleaf | 3.x | Envío de correos con templates |
| Spring AOP | 3.x | Programación orientada a aspectos |
| Spring Actuator | 3.x | Monitoreo y health checks |
| JWT (jjwt) | 0.12.6 | Tokens de autenticación |
| ModelMapper | 3.2.1 | Mapeo de objetos DTO ↔ Entity |
| Lombok | Latest | Reducción de boilerplate |
| SpringDoc OpenAPI | 2.3.0 | Documentación Swagger |
| Gradle | 8.8 | Build tool |
| JUnit 5 + Mockito | Latest | Testing unitario backend |

### Frontend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Next.js | 16.1.1 | Framework React (App Router + Turbopack) |
| React | 19.x | UI library |
| TypeScript | 5.7.x | Tipado estático |
| Tailwind CSS | 3.4.19 | Estilos utilitarios |
| GraphQL Request | 7.1.2 | Cliente GraphQL |
| Headless UI | 2.2.x | Componentes accesibles |
| Heroicons | 2.2.x | Iconos |
| Radix UI | 1.1.x | Componentes primitivos (Tabs) |
| Lucide React | 0.469.x | Iconos adicionales |
| next-themes | 0.4.x | Soporte dark mode |
| Vitest | 4.1.x | Testing unitario frontend |
| Testing Library | 16.3.x | Testing de componentes React |
| pnpm | 9.15.1 | Package manager |

### Infraestructura
| Tecnología | Versión | Uso |
|------------|---------|-----|
| PostgreSQL | 16 | Base de datos (Master + Replica) |
| Docker & Docker Compose | Latest | Contenedores y orquestación |
| Jenkins | LTS | CI/CD |

---

## MÓDULOS

### Backend (Spring Boot)

El backend sigue una **arquitectura modular con CQRS y Use Cases**, separando claramente las responsabilidades:

```
modules/
├── product/          # Catálogo de productos y categorías
├── order/            # Gestión de órdenes de compra
├── user/             # Autenticación, registro y gestión de usuarios
└── admin/            # Dashboard y estadísticas de administración
```

#### Módulo Product (Catálogo)
| Use Case | Descripción |
|----------|-------------|
| `GetProductsUseCase` | Listar productos activos, por categoría, por nombre, por ID y paginados |
| `CreateProductUseCase` | Crear producto con categoría, imágenes, colores y tallas |
| `UpdateProductUseCase` | Actualizar datos de un producto |
| `UpdateProductStatusUseCase` | Cambiar estado ACTIVE/INACTIVE |
| `DeleteProductUseCase` | Eliminar producto |
| `GetCategoriesUseCase` | Listar categorías activas y paginadas |
| `CreateCategoryUseCase` | Crear categoría |
| `UpdateCategoryUseCase` | Actualizar categoría |
| `UpdateCategoryStatusUseCase` | Cambiar estado de categoría |
| `DeleteCategoryUseCase` | Eliminar categoría |

#### Módulo Order (Órdenes)
| Use Case | Descripción |
|----------|-------------|
| `CreateOrderUseCase` | Crear orden con productos, cálculo de total y envío de email |
| `GetOrdersByCustomerUseCase` | Obtener órdenes del cliente autenticado |
| `GetOrderByIdAndCustomerUseCase` | Obtener orden específica del cliente |
| `CancelOrderByCustomerUseCase` | Cancelar orden por el cliente |
| `GetOrdersUseCase` | Listar todas las órdenes (admin, paginado) |
| `UpdateOrderStatusUseCase` | Actualizar estado de orden (admin) |

#### Módulo User (Usuarios)
| Use Case | Descripción |
|----------|-------------|
| `CreateUserUseCase` | Registro con encriptación de contraseña |
| `GetUserProfileUseCase` | Obtener perfil del usuario autenticado |
| `UpdateUserProfileUseCase` | Actualizar perfil |
| `ChangePasswordUseCase` | Cambiar contraseña |
| `GetUsersUseCase` | Listar usuarios (admin, paginado) |
| `UpdateUserStatusUseCase` | Cambiar estado ACTIVE/INACTIVE/SUSPENDED (admin) |
| `ActivateUserUseCase` | Activar usuario |
| `DeleteUserUseCase` | Eliminar usuario |

**Autenticación**: Login y registro vía JWT con `IAuthenticationService`, recuperación de contraseña por email.

#### Módulo Admin (Dashboard)
| Use Case | Descripción |
|----------|-------------|
| `GetDashboardStatsUseCase` | Estadísticas: totales, ingresos, órdenes pendientes, usuarios nuevos |
| `GetTopSellingProductsUseCase` | Productos más vendidos con ingresos |
| `GetRecentOrdersUseCase` | Órdenes recientes |

#### Componentes Transversales
| Componente | Descripción |
|------------|-------------|
| `JwtUtils` + `JwtAuthenticationFilter` | Generación y validación de tokens JWT |
| `IEmailSenderService` | Envío de correos (confirmación, recuperación) |
| `IProductExternalApi` | Comunicación desacoplada entre módulos (Product ↔ Order) |
| `PageResponse<T>` | Respuesta paginada genérica para GraphQL |
| `HomepageConfig` | Configuración dinámica de la página principal |
| CQRS DataSources | Separación de escritura (Master) y lectura (Replica) |

### Frontend (Next.js)

```
app/
├── page.tsx                # Página principal (productos destacados, carrusel, banner)
├── search/                 # Búsqueda de productos
│   ├── page.tsx            # Todos los productos
│   └── [handle]/           # Productos por categoría
├── product/[handle]/       # Detalle de producto
├── pay/                    # Proceso de pago
│   ├── page.tsx            # Checkout
│   ├── success/            # Pago exitoso
│   └── error/              # Error en pago
├── my-orders/              # Mis órdenes (usuario autenticado)
├── profile/                # Perfil del usuario
└── admin/                  # Panel de administración
    ├── page.tsx             # Dashboard con estadísticas
    ├── products/            # CRUD de productos
    ├── categories/          # CRUD de categorías
    ├── orders/              # Gestión de órdenes
    ├── users/               # Gestión de usuarios
    └── homepage/            # Configuración de la página principal
```

| Componente | Descripción |
|------------|-------------|
| `Auth.tsx` | Protección de rutas autenticadas |
| `LoginModal.tsx` | Modal de login/registro |
| `ConfirmModal.tsx` | Modal de confirmación de acciones |
| `navbar/` | Navegación principal con búsqueda |
| `drawer/` | Carrito lateral (drawer) |
| `card/` | Tarjeta de producto |
| `footer.tsx` | Footer del sitio |

| Context | Descripción |
|---------|-------------|
| `auth.tsx` | Estado de autenticación (usuario, token, login, logout) |
| `cart.tsx` | Carrito de compras (localStorage, cantidades, total) |
| `drawer.tsx` | Estado del drawer lateral |
| `login-modal.tsx` | Estado del modal de login |

### API GraphQL

| Tipo | Operaciones |
|------|-------------|
| **Queries públicas** | `getAllProductsActive`, `getProductById`, `getAllProductsActiveByName`, `getAllProductsActiveByCategoryId`, `getAllCategoriesActive`, `getPaymentMethods`, `getHomepageConfig` |
| **Queries autenticadas** | `getMyOrders` |
| **Queries admin** | `getDashboardStats`, `getProductsPaginated`, `getCategoriesPaginated`, `getOrdersPaginated`, `getUsersPaginated`, `getTopSellingProducts`, `getRecentOrders`, `get*Statuses` |
| **Mutations** | `createOrder`, `login`, `register` |
| **Mutations admin** | `addProduct`, `updateProduct`, `deleteProduct`, `updateProductStatus`, `createCategory`, `updateCategory`, `deleteCategory`, `updateCategoryStatus`, `updateOrderStatus`, `updateUserStatus`, `updateHomepageConfig` |

### ENUMs del Sistema

Los estados se mapean directamente con tipos ENUM de PostgreSQL:

| Java Enum | PostgreSQL Type | Valores |
|-----------|-----------------|---------|
| `ProductStatus` | `catalog.product_status` | ACTIVE, INACTIVE |
| `CategoryStatus` | `catalog.category_status` | ACTIVE, INACTIVE |
| `OrderStatus` | `orders.order_status` | PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELED |
| `PaymentMethod` | `orders.payment_method` | CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH, TRANSFER |
| `UserStatus` | `auth.user_status` | ACTIVE, INACTIVE, SUSPENDED |
| `RoleStatus` | `auth.role_status` | ACTIVE, INACTIVE |
| `RoleName` | — | ADMIN (Administrador), USER (Cliente) |

---

## TESTS

### Frontend — 70 tests (Vitest + Testing Library)

```bash
cd ecommerce-frond-end-master
pnpm install
pnpm test          # Ejecutar tests
pnpm test:watch    # Modo watch
```

| Archivo | Tests | Cobertura |
|---------|-------|-----------|
| `tests/lib/currencyFormatter.test.ts` | 5 | Formateo de moneda COP |
| `tests/lib/roles.test.ts` | 24 | Enum RoleName, ROLES map, helpers |
| `tests/lib/status.test.ts` | 23 | Status maps, labels, arrays selectores |
| `tests/lib/utils.test.ts` | 7 | Utilidad `cn()` (Tailwind merge) |
| `tests/context/cart.test.tsx` | 11 | Hook useCart (add, remove, quantity, total, clear) |

### Backend — 55 tests (JUnit 5 + Mockito)

```bash
cd ecommerce-back-end
./gradlew test
```

| Archivo | Tests | Cobertura |
|---------|-------|-----------|
| `CreateProductUseCaseImplTest` | 3 | Crear producto, categoría no encontrada, estado ACTIVE |
| `GetProductsUseCaseImplTest` | 9 | Obtener activos, por categoría/nombre/ID, paginación con filtros |
| `UpdateProductStatusUseCaseImplTest` | 3 | Actualizar estado, producto no encontrado, estado inválido |
| `CreateOrderUseCaseImplTest` | 4 | Crear orden, error, cálculo total, normalización email |
| `CreateUserUseCaseImplTest` | 4 | Crear usuario, email duplicado, rol no encontrado, encriptación |
| `ProductStatusTest` | 5 | Enum ProductStatus (valores, displayName, valueOf) |
| `CategoryStatusTest` | 4 | Enum CategoryStatus |
| `OrderStatusTest` | 4 | Enum OrderStatus (6 valores) |
| `PaymentMethodTest` | 4 | Enum PaymentMethod (5 valores) |
| `RoleNameTest` | 10 | fromCode, isValid, isAdmin, isUser, case-insensitive |
| `UserStatusTest` | 4 | Enum UserStatus (3 valores) |

---


## ARQUITECTURA CQRS + REPLICACIÓN

El sistema implementa **CQRS (Command Query Responsibility Segregation)** con **replicación PostgreSQL streaming** para sincronización automática en tiempo real.

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

### Características
- ✅ **Master-Replica**: Replicación streaming en tiempo real
- ✅ **CQRS**: Separación de escritura (Master) y lectura (Replica)
- ✅ **Hot Standby**: La replica acepta lecturas mientras recibe WAL
- ✅ **Alta Disponibilidad**: Tolerancia a fallos y escalabilidad
- ✅ **Lag Mínimo**: Latencia de replicación < 1MB
- ✅ **Configuración Automática**: Scripts automatizados de setup

### Repositorios CQRS

| Paquete | Propósito | DataSource | Base de Datos |
|---------|-----------|------------|---------------|
| `persisten/repository/command/` | Escritura (Commands) | `writeDataSource` | PostgreSQL Master (5432) |
| `persisten/repository/query/` | Lectura (Queries) | `readDataSource` | PostgreSQL Replica (5433) |

### Configuración Spring Boot CQRS

| Archivo | Propósito |
|---------|-----------|
| `DataSourceConfig.java` | Define `writeDataSource` y `readDataSource` |
| `CommandJpaConfig.java` | EntityManager para escritura (Master, puerto 5432) |
| `QueryJpaConfig.java` | EntityManager para lectura (Replica, puerto 5433) |
| `application-local.yml` | URLs de conexión a Master y Replica |

### Scripts de Replicación

```bash
# Test rápido de replicación
./test-replication-quick.sh

# Verificación completa
./verify-replication.sh

# Reiniciar replicación desde cero
./reset-replication.sh

# Verificar estado
./check-replication.sh
```

---

## ESTRUCTURA DEL PROYECTO

```
ecommerce/
├── LICENSE                                 # Licencia MIT
├── README.md                               # Esta documentación
├── docker-compose.yml                      # Docker Compose (PostgreSQL Master/Replica + Backend + Frontend + Jenkins)
├── Jenkinsfile                             # Pipeline CI/CD
│
├── database/
│   ├── init/                               # Scripts SQL de inicialización
│   │   ├── 00-users-and-schemas.sql       # Esquemas, ENUMs y usuarios
│   │   ├── 01-auth-schema.sql             # Tablas: customer, role
│   │   ├── 02-catalog-schema.sql          # Tablas: product, category
│   │   ├── 03-orders-schema.sql           # Tablas: orders, order_product
│   │   ├── 04-auth-data.sql               # Roles y usuario admin
│   │   ├── 05-catalog-data.sql            # Productos y categorías de ejemplo
│   │   ├── 06-views.sql                   # Vistas SQL
│   │   ├── 07-orders-data.sql             # Órdenes de ejemplo
│   │   └── 08-homepage-config.sql         # Configuración homepage
│   ├── master-config/                      # Config PostgreSQL Master (WAL, replicación)
│   └── replica-config/                     # Config PostgreSQL Replica (streaming)
│
├── ecommerce-back-end/                     # Backend — Spring Boot 3.4.1, Java 21
│   ├── build.gradle                        # Dependencias y plugins
│   ├── Dockerfile
│   └── src/
│       ├── main/
│       │   ├── resources/
│       │   │   ├── application.yml         # Config general
│       │   │   ├── application-local.yml   # Config CQRS DataSources
│       │   │   ├── application-docker.yml  # Config Docker
│       │   │   ├── graphql/schema.graphqls # Schema GraphQL completo
│       │   │   └── templates/mail/         # Templates de correos (Thymeleaf)
│       │   └── java/.../
│       │       ├── config/
│       │       │   ├── cqrs/               # DataSourceConfig, CommandJpaConfig, QueryJpaConfig
│       │       │   ├── security/           # SecurityConfig, filtros
│       │       │   ├── graphql/            # Configuración GraphQL
│       │       │   └── email/              # Config de email
│       │       ├── jwt/                    # JwtUtils, JwtAuthenticationFilter
│       │       ├── email/                  # IEmailSenderService
│       │       ├── api/                    # IProductExternalApi (comunicación entre módulos)
│       │       ├── shared/domain/          # PageResponse, PaymentMethod, UserStatus
│       │       └── modules/
│       │           ├── product/            # Catálogo (Product, Category)
│       │           │   ├── domain/service/ # IProductService
│       │           │   ├── domain/usecase/ # 10 use cases
│       │           │   ├── persisten/      # Entidades + Repos (command/ + query/)
│       │           │   └── web/            # Controllers + DTOs GraphQL
│       │           ├── order/              # Órdenes
│       │           │   ├── domain/service/ # IOrderService
│       │           │   ├── domain/usecase/ # 7 use cases
│       │           │   ├── persisten/      # Order, ProductOrder + Repos
│       │           │   └── web/            # Controllers + DTOs
│       │           ├── user/               # Usuarios y Auth
│       │           │   ├── domain/service/ # IUserService, IAuthenticationService
│       │           │   ├── domain/usecase/ # 9 use cases
│       │           │   ├── persisten/      # User, Role, RoleName + Repos
│       │           │   └── web/            # Controllers + DTOs
│       │           └── admin/              # Dashboard
│       │               ├── domain/usecase/ # 3 use cases (stats, top selling, recent)
│       │               └── web/            # Controllers + DTOs
│       └── test/                           # 55 tests unitarios (JUnit 5 + Mockito)
│
└── ecommerce-frond-end-master/             # Frontend — Next.js 16.1.1, React 19
    ├── package.json
    ├── vitest.config.ts                    # Configuración Vitest
    ├── Dockerfile
    ├── app/                                # Pages (App Router)
    │   ├── page.tsx                        # Home (productos destacados, carrusel)
    │   ├── search/                         # Búsqueda y filtros por categoría
    │   ├── product/[handle]/              # Detalle de producto
    │   ├── pay/                           # Checkout, success, error
    │   ├── my-orders/                     # Órdenes del usuario
    │   ├── profile/                       # Perfil
    │   └── admin/                         # Panel admin (dashboard, CRUD, homepage config)
    ├── components/                         # Componentes reutilizables
    ├── context/                            # React Context (auth, cart, drawer, login-modal)
    ├── lib/                                # Utilidades y cliente GraphQL
    │   ├── graphql/query.ts               # Queries GraphQL
    │   ├── graphql/mutation.ts            # Mutations GraphQL
    │   ├── graphql/admin.ts               # Queries/Mutations admin
    │   ├── currencyFormatter.ts           # Formateo COP
    │   ├── roles.ts                       # RoleName enum y helpers
    │   ├── status.ts                      # Status maps y labels
    │   ├── types.ts                       # Tipos TypeScript
    │   └── utils.ts                       # cn() (clsx + tailwind-merge)
    └── tests/                              # 70 tests unitarios (Vitest + Testing Library)
```

---

## DOCKER COMPOSE

```bash
# Iniciar infraestructura completa
docker compose up -d

# Detener servicios
docker compose down
```

### Puertos de Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| PostgreSQL Master | 5432 | Base de datos — escritura (Read/Write) |
| PostgreSQL Replica | 5433 | Base de datos — lectura (Read-Only) |
| Backend | 8080 | API Spring Boot + GraphQL |
| Frontend | 3000 | Next.js |
| Jenkins | 8081 | CI/CD |
| GraphiQL | 8080/graphiql | Playground GraphQL |
| Actuator | 8080/actuator/health | Health check |

---

## CI/CD (Jenkins)

Pipeline definido en `Jenkinsfile`:

1. **Checkout** — Clona el repositorio
2. **Build Backend** — `./gradlew clean build -x test`
3. **Build Frontend** — `pnpm install && pnpm build`
4. **Build Docker Images** — `docker-compose build`
5. **Deploy** — `docker-compose up -d`
6. **Health Check** — Verifica backend (`/actuator/health`) y frontend

---

## CREDENCIALES

### Base de Datos
| Usuario | Password | Uso |
|---------|----------|-----|
| postgres | postgres | Admin / CQRS (Master & Replica) |
| replicator | replicator_password | Replicación streaming |

### Usuario de Prueba
- **Email**: admin@ecommerce.com
- **Password**: admin123


