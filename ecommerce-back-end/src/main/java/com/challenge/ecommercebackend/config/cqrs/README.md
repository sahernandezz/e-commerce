# Configuración CQRS - Command Query Responsibility Segregation

## Descripción

Esta implementación de CQRS separa las operaciones de **escritura (Command)** y **lectura (Query)** a nivel de repositorio, utilizando dos DataSources diferentes pero compartiendo las mismas entidades JPA.

## Arquitectura

```
┌─────────────────────────────────────────────────┐
│              Capa de Servicio                   │
└─────────────┬───────────────────────┬───────────┘
              │                       │
              ▼                       ▼
┌─────────────────────┐   ┌─────────────────────┐
│  Command Repository │   │  Query Repository   │
│  (Escritura)        │   │  (Lectura)          │
└──────────┬──────────┘   └──────────┬──────────┘
           │                         │
           ▼                         ▼
┌─────────────────────┐   ┌─────────────────────┐
│  Write DataSource   │   │  Read DataSource    │
│  (Master DB)        │   │  (Replica DB)       │
└─────────────────────┘   └─────────────────────┘
           │                         │
           └───────────┬─────────────┘
                       ▼
            ┌─────────────────────┐
            │   Mismas Entidades  │
            │   (User, Product,   │
            │    Order, etc.)     │
            └─────────────────────┘
```

## Componentes

### 1. DataSourceConfig
Define los dos DataSources:
- **writeDataSource**: Para operaciones de escritura (INSERT, UPDATE, DELETE)
- **readDataSource**: Para operaciones de lectura (SELECT)

### 2. CommandJpaConfig
Configuración JPA para repositorios de escritura:
- EntityManagerFactory: `commandEntityManagerFactory`
- TransactionManager: `commandTransactionManager`
- Escanea paquetes: `*.repository.command`

### 3. QueryJpaConfig
Configuración JPA para repositorios de lectura:
- EntityManagerFactory: `queryEntityManagerFactory`
- TransactionManager: `queryTransactionManager`
- Escanea paquetes: `*.repository.query`
- Optimizado para lecturas masivas

### 4. Anotaciones Personalizadas

#### @CommandTransactional
Para servicios que realizan escritura:
```java
@Service
@CommandTransactional
public class UserCommandService {
    @Autowired
    private IUserCommandRepository userCommandRepository;
    
    public User createUser(User user) {
        return userCommandRepository.save(user);
    }
}
```

#### @QueryTransactional
Para servicios que solo leen:
```java
@Service
@QueryTransactional
public class UserQueryService {
    @Autowired
    private IUserQueryRepository userQueryRepository;
    
    public User findUserById(Long id) {
        return userQueryRepository.findById(id).orElse(null);
    }
}
```

## Estructura de Repositorios

```
modules/
├── user/
│   └── persisten/
│       ├── entity/
│       │   └── User.java          # Entidad compartida
│       └── repository/
│           ├── command/
│           │   └── IUserCommandRepository.java  # Escritura
│           └── query/
│               └── IUserQueryRepository.java    # Lectura
│
├── product/
│   └── persisten/
│       ├── entity/
│       │   ├── Product.java       # Entidad compartida
│       │   └── Category.java      # Entidad compartida
│       └── repository/
│           ├── command/
│           │   ├── IProductCommandRepository.java
│           │   └── ICategoryCommandRepository.java
│           └── query/
│               ├── IProductQueryRepository.java
│               └── ICategoryQueryRepository.java
│
└── order/
    └── persisten/
        ├── entity/
        │   └── Order.java         # Entidad compartida
        └── repository/
            ├── command/
            │   └── IOrderCommandRepository.java
            └── query/
                └── IOrderQueryRepository.java
```

## Configuración de DataSources (application-local.yml)

```yaml
spring:
  datasource:
    # DataSource de escritura (Master)
    write:
      url: jdbc:postgresql://localhost:5432/ecommerce
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
      hikari:
        pool-name: WriteHikariPool
        maximum-pool-size: 10
        minimum-idle: 5
        connection-timeout: 30000
        idle-timeout: 600000
        max-lifetime: 1800000

    # DataSource de lectura (Replica)
    read:
      url: jdbc:postgresql://localhost:5433/ecommerce
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
      hikari:
        pool-name: ReadHikariPool
        maximum-pool-size: 20
        minimum-idle: 10
        connection-timeout: 30000
        idle-timeout: 600000
        max-lifetime: 1800000
```

## Ventajas de esta Implementación

1. **Separación de Responsabilidades**: Las operaciones de lectura y escritura están claramente separadas
2. **Escalabilidad**: Puedes escalar lecturas y escrituras independientemente
3. **Optimización**: El datasource de lectura puede tener configuraciones optimizadas para queries
4. **Replicación**: Facilita el uso de réplicas de base de datos para lecturas
5. **Mantenibilidad**: Código más limpio y fácil de mantener
6. **Mismas Entidades**: No hay duplicación de código de entidades

## Uso en Servicios

### Ejemplo: UserService con Command y Query

```java
@Service
public class UserService {
    
    @Autowired
    private UserCommandService userCommandService;
    
    @Autowired
    private UserQueryService userQueryService;
    
    public User registerUser(UserDTO userDTO) {
        // Operación de escritura
        return userCommandService.createUser(userDTO);
    }
    
    public User getUserById(Long id) {
        // Operación de lectura
        return userQueryService.findUserById(id);
    }
}
```

### UserCommandService (Escritura)

```java
@Service
@CommandTransactional
public class UserCommandService {
    
    @Autowired
    private IUserCommandRepository userCommandRepository;
    
    public User createUser(UserDTO dto) {
        User user = new User();
        // ... mapear datos
        return userCommandRepository.save(user);
    }
    
    public User updateUser(Long id, UserDTO dto) {
        User user = userCommandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        // ... actualizar datos
        return userCommandRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userCommandRepository.deleteById(id);
    }
}
```

### UserQueryService (Lectura)

```java
@Service
@QueryTransactional
public class UserQueryService {
    
    @Autowired
    private IUserQueryRepository userQueryRepository;
    
    public User findUserById(Long id) {
        return userQueryRepository.findById(id).orElse(null);
    }
    
    public List<User> findAllUsers() {
        return userQueryRepository.findAll();
    }
    
    public Page<User> findUsersByStatus(UserStatus status, Pageable pageable) {
        return userQueryRepository.findByStatus(status, pageable);
    }
}
```

## Mejores Prácticas

1. **Nunca mezclar**: No uses repositorios Command en servicios Query y viceversa
2. **Transacciones explícitas**: Usa `@CommandTransactional` o `@QueryTransactional`
3. **Validación**: Los repositorios Command deben validar antes de escribir
4. **Caché**: Considera cachear resultados en los servicios Query
5. **Eventos**: Considera publicar eventos después de operaciones Command para sincronización

## Monitoreo

Puedes monitorear las conexiones de cada pool:
- `WriteHikariPool`: Conexiones de escritura
- `ReadHikariPool`: Conexiones de lectura

## Testing

```java
@SpringBootTest
class UserCommandServiceTest {
    
    @Autowired
    private UserCommandService commandService;
    
    @Autowired
    private UserQueryService queryService;
    
    @Test
    @Transactional("commandTransactionManager")
    void testCreateUser() {
        User user = commandService.createUser(userDTO);
        assertNotNull(user.getId());
    }
    
    @Test
    @Transactional("queryTransactionManager")
    void testFindUser() {
        User user = queryService.findUserById(1L);
        assertNotNull(user);
    }
}
```

## Consideraciones

- **Consistencia eventual**: Si usas réplicas, puede haber un pequeño delay
- **Costos**: Necesitas mantener dos conexiones (o más si tienes múltiples réplicas)
- **Complejidad**: Añade complejidad al proyecto, úsalo solo si lo necesitas

## Configuración para Producción

En producción, típicamente:
- `writeDataSource` → Apunta al Master DB
- `readDataSource` → Apunta a una o múltiples Réplicas (con load balancing)

```yaml
spring:
  datasource:
    write:
      url: jdbc:postgresql://master.db.prod:5432/ecommerce
    read:
      url: jdbc:postgresql://replica.db.prod:5432/ecommerce
```

