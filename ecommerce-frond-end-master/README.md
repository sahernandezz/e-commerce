# Ecommerce Front-end

Este proyecto es una aplicación de comercio electrónico construida con **Next.js 14**, **React** y **TypeScript**. Utiliza la arquitectura de rutas del directorio `app` y consume una API GraphQL para obtener la información de productos y realizar pedidos.

## Arquitectura del proyecto

```
/               - raíz del repositorio
├── app/        - páginas y layouts de Next.js
│   ├── layout.tsx
│   ├── page.tsx
│   └── ...
├── components/ - componentes reutilizables (Navbar, Drawer, Card, etc.)
├── context/    - proveedores de contexto (Auth, Cart, Drawer, LoginModal)
├── lib/        - utilidades y consultas/mutaciones GraphQL
│   ├── graphql/
│   │   ├── query.ts
│   │   └── mutation.ts
│   └── types.ts
├── public/     - recursos estáticos
├── Dockerfile  - imagen de producción
├── compose.yml - entorno de desarrollo con Docker
└── ...
```

### Contextos de React

- **AuthProvider**: gestiona la autenticación y el almacenamiento del token.
- **CartProvider**: mantiene el estado del carrito y su persistencia en `localStorage`.
- **DrawerProvider** y **LoginModalProvider**: controlan la visibilidad del panel lateral y del modal de inicio de sesión.

### Comunicación con la API

Las funciones de `lib/graphql` utilizan la librería [`graphql-request`](https://github.com/graphql-request/graphql-request) para interactuar con el servidor GraphQL ubicado por defecto en `http://localhost:8080/graphql`.

### Estilos

El proyecto utiliza **Tailwind CSS** con el plugin `tailwindcss-animate` para las animaciones. El archivo de configuración se encuentra en `tailwind.config.ts`.

## Puesta en marcha

1. Instala las dependencias con [PNPM](https://pnpm.io/):

   ```bash
   pnpm install
   ```

2. Inicia el entorno de desarrollo:

   ```bash
   pnpm dev
   ```

   La aplicación estará disponible en [http://localhost:3000](http://localhost:3000).

3. Ejecuta el linter para comprobar el código:

   ```bash
   pnpm lint
   ```

4. Para generar la versión de producción:

   ```bash
   pnpm build
   pnpm start
   ```

### Uso con Docker

También es posible levantar el proyecto utilizando Docker y el archivo `compose.yml`:

```bash
docker compose up --build
```

## Personalización

Si necesitas modificar el endpoint de la API GraphQL, ajusta la constante `endpoint` presente en `lib/graphql/query.ts` y `lib/graphql/mutation.ts`.

## Licencia

Este proyecto está disponible bajo los términos de la licencia MIT.
