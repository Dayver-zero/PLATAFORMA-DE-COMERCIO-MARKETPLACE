# Manual Técnico - Plataforma de Comercio Local Inteligente

---

## 1. Información General

| Campo | Valor |
|-------|-------|
| **Nombre del proyecto** | Plataforma de Comercio Local Inteligente |
| **Nombre del artefacto** | `marketplace-pacccioli` |
| **Versión** | `1.0.0` |
| **Descripción** | Backend para plataforma de comercio local con recomendaciones contextuales basadas en clima, ubicación geográfica e historial de usuario |
| **Objetivo** | Fortalecer la economía local de Punata, Bolivia, conectando comerciantes y clientes mediante un sistema inteligente de recomendaciones |
| **Arquitectura general** | Cliente-Servidor. Frontend SPA en React consume API REST del backend Spring Boot. Comunicación vía HTTP/JSON con autenticación JWT |
| **Patrón utilizado** | **MVC** (Model-View-Controller) en el backend con capas: Controller → Service → Repository (JPA). Frontend basado en componentes funcionales de React con estado global en `App.jsx` |

---

## 2. Tecnologías Utilizadas

### Backend

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| Maven | 3.x (configuración en pom.xml) |
| Spring Boot Starter Web | 3.2.0 |
| Spring Boot Starter Data JPA | 3.2.0 |
| Spring Boot Starter Security | 3.2.0 |
| Spring Boot Starter Validation | 3.2.0 |
| MySQL Connector/J | 8.x (runtime) |
| JJWT (API + Impl + Jackson) | 0.12.3 |
| Lombok | 1.18.42 |
| Hibernate | 6.x (incluido en Spring Boot 3.2.0) |
| BCrypt | Incluido en Spring Security |
| Jackson | Incluido en Spring Boot Web |

### Frontend

| Tecnología | Versión |
|------------|---------|
| React | ^18.2.0 |
| Vite | ^5.0.8 |
| Node | ^18+ (requerido) |
| Axios | ^1.6.2 |
| Tailwind CSS | ^3.4.0 |
| Lucide React | ^1.17.0 |
| PostCSS | ^8.4.32 |
| Autoprefixer | ^10.4.16 |
| ESLint | ^8.55.0 |
| Vite Plugin PWA | ^0.17.4 |
| Workbox Window | ^7.0.0 |

### Base de Datos

| Componente | Detalle |
|------------|---------|
| Motor | MySQL |
| Driver | `com.mysql.cj.jdbc.Driver` |
| Dialecto Hibernate | `org.hibernate.dialect.MySQLDialect` |
| DDL automático | `spring.jpa.hibernate.ddl-auto=update` |

### Otras Herramientas

| Herramienta | Uso |
|-------------|-----|
| **JWT (JSON Web Tokens)** | Autenticación stateless con tokens firmados HMAC-SHA256 |
| **OpenWeather API** | Obtención de clima actual y pronóstico para recomendaciones contextuales |
| **Leaflet** | Se menciona en el frontend pero no se encontró implementación activa en los archivos fuente |
| **Axios** | Cliente HTTP para peticiones del frontend al backend |
| **Lombok** | Reducción de boilerplate (anotaciones @Data, @Slf4j, @RequiredArgsConstructor) |
| **Spring Security** | Seguridad de endpoints con filtro JWT |
| **JPA / Hibernate** | ORM para mapeo objeto-relacional |
| **BCrypt** | Encriptación de contraseñas |
| **Fórmula Haversine** | Cálculo de distancias geográficas (GeolocalizacionService) |

---

## 3. Estructura del Proyecto

### Árbol completo del proyecto

```
marketplace-pacccioli/
├── .github/
│   └── modernize/
│       └── java-upgrade/
├── .vscode/
├── backend/
│   ├── .gitignore
│   ├── backend.log
│   ├── pom.xml
│   ├── uploads/
│   │   └── productos/
│   │       ├── sin-imagen.svg
│   │       └── *.jpg, *.png (archivos subidos)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/marketplace/pacccioli/
│   │   │   │   ├── MarketplacePacccioliApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── DataSeeder.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   ├── JwtFilter.java
│   │   │   │   │   ├── JwtProvider.java
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Usuario.java
│   │   │   │   │   ├── Producto.java
│   │   │   │   │   ├── Comercio.java
│   │   │   │   │   ├── Pedido.java
│   │   │   │   │   ├── PedidoItem.java
│   │   │   │   │   ├── Carrito.java
│   │   │   │   │   ├── CarritoItem.java
│   │   │   │   │   ├── Reserva.java
│   │   │   │   │   └── Interaccion.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── ApiResponseDTO.java
│   │   │   │   │   ├── UsuarioDTO.java
│   │   │   │   │   ├── ProductoDTO.java
│   │   │   │   │   ├── ComercioDTO.java
│   │   │   │   │   ├── PedidoDTO.java
│   │   │   │   │   ├── PedidoItemDTO.java
│   │   │   │   │   ├── CarritoDTO.java
│   │   │   │   │   ├── CarritoItemDTO.java
│   │   │   │   │   ├── ReservaDTO.java
│   │   │   │   │   ├── InteraccionDTO.java
│   │   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   │   └── LoginResponseDTO.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── UsuarioRepository.java
│   │   │   │   │   ├── ProductoRepository.java
│   │   │   │   │   ├── ComercioRepository.java
│   │   │   │   │   ├── PedidoRepository.java
│   │   │   │   │   ├── PedidoItemRepository.java
│   │   │   │   │   ├── CarritoRepository.java
│   │   │   │   │   ├── CarritoItemRepository.java
│   │   │   │   │   ├── ReservaRepository.java
│   │   │   │   │   └── InteraccionRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── CarritoService.java
│   │   │   │   │   ├── ClimaService.java
│   │   │   │   │   ├── GeolocalizacionService.java
│   │   │   │   │   ├── MotorRecomendacionService.java
│   │   │   │   │   ├── PedidoService.java
│   │   │   │   │   └── ReservaService.java
│   │   │   │   └── controller/
│   │   │   │       ├── AuthController.java
│   │   │   │       ├── UsuarioController.java
│   │   │   │       ├── ProductoController.java
│   │   │   │       ├── ComercioController.java
│   │   │   │       ├── PedidoController.java
│   │   │   │       ├── CarritoController.java
│   │   │   │       ├── ReservaController.java
│   │   │   │       ├── InteraccionController.java
│   │   │   │       ├── ClimaController.java
│   │   │   │       ├── RecomendacionController.java
│   │   │   │       └── UploadController.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-local.properties
│   │   │       └── data.sql
│   │   └── test/java/com/marketplace/pacccioli/service/
│   │       ├── ClimaServiceTest.java
│   │       ├── GeolocalizacionServiceTest.java
│   │       └── MotorRecomendacionServiceTest.java
│   └── target/
│       └── marketplace-pacccioli-1.0.0.jar
│
└── frontend/
    ├── index.html
    ├── package.json
    ├── vite.config.js
    ├── postcss.config.js
    ├── tailwind.config.js
    ├── public/
    │   ├── favicon-logo-marquetplace.ico
    │   ├── favicon-marquetplace.png
    │   └── manifest.json
    └── src/
        ├── main.jsx
        ├── App.jsx
        ├── index.css
        ├── components/
        │   ├── Header.jsx
        │   ├── Login.jsx
        │   ├── FeedRecomendaciones.jsx
        │   ├── ProductCard.jsx
        │   ├── Carrito.jsx
        │   ├── Checkout.jsx
        │   ├── HistorialPedidos.jsx
        │   ├── MisReservas.jsx
        │   ├── ModalReserva.jsx
        │   ├── Perfil.jsx
        │   ├── PanelComerciante.jsx
        │   ├── FormularioProducto.jsx
        │   ├── TablaInventario.jsx
        │   ├── GestionPedidos.jsx
        │   └── GestionReservas.jsx
        └── services/
            ├── api.js
            ├── authService.js
            ├── carritoService.js
            ├── climaService.js
            ├── comerciosService.js
            ├── geolocalizacionService.js
            ├── pedidosService.js
            ├── productosService.js
            ├── recomendacionesService.js
            └── reservasService.js
```

### Función de cada carpeta - Backend

| Carpeta | Función |
|---------|---------|
| `config/` | Clases de configuración de Spring: `WebConfig` (recursos estáticos), `DataSeeder` (datos iniciales) |
| `security/` | Seguridad: `JwtProvider` (generar/validar tokens), `JwtFilter` (filtro por petición), `SecurityConfig` (cadena de filtros), `CorsConfig` (CORS) |
| `model/` | Entidades JPA que mapean las tablas de la base de datos |
| `dto/` | Data Transfer Objects para comunicación con el frontend. Segregan la capa de persistencia de la API |
| `repository/` | Interfaces Spring Data JPA para acceso a datos. Contienen métodos de consulta personalizados |
| `service/` | Lógica de negocio. Orquestan operaciones entre repositories, validan reglas de negocio |
| `controller/` | Controladores REST. Manejan peticiones HTTP, delegan en servicios, retornan respuestas JSON |
| `resources/` | Archivos de configuración: `application.properties`, `application-local.properties`, `data.sql` |
| `test/` | Tests unitarios de los servicios principales |

### Función de cada carpeta - Frontend

| Carpeta | Función |
|---------|---------|
| `components/` | Componentes funcionales de React. Cada uno representa una vista o sección de la UI |
| `services/` | Módulos JavaScript que encapsulan llamadas Axios a la API REST del backend |
| `public/` | Archivos estáticos: favicons, manifiesto PWA |
| Raíz (`src/`) | `main.jsx` (entry point), `App.jsx` (componente raíz con enrutamiento por estado), `index.css` (estilos Tailwind) |

---

## 4. Configuración del Proyecto

### `application.properties` (backend)

```properties
# =============================================
# CONFIGURACIÓN DE BASE DE DATOS MYSQL
# =============================================
spring.datasource.url=jdbc:mysql://localhost:3306/marketplace_pacccioli?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=nano123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# =============================================
# CONFIGURACIÓN DE JPA / HIBERNATE
# =============================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# =============================================
# CONFIGURACIÓN DEL SERVIDOR
# =============================================
server.port=8080

# =============================================
# CONFIGURACIÓN DE LOGGING
# =============================================
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
logging.level.com.marketplace.pacccioli=DEBUG

# =============================================
# CONFIGURACIÓN DE JWT (TOKENS)
# =============================================
jwt.secret=miClaveSecretaParaFirmarTokensJWT2024PlataformaComercioLocal
jwt.expiration=86400000

# =============================================
# CONFIGURACIÓN DE APIs EXTERNAS
# =============================================
spring.config.import=optional:classpath:application-local.properties
openweather.api.key=

google.maps.api.key=

# =============================================
# CONFIGURACIÓN DE SUBIDA DE ARCHIVOS
# =============================================
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
app.upload.dir=uploads

# =============================================
# CONFIGURACIÓN DE RECOMENDACIONES
# =============================================
recomendacion.radio-busqueda-predeterminado=5
recomendacion.max-recomendaciones=20
recomendacion.peso-clima=0.3
recomendacion.peso-ubicacion=0.3
recomendacion.peso-historial=0.4
```

### Variables de configuración

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `server.port` | `8080` | Puerto del servidor backend |
| `jwt.secret` | `miClaveSecretaParaFirmarTokensJWT2024PlataformaComercioLocal` | Clave secreta para firmar JWT (HMAC-SHA256) |
| `jwt.expiration` | `86400000` | Expiración del token en ms (24 horas) |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/marketplace_pacccioli` | URL de conexión MySQL |
| `spring.datasource.username` | `root` | Usuario de base de datos |
| `spring.datasource.password` | `nano123` | Contraseña de base de datos |
| `app.upload.dir` | `uploads` | Directorio de subida de archivos |
| `spring.servlet.multipart.max-file-size` | `5MB` | Tamaño máximo por archivo |
| `spring.servlet.multipart.max-request-size` | `10MB` | Tamaño máximo total por petición |
| `openweather.api.key` | (en local.properties) | API key de OpenWeather |
| `google.maps.api.key` | (vacío) | API key de Google Maps |
| `recomendacion.peso-clima` | `0.3` | Peso del clima en recomendaciones |
| `recomendacion.peso-ubicacion` | `0.3` | Peso de la ubicación en recomendaciones |
| `recomendacion.peso-historial` | `0.4` | Peso del historial en recomendaciones |
| `recomendacion.max-recomendaciones` | `20` | Máximo de recomendaciones a retornar |

### Configuración CORS (`CorsConfig.java`)

| Parámetro | Valor |
|-----------|-------|
| Orígenes permitidos | `http://localhost:3000`, `http://localhost:5173`, `http://localhost:4173`, `http://127.0.0.1:5173`, `http://127.0.0.1:4173` |
| Métodos permitidos | `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS` |
| Headers permitidos | `*` |
| Credenciales | `true` |

### Configuración JWT

| Parámetro | Valor |
|-----------|-------|
| Algoritmo | HMAC-SHA256 |
| Claims incluidos | `sub` (usuarioId), `email`, `rol`, `iat`, `exp` |
| Encabezado HTTP | `Authorization: Bearer <token>` |
| Expiración | 24 horas (86400000 ms) |
| Filtro | `JwtFilter` aplicado ANTES de `UsernamePasswordAuthenticationFilter` |

### Configuración Base de Datos

| Propiedad | Valor |
|-----------|-------|
| URL | `jdbc:mysql://localhost:3306/marketplace_pacccioli?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
| Driver | `com.mysql.cj.jdbc.Driver` |
| Dialecto | `org.hibernate.dialect.MySQLDialect` |
| DDL | `update` (Hibernate crea/actualiza tablas automáticamente) |
| SQL log | `true` (con formato) |

---

## 5. Dependencias

### Backend (Maven - pom.xml)

| Dependencia | Versión | Ámbito | Propósito |
|-------------|---------|--------|-----------|
| `spring-boot-starter-web` | 3.2.0 | compile | Framework web MVC con Tomcat embebido |
| `spring-boot-starter-data-jpa` | 3.2.0 | compile | ORM con Hibernate + Spring Data JPA |
| `mysql-connector-j` | 8.x | runtime | Driver de conexión a MySQL |
| `spring-boot-starter-security` | 3.2.0 | compile | Seguridad: autenticación, autorización, CSRF, CORS |
| `jjwt-api` | 0.12.3 | compile | API de JJWT para generar/validar JWT |
| `jjwt-impl` | 0.12.3 | runtime | Implementación de JJWT |
| `jjwt-jackson` | 0.12.3 | runtime | Serialización JSON para JJWT vía Jackson |
| `lombok` | 1.18.42 | compile (optional) | Anotaciones para reducir código boilerplate |
| `spring-boot-starter-validation` | 3.2.0 | compile | Validación con Jakarta Bean Validation |
| `spring-boot-devtools` | 3.2.0 | runtime (optional) | Herramientas de desarrollo (hot reload) |
| `spring-boot-starter-test` | 3.2.0 | test | Testing con JUnit, Mockito, etc. |
| `spring-security-test` | 3.2.0 | test | Testing de seguridad |

### Plugins Maven

| Plugin | Versión | Propósito |
|--------|---------|-----------|
| `maven-compiler-plugin` | 3.11.0 | Compilación Java 21 con procesamiento de anotaciones Lombok |
| `spring-boot-maven-plugin` | 3.2.0 | Empaquetado como JAR ejecutable (excluye Lombok) |
| `maven-surefire-plugin` | - | Ejecución de tests con `-Dnet.bytebuddy.experimental=true` |

### Frontend (npm - package.json)

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| `react` | ^18.2.0 | Biblioteca principal de UI |
| `react-dom` | ^18.2.0 | Renderizado DOM de React |
| `axios` | ^1.6.2 | Cliente HTTP para peticiones al backend |
| `lucide-react` | ^1.17.0 | Iconos SVG modernos |

### DevDependencias Frontend

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| `@vitejs/plugin-react` | ^4.2.1 | Plugin de Vite para React (Fast Refresh) |
| `vite` | ^5.0.8 | Bundler y dev server |
| `tailwindcss` | ^3.4.0 | Framework CSS utility-first |
| `postcss` | ^8.4.32 | Procesador CSS para Tailwind |
| `autoprefixer` | ^10.4.16 | Prefijos CSS automáticos |
| `eslint` | ^8.55.0 | Linter de JavaScript |
| `eslint-plugin-react` | ^7.33.2 | Reglas específicas de React para ESLint |
| `eslint-plugin-react-hooks` | ^4.6.0 | Reglas para hooks de React |
| `eslint-plugin-react-refresh` | ^0.4.5 | Reglas para React Fast Refresh |
| `@types/react` | ^18.2.43 | Tipos TypeScript para React |
| `@types/react-dom` | ^18.2.17 | Tipos TypeScript para ReactDOM |
| `vite-plugin-pwa` | ^0.17.4 | Plugin Vite para Progressive Web App |
| `workbox-window` | ^7.0.0 | Librería Workbox para service workers PWA |

---

## 6. Base de Datos

### Esquema de tablas generado por Hibernate (ddl-auto=update)

---

### Entidad: `Usuario` → Tabla: `usuarios`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | Nombre de usuario |
| `password` | VARCHAR(100) | NOT NULL | Hash BCrypt de la contraseña |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE | Correo electrónico |
| `nombre` | VARCHAR(100) | - | Nombre completo |
| `apellidos` | VARCHAR(100) | - | Apellidos |
| `telefono` | VARCHAR(20) | - | Teléfono de contacto |
| `direccion` | VARCHAR(255) | - | Dirección física |
| `rol` | ENUM('CLIENTE','COMERCIANTE','ADMIN') | NOT NULL | Rol del usuario |
| `preferencias` | TEXT | - | Preferencias en JSON |
| `historial_busqueda` | TEXT | - | Historial de búsquedas en JSON |
| `latitud` | DOUBLE | - | Latitud de ubicación |
| `longitud` | DOUBLE | - | Longitud de ubicación |
| `radio_busqueda_km` | INT | DEFAULT 5 | Radio de búsqueda en km |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado del usuario |
| `fecha_creacion` | DATETIME | NOT NULL, updatable=false | Fecha de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Fecha de última modificación |

**Relaciones:**
- `Usuario` 1 → N `Comercio` (propietario)
- `Usuario` 1 → N `Interaccion` (usuario)
- `Usuario` 1 → 1 `Carrito`

---

### Entidad: `Comercio` → Tabla: `comercios`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre del comercio |
| `descripcion` | VARCHAR(500) | NOT NULL | Descripción del comercio |
| `direccion` | VARCHAR(255) | - | Dirección física |
| `telefono` | VARCHAR(20) | - | Teléfono de contacto |
| `email` | VARCHAR(100) | - | Correo electrónico |
| `horario_atencion` | VARCHAR(100) | - | Horario de atención |
| `categoria` | ENUM('RESTAURANTE','CAFE','TIENDA_ROPA','SUPERMERCADO','FARMACIA','LIBRERIA','ELECTRONICA','HOGAR','DEPORTES','BELLEZA','OTROS') | NOT NULL | Categoría del comercio |
| `latitud` | DOUBLE | NOT NULL | Latitud |
| `longitud` | DOUBLE | NOT NULL | Longitud |
| `imagen_url` | VARCHAR(500) | - | URL de imagen |
| `calificacion` | DOUBLE | DEFAULT 0.0 | Calificación promedio |
| `numero_reseñas` | INT | - | Número de reseñas |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado del comercio |
| `fecha_creacion` | DATETIME | NOT NULL, updatable=false | Fecha de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Fecha de última modificación |
| `propietario_id` | BIGINT | FK → `usuarios(id)`, NOT NULL | Propietario del comercio |

**Relaciones:**
- `Comercio` N → 1 `Usuario` (propietario)
- `Comercio` 1 → N `Producto` (comercio)
- `Comercio` 1 → N `Reserva` (comercio)

---

### Entidad: `Producto` → Tabla: `productos`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre del producto |
| `descripcion` | VARCHAR(500) | NOT NULL | Descripción del producto |
| `precio` | DECIMAL(10,2) | NOT NULL | Precio unitario |
| `stock` | INT | NOT NULL, DEFAULT 0 | Cantidad en stock |
| `url_imagen` | VARCHAR(500) | - | URL de imagen |
| `etiquetas_inteligentes` | TEXT | - | Etiquetas en JSON para matching climático |
| `categoria` | ENUM('ROPA','ACCESORIOS','COMIDA','BEBIDA','ELECTRONICA','HOGAR','DEPORTES','BELLEZA','SALUD','LIBROS','JUGUETES','OTROS') | NOT NULL | Categoría del producto |
| `estado` | ENUM('DISPONIBLE','AGOTADO','DESCONTINUADO') | NOT NULL, DEFAULT 'DISPONIBLE' | Estado del producto |
| `conteo_visualizaciones` | INT | DEFAULT 0 | Contador de vistas |
| `conteo_compras` | INT | DEFAULT 0 | Contador de compras |
| `calificacion_promedio` | DOUBLE | DEFAULT 0.0 | Calificación promedio |
| `activo` | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado activo |
| `permite_reserva` | BOOLEAN | NOT NULL, DEFAULT FALSE | Permite reservas |
| `permite_pago_adelantado` | BOOLEAN | NOT NULL, DEFAULT FALSE | Permite pago adelantado |
| `fecha_creacion` | DATETIME | NOT NULL, updatable=false | Fecha de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Fecha de última modificación |
| `comercio_id` | BIGINT | FK → `comercios(id)`, NOT NULL | Comercio al que pertenece |

**Relaciones:**
- `Producto` N → 1 `Comercio` (comercio)
- `Producto` 1 → N `Interaccion` (producto)
- `Producto` 1 → N `PedidoItem` (producto)
- `Producto` 1 → N `CarritoItem` (producto)
- `Producto` 1 → N `Reserva` (producto)

---

### Entidad: `Pedido` → Tabla: `pedidos`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `usuario_id` | BIGINT | FK → `usuarios(id)`, NOT NULL | Usuario que realiza el pedido |
| `estado` | ENUM('PENDIENTE','CONFIRMADO','ENVIADO','ENTREGADO','CANCELADO') | NOT NULL, DEFAULT 'PENDIENTE' | Estado del pedido |
| `total` | DECIMAL(12,2) | NOT NULL | Monto total |
| `metodo_pago` | ENUM('EFECTIVO','YAPE','TARJETA') | NOT NULL | Método de pago |
| `codigo_pago` | VARCHAR(30) | - | Código de pago (YAPE) |
| `comprobante_url` | VARCHAR(500) | - | URL del comprobante |
| `referencia_pago` | VARCHAR(100) | - | Referencia de pago |
| `direccion_envio` | VARCHAR(500) | - | Dirección de envío |
| `notas` | TEXT | - | Notas del pedido |
| `fecha_creacion` | DATETIME | NOT NULL, updatable=false | Fecha de creación |
| `fecha_pago` | DATETIME | - | Fecha de pago |

**Relaciones:**
- `Pedido` N → 1 `Usuario` (usuario)
- `Pedido` 1 → N `PedidoItem` (items)

---

### Entidad: `PedidoItem` → Tabla: `pedido_items`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `pedido_id` | BIGINT | FK → `pedidos(id)`, NOT NULL | Pedido al que pertenece |
| `producto_id` | BIGINT | FK → `productos(id)`, NOT NULL | Producto |
| `nombre_producto` | VARCHAR(100) | NOT NULL | Nombre al momento del pedido |
| `precio_unitario` | DECIMAL(10,2) | NOT NULL | Precio al momento del pedido |
| `cantidad` | INT | NOT NULL | Cantidad comprada |
| `subtotal` | DECIMAL(12,2) | NOT NULL | Subtotal (precio × cantidad) |

**Relaciones:**
- `PedidoItem` N → 1 `Pedido` (pedido)
- `PedidoItem` N → 1 `Producto` (producto)

---

### Entidad: `Carrito` → Tabla: `carritos`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `usuario_id` | BIGINT | FK → `usuarios(id)`, NOT NULL, UNIQUE | Usuario (1 carrito por usuario) |
| `fecha_creacion` | DATETIME | NOT NULL, updatable=false | Fecha de creación |
| `fecha_actualizacion` | DATETIME | NOT NULL | Fecha de última modificación |

**Relaciones:**
- `Carrito` 1 → 1 `Usuario`
- `Carrito` 1 → N `CarritoItem`

---

### Entidad: `CarritoItem` → Tabla: `carrito_items`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `carrito_id` | BIGINT | FK → `carritos(id)`, NOT NULL | Carrito al que pertenece |
| `producto_id` | BIGINT | FK → `productos(id)`, NOT NULL | Producto agregado |
| `cantidad` | INT | NOT NULL, DEFAULT 1 | Cantidad |
| `fecha_agregado` | DATETIME | NOT NULL, updatable=false | Fecha en que se agregó |

**Relaciones:**
- `CarritoItem` N → 1 `Carrito` (carrito)
- `CarritoItem` N → 1 `Producto` (producto)

---

### Entidad: `Reserva` → Tabla: `reservas`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `usuario_id` | BIGINT | FK → `usuarios(id)`, NOT NULL | Usuario que reserva |
| `producto_id` | BIGINT | FK → `productos(id)`, NOT NULL | Producto reservado |
| `comercio_id` | BIGINT | FK → `comercios(id)`, NOT NULL | Comercio del producto |
| `cantidad` | INT | NOT NULL, DEFAULT 1 | Cantidad reservada |
| `estado` | ENUM('PENDIENTE','CONFIRMADA','CANCELADA','COMPLETADA') | NOT NULL, DEFAULT 'PENDIENTE' | Estado de la reserva |
| `fecha_expiracion` | DATETIME | NOT NULL | Fecha de expiración (24h) |
| `notas` | TEXT | - | Notas adicionales |
| `fecha_reserva` | DATETIME | NOT NULL, updatable=false | Fecha de creación |

**Relaciones:**
- `Reserva` N → 1 `Usuario` (usuario)
- `Reserva` N → 1 `Producto` (producto)
- `Reserva` N → 1 `Comercio` (comercio)

---

### Entidad: `Interaccion` → Tabla: `interacciones`

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| `tipo_interaccion` | ENUM('VISUALIZACION','CLICK','COMPRA','FAVORITO') | NOT NULL | Tipo de interacción |
| `usuario_id` | BIGINT | FK → `usuarios(id)`, NOT NULL | Usuario que interactuó |
| `producto_id` | BIGINT | FK → `productos(id)`, NOT NULL | Producto interactuado |
| `cantidad` | INT | DEFAULT 1 | Cantidad |
| `precio_momento` | DECIMAL(10,2) | - | Precio al momento de la interacción |
| `contexto_clima` | TEXT | - | Descripción del clima en ese momento |
| `latitud_usuario` | DOUBLE | - | Latitud del usuario |
| `longitud_usuario` | DOUBLE | - | Longitud del usuario |
| `fuente` | ENUM('BUSQUEDA','RECOMENDACION','DIRECTO','PROMOCION','FEED') | - | Fuente de la interacción |
| `fecha_interaccion` | DATETIME | NOT NULL, updatable=false | Fecha de la interacción |

**Relaciones:**
- `Interaccion` N → 1 `Usuario` (usuario)
- `Interaccion` N → 1 `Producto` (producto)

---

### Diagrama de Relaciones

```
usuarios (1) ──── (N) comercios
usuarios (1) ──── (N) interacciones
usuarios (1) ──── (1) carritos
usuarios (1) ──── (N) pedidos
usuarios (1) ──── (N) reservas
comercios (1) ──── (N) productos
comercios (1) ──── (N) reservas
productos (1) ──── (N) interacciones
productos (1) ──── (N) pedido_items
productos (1) ──── (N) carrito_items
productos (1) ──── (N) reservas
pedidos (1) ──── (N) pedido_items
carritos (1) ──── (N) carrito_items
```

---

## 7. API REST

### Convenciones de respuesta

Todas las respuestas usan el formato `ApiResponseDTO<T>`:

```json
{
  "exito": true|false,
  "mensaje": "String descriptivo",
  "datos": { ... },
  "errores": ["String", ...],
  "timestamp": "2024-01-01T00:00:00"
}
```

---

### Módulo: Autenticación (`/api/auth`)

#### `POST /api/auth/login`

| Campo | Valor |
|-------|-------|
| **Descripción** | Autenticación de usuario con email y contraseña |
| **Body** | `{ "email": "string", "contrasena": "string", "rol": "string (opcional)" }` |
| **Respuesta éxito** | `200 - { "token": "jwt...", "tipo": "Bearer", "usuarioId": 1, "nombre": "...", "email": "...", "rol": "CLIENTE" }` |
| **Respuesta error** | `401 - Credenciales inválidas` / `401 - Usuario inactivo` |
| **Autenticación** | No requiere |

#### `POST /api/auth/registro`

| Campo | Valor |
|-------|-------|
| **Descripción** | Registro de nuevo usuario (CLIENTE por defecto, COMERCIANTE si se especifica) |
| **Body** | `{ "email": "string", "contrasena": "string", "rol": "CLIENTE|COMERCIANTE" }` |
| **Respuesta éxito** | `201 - { token, tipo, usuarioId, nombre, email, rol }` |
| **Respuesta error** | `400 - Email ya registrado` |
| **Autenticación** | No requiere |

#### `POST /api/auth/logout`

| Campo | Valor |
|-------|-------|
| **Descripción** | Logout (notifica al servidor, el cliente elimina el token) |
| **Respuesta** | `200 - Logout exitoso` |
| **Autenticación** | No requiere |

#### `POST /api/auth/verificar-email?email=xxx`

| Campo | Valor |
|-------|-------|
| **Descripción** | Verifica si un email está disponible |
| **Parámetros** | `email` (query param) |
| **Respuesta éxito** | `200 - { datos: true }` si está disponible |
| **Autenticación** | No requiere |

---

### Módulo: Usuarios (`/api/usuarios`)

#### `GET /api/usuarios`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener todos los usuarios activos |
| **Respuesta** | `200 - List<UsuarioDTO>` |
| **Autenticación** | No requerida (endpoint público) |

#### `GET /api/usuarios/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener usuario por ID |
| **Respuesta éxito** | `200 - UsuarioDTO` |
| **Respuesta error** | `404 - Usuario no encontrado` |
| **Autenticación** | No requerida |

#### `GET /api/usuarios/email/{email}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener usuario por email (case insensitive) |
| **Respuesta éxito** | `200 - UsuarioDTO` |
| **Respuesta error** | `404 - No existe usuario con email ...` |
| **Autenticación** | No requerida |

#### `GET /api/usuarios/rol/{rol}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener usuarios activos por rol |
| **Parámetros** | `rol`: CLIENTE, COMERCIANTE, ADMIN |
| **Respuesta** | `200 - List<UsuarioDTO>` |
| **Autenticación** | No requerida |

#### `PUT /api/usuarios/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Actualizar perfil (nombre, latitud, longitud, radio_busqueda, preferencias) |
| **Body** | `UsuarioDTO` con campos a actualizar |
| **Respuesta éxito** | `200 - UsuarioDTO actualizado` |
| **Respuesta error** | `403 - No tienes permiso` (solo el propio usuario) |
| **Autenticación** | Requiere JWT del mismo usuario |

#### `DELETE /api/usuarios/cuenta`

| Campo | Valor |
|-------|-------|
| **Descripción** | Soft-delete: desactiva usuario, comercios y productos |
| **Respuesta éxito** | `200 - Cuenta eliminada exitosamente` |
| **Autenticación** | Requiere JWT |

#### `PUT /api/usuarios/{id}/rol`

| Campo | Valor |
|-------|-------|
| **Descripción** | Cambiar rol (CLIENTE ↔ COMERCIANTE). Crea/desactiva comercios automáticamente |
| **Body** | `{ "rol": "CLIENTE|COMERCIANTE" }` |
| **Respuesta éxito** | `200 - { usuario, token }` (nuevo token con rol actualizado) |
| **Autenticación** | Requiere JWT del mismo usuario |

---

### Módulo: Productos (`/api/productos`)

#### `GET /api/productos`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener todos los productos (incluye datos del comercio) |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/productos/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener producto por ID |
| **Respuesta éxito** | `200 - ProductoDTO` |
| **Respuesta error** | `404 - Producto no encontrado` |
| **Autenticación** | No requerida |

#### `GET /api/productos/buscar?nombre=X`

| Campo | Valor |
|-------|-------|
| **Descripción** | Buscar productos por nombre (case insensitive) |
| **Parámetros** | `nombre` (query param) |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/productos/comercio/{comercioId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener productos activos de un comercio |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/productos/populares`

| Campo | Valor |
|-------|-------|
| **Descripción** | Top 10 productos más vistos |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/productos/calificados`

| Campo | Valor |
|-------|-------|
| **Descripción** | Top 10 productos mejor calificados |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `POST /api/productos`

| Campo | Valor |
|-------|-------|
| **Descripción** | Crear producto (solo COMERCIANTE). Se asocia al primer comercio del usuario |
| **Body** | `{ "nombre", "descripcion", "precio", "stock", "urlImagen", "categoria", "estado", "etiquetasInteligentes" }` |
| **Respuesta éxito** | `201 - ProductoDTO` |
| **Respuesta error** | `403 - Solo comerciantes pueden crear productos` |
| **Autenticación** | Requiere JWT con rol COMERCIANTE |

#### `PUT /api/productos/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Actualizar producto (solo dueño del comercio) |
| **Body** | Mismos campos que POST |
| **Respuesta éxito** | `200 - ProductoDTO` |
| **Respuesta error** | `403 - No tienes permiso` |
| **Autenticación** | Requiere JWT del propietario |

#### `DELETE /api/productos/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Eliminar producto (solo dueño del comercio) |
| **Respuesta éxito** | `200 - Producto eliminado` |
| **Respuesta error** | `403 - No tienes permiso` |
| **Autenticación** | Requiere JWT del propietario |

---

### Módulo: Comercios (`/api/comercios`)

#### `GET /api/comercios`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener todos los comercios activos |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener comercio por ID |
| **Respuesta éxito** | `200 - ComercioDTO` |
| **Respuesta error** | `404 - Comercio no encontrado` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/buscar?nombre=X`

| Campo | Valor |
|-------|-------|
| **Descripción** | Buscar comercios por nombre |
| **Parámetros** | `nombre` |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/categoria/{categoria}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener comercios por categoría |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/cercanos?latitud=X&longitud=Y&radio=Z`

| Campo | Valor |
|-------|-------|
| **Descripción** | Comercios cercanos a una ubicación (usa fórmula Haversine) |
| **Parámetros** | `latitud`, `longitud`, `radio` (default 5 km) |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/mis-comercios`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener comercios del usuario autenticado |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | Requiere JWT |

#### `GET /api/comercios/mejores`

| Campo | Valor |
|-------|-------|
| **Descripción** | Top 10 comercios mejor calificados |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/comercios/buscar-avanzado?nombre=X&latitud=Y&longitud=Z&radio=R`

| Campo | Valor |
|-------|-------|
| **Descripción** | Búsqueda combinada por nombre + ubicación |
| **Respuesta** | `200 - List<ComercioDTO>` |
| **Autenticación** | No requerida |

---

### Módulo: Carrito (`/api/carrito`)

#### `GET /api/carrito`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener carrito del usuario autenticado (lo crea si no existe) |
| **Respuesta** | `200 - CarritoDTO { items, totalItems, subtotal }` |
| **Autenticación** | Requiere JWT |

#### `GET /api/carrito/conteo`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener cantidad total de items en el carrito |
| **Respuesta** | `200 - { datos: 5 }` |
| **Autenticación** | Requiere JWT |

#### `POST /api/carrito/items`

| Campo | Valor |
|-------|-------|
| **Descripción** | Agregar producto al carrito (o incrementar cantidad si ya existe) |
| **Body** | `{ "productoId": 1, "cantidad": 2 }` |
| **Respuesta éxito** | `201 - CarritoItemDTO` |
| **Respuesta error** | `400 - Stock insuficiente` |
| **Autenticación** | Requiere JWT |

#### `PUT /api/carrito/items/{itemId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Actualizar cantidad de un item (si cantidad ≤ 0, elimina el item) |
| **Body** | `{ "cantidad": 3 }` |
| **Respuesta** | `200 - CarritoItemDTO` o `200 - null` si se eliminó |
| **Autenticación** | Requiere JWT |

#### `DELETE /api/carrito/items/{itemId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Eliminar item del carrito |
| **Respuesta** | `200 - Item eliminado` |
| **Autenticación** | Requiere JWT |

#### `DELETE /api/carrito`

| Campo | Valor |
|-------|-------|
| **Descripción** | Limpiar todo el carrito |
| **Respuesta** | `200 - Carrito limpiado` |
| **Autenticación** | Requiere JWT |

---

### Módulo: Pedidos (`/api/pedidos`)

#### `POST /api/pedidos`

| Campo | Valor |
|-------|-------|
| **Descripción** | Crear pedido desde el carrito actual (descuenta stock, limpia carrito) |
| **Body** | `{ "metodoPago": "EFECTIVO|YAPE|TARJETA" }` |
| **Respuesta éxito** | `201 - PedidoDTO` |
| **Respuesta error** | `400 - El carrito está vacío` / `400 - Stock insuficiente` |
| **Autenticación** | Requiere JWT |

#### `GET /api/pedidos`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener pedidos del usuario autenticado (ordenados por fecha descendente) |
| **Respuesta** | `200 - List<PedidoDTO>` |
| **Autenticación** | Requiere JWT |

#### `GET /api/pedidos/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener detalle de un pedido |
| **Respuesta éxito** | `200 - PedidoDTO` |
| **Respuesta error** | `404 - Pedido no encontrado` |
| **Autenticación** | Requiere JWT |

#### `PUT /api/pedidos/{id}/estado`

| Campo | Valor |
|-------|-------|
| **Descripción** | Actualizar estado del pedido (solo comerciante o admin) |
| **Body** | `{ "estado": "CONFIRMADO|ENVIADO|ENTREGADO|CANCELADO" }` |
| **Respuesta** | `200 - PedidoDTO actualizado` |
| **Autenticación** | Requiere JWT (comerciante del producto o admin) |

#### `POST /api/pedidos/{id}/pago/yape/generar`

| Campo | Valor |
|-------|-------|
| **Descripción** | Generar código de pago YAPE (formato `YAPE-XXXXXXXX`) |
| **Respuesta** | `200 - "YAPE-ABC12345"` |
| **Autenticación** | Requiere JWT (propietario del pedido) |

#### `POST /api/pedidos/{id}/pago/yape/confirmar`

| Campo | Valor |
|-------|-------|
| **Descripción** | Confirmar pago YAPE con referencia y comprobante |
| **Body** | `{ "referenciaPago": "...", "comprobanteUrl": "..." }` |
| **Respuesta** | `200 - PedidoDTO` |
| **Autenticación** | Requiere JWT (propietario del pedido) |

#### `GET /api/pedidos/comercio`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener pedidos que contienen productos del comercio del usuario autenticado |
| **Respuesta** | `200 - List<PedidoDTO>` |
| **Autenticación** | Requiere JWT (comerciante) |

---

### Módulo: Reservas (`/api/reservas`)

#### `POST /api/reservas`

| Campo | Valor |
|-------|-------|
| **Descripción** | Crear una reserva de producto (requiere `permiteReserva=true`) |
| **Body** | `{ "productoId": 1, "cantidad": 2, "notas": "..." }` |
| **Respuesta éxito** | `201 - ReservaDTO` |
| **Respuesta error** | `400 - Stock insuficiente` / `400 - No permite reserva` |
| **Autenticación** | Requiere JWT |

#### `GET /api/reservas`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener reservas del usuario autenticado |
| **Respuesta** | `200 - List<ReservaDTO>` |
| **Autenticación** | Requiere JWT |

#### `GET /api/reservas/{id}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener detalle de una reserva |
| **Respuesta éxito** | `200 - ReservaDTO` |
| **Respuesta error** | `404 - Reserva no encontrada` |
| **Autenticación** | Requiere JWT |

#### `PUT /api/reservas/{id}/cancelar`

| Campo | Valor |
|-------|-------|
| **Descripción** | Cancelar reserva (usuario o comerciante) |
| **Respuesta** | `200 - ReservaDTO cancelada` |
| **Respuesta error** | `403 - No tienes permiso` |
| **Autenticación** | Requiere JWT |

#### `PUT /api/reservas/{id}/completar`

| Campo | Valor |
|-------|-------|
| **Descripción** | Marcar reserva como completada (descuenta stock). Solo comerciante |
| **Respuesta** | `200 - ReservaDTO completada` |
| **Autenticación** | Requiere JWT (comerciante) |

#### `GET /api/reservas/comercio`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener reservas de los comercios del usuario autenticado |
| **Respuesta** | `200 - List<ReservaDTO>` |
| **Autenticación** | Requiere JWT (comerciante) |

---

### Módulo: Interacciones (`/api/interacciones`)

#### `POST /api/interacciones`

| Campo | Valor |
|-------|-------|
| **Descripción** | Registrar interacción de usuario con producto (visualización, click, compra, favorito) |
| **Body** | `{ "usuarioId", "productoId", "tipo": "VISUALIZACION|CLICK|COMPRA|FAVORITO", "fuente": "BUSQUEDA|RECOMENDACION|...", "latitudUsuario", "longitudUsuario", "climaContexto", "precioEnInteraccion" }` |
| **Respuesta** | `201 - InteraccionDTO` |
| **Autenticación** | No requerida |

#### `GET /api/interacciones/usuario/{usuarioId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Historial de interacciones de un usuario |
| **Respuesta** | `200 - List<InteraccionDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/interacciones/producto/{productoId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Interacciones de un producto |
| **Respuesta** | `200 - List<InteraccionDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/interacciones/tipo/{tipo}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Interacciones filtradas por tipo |
| **Respuesta** | `200 - List<InteraccionDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/interacciones/usuario/{usuarioId}/compras`

| Campo | Valor |
|-------|-------|
| **Descripción** | Historial de compras de un usuario |
| **Respuesta** | `200 - List<InteraccionDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/interacciones/estadisticas/producto/{productoId}`

| Campo | Valor |
|-------|-------|
| **Descripción** | Estadísticas de un producto (visualizaciones y compras) |
| **Respuesta** | `200 - "Visualizaciones: X, Compras: Y"` |
| **Autenticación** | No requerida |

---

### Módulo: Clima (`/api/clima`)

#### `GET /api/clima?latitud=X&longitud=Y`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener clima actual por coordenadas (consume OpenWeather API) |
| **Respuesta éxito** | `200 - { temperatura, sensacionTermica, humedad, condicion, icono, velocidadViento, ciudad, pais }` |
| **Respuesta error** | `404 - Servicio no disponible` (usa datos simulados como fallback) |
| **Autenticación** | No requerida |

#### `GET /api/clima/ciudad?ciudad=Nombre`

| Campo | Valor |
|-------|-------|
| **Descripción** | Obtener clima por nombre de ciudad |
| **Respuesta** | `200 - ClimaDTO` |
| **Autenticación** | No requerida |

#### `GET /api/clima/pronostico?latitud=X&longitud=Y&dias=N`

| Campo | Valor |
|-------|-------|
| **Descripción** | Pronóstico para N días (máximo 5) |
| **Respuesta** | `200 - List<ClimaDTO>` |
| **Autenticación** | No requerida |

---

### Módulo: Recomendaciones (`/api/recomendaciones`)

#### `GET /api/recomendaciones?usuarioId=X&latitud=Y&longitud=Z`

| Campo | Valor |
|-------|-------|
| **Descripción** | Recomendaciones híbridas (clima 30% + ubicación 30% + historial 40%). Máximo 20 resultados |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/recomendaciones/clima?latitud=X&longitud=Y`

| Campo | Valor |
|-------|-------|
| **Descripción** | Recomendaciones filtradas solo por clima |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/recomendaciones/ubicacion?latitud=X&longitud=Y&radio=Z`

| Campo | Valor |
|-------|-------|
| **Descripción** | Recomendaciones por proximidad geográfica |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

#### `GET /api/recomendaciones/historial?usuarioId=X`

| Campo | Valor |
|-------|-------|
| **Descripción** | Recomendaciones basadas en historial del usuario |
| **Respuesta** | `200 - List<ProductoDTO>` |
| **Autenticación** | No requerida |

---

### Módulo: Subida de archivos (`/api/upload`)

#### `POST /api/upload`

| Campo | Valor |
|-------|-------|
| **Descripción** | Subir archivo de imagen (productos). Se guarda en `uploads/productos/` con nombre UUID |
| **Body** | `multipart/form-data` con campo `archivo` |
| **Extensiones permitidas** | jpg, jpeg, png, webp, gif |
| **Tamaño máximo** | 5 MB |
| **Respuesta éxito** | `200 - { url: "/uploads/productos/uuid.ext", nombreOriginal: "..." }` |
| **Respuesta error** | `400 - Tipo no permitido` / `400 - Excede tamaño máximo` |
| **Autenticación** | Requiere JWT |

---

## 8. Seguridad

### JWT (JSON Web Token)

| Aspecto | Detalle |
|---------|---------|
| **Clase** | `JwtProvider.java` en `backend/src/main/java/com/marketplace/pacccioli/security/` |
| **Algoritmo** | HMAC-SHA256 (`Keys.hmacShaKeyFor`) |
| **Clave secreta** | `miClaveSecretaParaFirmarTokensJWT2024PlataformaComercioLocal` (configurable en `application.properties` vía `jwt.secret`) |
| **Expiración** | 24 horas (configurable vía `jwt.expiration`) |
| **Claims estándar** | `sub` (usuarioId como String), `iat` (issued at), `exp` (expiration) |
| **Claims personalizados** | `email`, `rol` |
| **Métodos públicos** | `generateToken(usuarioId, email, rol)` → String, `validateToken(token)` → boolean, `extractUsuarioId(token)` → Long, `extractEmail(token)` → String, `extractRol(token)` → String |
| **Librería** | `io.jsonwebtoken:jjwt:0.12.3` (API + Impl + Jackson) |

### Spring Security

| Aspecto | Detalle |
|---------|---------|
| **Clase** | `SecurityConfig.java` |
| **CSRF** | Deshabilitado (API REST stateless) |
| **CORS** | Habilitado con configuración personalizada (`CorsConfig.java`) |
| **HTTP Basic** | Deshabilitado |
| **Form Login** | Deshabilitado |
| **Reglas** | `OPTIONS /**` → permitido, `/uploads/**` → permitido, `/api/**` → permitido, cualquier otra → autenticado |
| **Cadena de filtros** | `JwtFilter` se ejecuta ANTES de `UsernamePasswordAuthenticationFilter` |

### Filtros

| Filtro | Detalle |
|--------|---------|
| **Clase** | `JwtFilter.java` |
| **Tipo** | `OncePerRequestFilter` (se ejecuta una vez por petición) |
| **Rutas procesadas** | Solo rutas que comienzan con `/api/` |
| **Extracción del token** | Header `Authorization` con formato `Bearer <token>` |
| **Validación** | Si el token es válido, extrae usuarioId, email y rol y los agrega como atributos del request (`usuarioId`, `email`, `rol`) |
| **Contexto de seguridad** | Crea `UsernamePasswordAuthenticationToken` con `ROLE_{rol}` como autoridad |
| **Manejo de errores** | Captura excepciones y retorna `500` con JSON de error |

### Roles y Permisos

| Rol | Acceso |
|-----|--------|
| **CLIENTE** | Navegar productos/comercios, gestionar carrito, crear pedidos, hacer reservas, ver perfil, ver historial |
| **COMERCIANTE** | Todo lo de CLIENTE + crear/editar/eliminar productos, ver pedidos recibidos, gestionar reservas, panel comerciante |
| **ADMIN** | Acceso completo. Puede ver/administrar todos los comercios y usuarios |

### Protección de endpoints

La protección se maneja a nivel de código en cada controller:

```java
// Ejemplo: ProductoController.java
Long usuarioId = (Long) request.getAttribute("usuarioId");
String rol = (String) request.getAttribute("rol");
if (usuarioId == null || !"COMERCIANTE".equals(rol)) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)...;
}
```

No se usan anotaciones `@PreAuthorize` ni `@Secured`. La autorización se verifica manualmente en cada método que requiere acceso restringido.

---

## 9. Flujo del Sistema

### Flujo de una petición completa

```
CLIENTE (React)
    │
    │  1. Interacción del usuario (clic en "Agregar al carrito")
    ▼
COMPONENTE REACT (ProductCard.jsx)
    │
    │  2. Llama a carritoService.agregarItem(productoId, cantidad)
    ▼
SERVICIO FRONTEND (carritoService.js)
    │
    │  3. Crea objeto { productoId, cantidad }
    │  4. Token JWT se inyecta automáticamente en el interceptor de Axios
    ▼
AXIOS (api.js)
    │
    │  5. POST http://localhost:8080/api/carrito/items
    │  6. Header: Authorization: Bearer <token>
    │  7. Header: Content-Type: application/json
    │
    │  ──────── RED ────────
    │
    ▼
JWT FILTER (JwtFilter.java)
    │
    │  8. Extrae token del header "Authorization"
    │  9. Valida token con JwtProvider.validateToken()
    │ 10. Extrae usuarioId, email, rol
    │ 11. Agrega atributos al request: usuarioId, email, rol
    │ 12. Establece SecurityContext con ROLE_{rol}
    ▼
CONTROLLER (CarritoController.java)
    │
    │ 13. @PostMapping("/api/carrito/items")
    │ 14. Obtiene usuarioId del request attribute
    │ 15. Parsea productoId y cantidad del body
    │ 16. Llama a carritoService.agregarItem()
    ▼
SERVICE (CarritoService.java)
    │
    │ 17. @Transactional
    │ 18. Busca/crea carrito del usuario (obtenerOcrearCarrito)
    │ 19. Verifica que el producto exista y tenga stock
    │ 20. Busca si ya existe item en el carrito
    │     ├─ Si existe: incrementa cantidad
    │     └─ Si no existe: crea nuevo CarritoItem
    │ 21. Verifica stock suficiente
    │ 22. Guarda en BD
    │ 23. Convierte a CarritoItemDTO
    ▼
REPOSITORY (CarritoItemRepository.java)
    │
    │ 24. carritoItemRepository.save(item) → INSERT/UPDATE SQL
    ▼
BASE DE DATOS (MySQL)
    │
    │ 25. Ejecuta SQL generado por Hibernate
    │ 26. Retorna registro insertado/actualizado
    ▼
RESPUESTA (viaje de vuelta)
    │
    │ 27. Repository retorna entidad persistida
    │ 28. Service retorna CarritoItemDTO
    │ 29. Controller construye ApiResponseDTO<CarritoItemDTO>
    │ 30. Spring serializa a JSON
    │ 31. HTTP 201 Created
    │
    │  ──────── RED ────────
    │
    ▼
AXIOS (api.js)
    │
    │ 32. Interceptor de respuesta transforma response.data
    │ 33. Si es 401, limpia token y redirige a login
    ▼
COMPONENTE REACT (Carrito.jsx)
    │
    │ 34. Recibe { exito: true, mensaje: "Item agregado", datos: {...} }
    │ 35. Actualiza estado del carrito en UI
    ▼
USUARIO
    │
    │ 36. Ve el item agregado en el carrito
    ▼
```

### Flujo específico: Login

```
Login.jsx                                   AuthController.java
    │                                            │
    │  POST /api/auth/login                      │
    │  { email, contrasena }                     │
    ├───────────────────────────────────────────►│
    │                                            │
    │                                            ├─ Busca usuario por email
    │                                            ├─ Verifica BCrypt password
    │                                            ├─ Verifica usuario activo
    │                                            ├─ Genera JWT token
    │                                            │
    │  { token, tipo, usuarioId, nombre,         │
    │    email, rol }                            │
    │◄───────────────────────────────────────────┤
    │                                            │
    ├─ Guarda token en localStorage             │
    ├─ Guarda datos de usuario en localStorage  │
    └─ Redirige al Feed de Recomendaciones       │
```

### Flujo específico: Recomendaciones Híbridas

```
FeedRecomendaciones.jsx                   RecomendacionController.java
    │                                            │
    │  GET /api/recomendaciones                  │
    │  ?usuarioId=X&latitud=Y&longitud=Z         │
    ├───────────────────────────────────────────►│
    │                                            │
    │                    MotorRecomendacionService.java
    │                                            │
    │                  ┌─────────────────────────┤
    │                  │                         │
    │    Usuario ──────┤  Peso 0.3 (clima)       │
    │    Ubicación ────┤  Peso 0.3 (ubicación)   │──► Puntaje total
    │    Historial ────┤  Peso 0.4 (historial)   │
    │                  │                         │
    │                  └─────────────────────────┤
    │                                            │
    │  List<ProductoDTO> ordenado por relevancia │
    │◄───────────────────────────────────────────┤
    │                                            │
    └─ Muestra tarjetas de productos             │
```

---

## 10. Frontend

### Componentes

| Componente | Archivo | Función |
|------------|---------|---------|
| **Header** | `Header.jsx` | Barra de navegación superior con logo, buscador, widget climático, indicador de ubicación, carrito con conteo, menú de usuario y menú responsive móvil |
| **Login** | `Login.jsx` | Formulario de inicio de sesión y registro con tabs, animaciones, selección de rol (CLIENTE/COMERCIANTE), validación de campos |
| **FeedRecomendaciones** | `FeedRecomendaciones.jsx` | Feed principal con recomendaciones inteligentes. Muestra tarjetas de productos con indicador climático, filtros por condición, botón de reserva |
| **ProductCard** | `ProductCard.jsx` | Tarjeta individual de producto con imagen, precio, nombre, stock, comercio, botón carrito |
| **Carrito** | `Carrito.jsx` | Vista del carrito de compras con lista de items, cantidades, subtotales, botón de checkout |
| **Checkout** | `Checkout.jsx` | Proceso de pago con selección de método (EFECTIVO/YAPE), generación de código YAPE, confirmación |
| **HistorialPedidos** | `HistorialPedidos.jsx` | Lista de pedidos del usuario con estados, métodos de pago, totales |
| **MisReservas** | `MisReservas.jsx` | Lista de reservas del usuario con estados y opción de cancelar |
| **ModalReserva** | `ModalReserva.jsx` | Modal para crear una reserva de producto con cantidad y notas |
| **Perfil** | `Perfil.jsx` | Perfil de usuario con edición de nombre, ubicación, preferencias, cambio de rol, eliminación de cuenta |
| **PanelComerciante** | `PanelComerciante.jsx` | Panel de administración para comerciantes con formulario de producto y tabla de inventario |
| **FormularioProducto** | `FormularioProducto.jsx` | Formulario para crear/editar producto con nombre, descripción, precio, stock, categoría, etiquetas, imagen |
| **TablaInventario** | `TablaInventario.jsx` | Tabla de productos del comercio con edición y eliminación |
| **GestionPedidos** | `GestionPedidos.jsx` | Gestión de pedidos recibidos (para comerciantes) con cambio de estado |
| **GestionReservas** | `GestionReservas.jsx` | Gestión de reservas recibidas (para comerciantes) con opciones de completar/cancelar |

### Páginas

No hay páginas separadas con React Router. El enrutamiento se maneja por estado en `App.jsx`:

```jsx
const [vistaActual, setVistaActual] = useState('inicio');
// Valores posibles: inicio, comerciante, perfil, login, carrito, checkout, pedidos, reservas, gestion-pedidos, gestion-reservas
```

### Layouts

- **Header.jsx**: Actúa como layout principal. Contiene navegación, clima, ubicación, carrito y menú de usuario
- **footer**: Definido inline en `App.jsx` con texto "© 2024 Mercado Local Punata"

### Hooks

No se encontraron hooks personalizados. Se usan hooks nativos de React:
- `useState`
- `useEffect`
- `useCallback`
- `useMemo`

### Context

No se encontró implementación de Context API. El estado global se maneja mediante props desde `App.jsx`.

### Rutas

El enrutamiento es por estado (no hay React Router):

| Estado | Componente renderizado | Descripción |
|--------|----------------------|-------------|
| `inicio` | `FeedRecomendaciones` | Página principal con recomendaciones |
| `comerciante` | `PanelComerciante` | Panel de gestión para comerciantes |
| `perfil` | `Perfil` | Perfil de usuario |
| `login` | `Login` | Inicio de sesión / registro |
| `carrito` | `Carrito` | Carrito de compras |
| `checkout` | `Checkout` | Proceso de pago |
| `pedidos` | `HistorialPedidos` | Historial de pedidos |
| `reservas` | `MisReservas` | Reservas del usuario |
| `gestion-pedidos` | `GestionPedidos` | Gestión de pedidos (comerciante) |
| `gestion-reservas` | `GestionReservas` | Gestión de reservas (comerciante) |

### Servicios

| Servicio | Archivo | Métodos principales |
|----------|---------|-------------------|
| **api** | `api.js` | Configuración Axios: `baseURL`, interceptores para JWT y manejo de errores 401 |
| **authService** | `authService.js` | `login()`, `registro()`, `logout()`, `verificarEmail()`, `obtenerUsuarioActual()`, `eliminarCuenta()`, `obtenerPerfil()`, `actualizarPerfil()`, `cambiarRol()`, `estaAutenticado()` |
| **carritoService** | `carritoService.js` | `obtenerCarrito()`, `contarItems()`, `agregarItem()`, `actualizarCantidad()`, `eliminarItem()`, `limpiarCarrito()` |
| **climaService** | `climaService.js` | `obtenerClimaPorCoordenadas()`, `obtenerClimaPorCiudad()`, `obtenerPronostico()` |
| **comerciosService** | `comerciosService.js` | `obtenerTodos()`, `obtenerPorId()`, `buscar()`, `obtenerCercanos()`, `obtenerMejores()`, `misComercios()` |
| **geolocalizacionService** | `geolocalizacionService.js` | `obtenerUbicacionActual()` (navigator.geolocation) |
| **pedidosService** | `pedidosService.js` | `crearPedido()`, `obtenerPedidos()`, `obtenerPedido()`, `actualizarEstado()`, `generarCodigoYape()`, `confirmarPagoYape()`, `obtenerPedidosComercio()` |
| **productosService** | `productosService.js` | `obtenerTodos()`, `obtenerPorId()`, `buscar()`, `obtenerPopulares()`, `obtenerCalificados()`, `obtenerPorComercio()`, `crear()`, `actualizar()`, `eliminar()` |
| **recomendacionesService** | `recomendacionesService.js` | `obtenerRecomendaciones()`, `recomendarPorClima()`, `recomendarPorUbicacion()`, `recomendarPorHistorial()` |
| **reservasService** | `reservasService.js` | `crearReserva()`, `obtenerReservas()`, `obtenerReserva()`, `cancelarReserva()`, `completarReserva()`, `obtenerReservasComercio()` |

---

## 11. Backend

### Controllers

| Controller | Ruta base | Métodos (endpoints) |
|------------|-----------|---------------------|
| **AuthController** | `/api/auth` | `POST /login`, `POST /registro`, `POST /logout`, `POST /verificar-email` |
| **UsuarioController** | `/api/usuarios` | `GET /`, `GET /{id}`, `GET /email/{email}`, `GET /rol/{rol}`, `PUT /{id}`, `DELETE /cuenta`, `PUT /{id}/rol` |
| **ProductoController** | `/api/productos` | `GET /`, `GET /{id}`, `GET /buscar`, `GET /comercio/{id}`, `GET /populares`, `GET /calificados`, `POST /`, `PUT /{id}`, `DELETE /{id}` |
| **ComercioController** | `/api/comercios` | `GET /`, `GET /{id}`, `GET /buscar`, `GET /categoria/{cat}`, `GET /cercanos`, `GET /mis-comercios`, `GET /mejores`, `GET /buscar-avanzado` |
| **PedidoController** | `/api/pedidos` | `POST /`, `GET /`, `GET /{id}`, `PUT /{id}/estado`, `POST /{id}/pago/yape/generar`, `POST /{id}/pago/yape/confirmar`, `GET /comercio` |
| **CarritoController** | `/api/carrito` | `GET /`, `GET /conteo`, `POST /items`, `PUT /items/{id}`, `DELETE /items/{id}`, `DELETE /` |
| **ReservaController** | `/api/reservas` | `POST /`, `GET /`, `GET /{id}`, `PUT /{id}/cancelar`, `PUT /{id}/completar`, `GET /comercio` |
| **InteraccionController** | `/api/interacciones` | `POST /`, `GET /usuario/{id}`, `GET /producto/{id}`, `GET /tipo/{tipo}`, `GET /usuario/{id}/compras`, `GET /estadisticas/producto/{id}` |
| **ClimaController** | `/api/clima` | `GET /`, `GET /ciudad`, `GET /pronostico` |
| **RecomendacionController** | `/api/recomendaciones` | `GET /`, `GET /clima`, `GET /ubicacion`, `GET /historial` |
| **UploadController** | `/api/upload` | `POST /` |

### Services

| Service | Propósito | Métodos principales |
|---------|-----------|---------------------|
| **CarritoService** | Lógica del carrito de compras | `obtenerOcrearCarrito()`, `agregarItem()`, `actualizarCantidad()`, `eliminarItem()`, `limpiarCarrito()`, `obtenerCarrito()`, `contarItems()` |
| **PedidoService** | Creación y gestión de pedidos | `crearPedidoDesdeCarrito()`, `obtenerPedidosUsuario()`, `obtenerPedido()`, `actualizarEstado()`, `generarCodigoPagoYape()`, `confirmarPagoYape()`, `obtenerPedidosComerciante()` |
| **ReservaService** | Gestión de reservas de productos | `crearReserva()`, `cancelarReserva()`, `completarReserva()`, `obtenerReservasUsuario()`, `obtenerReservasComercio()`, `obtenerReserva()` |
| **MotorRecomendacionService** | Motor de recomendaciones híbrido | `generarRecomendaciones()`, `recomendarPorClima()`, `recomendarPorHistorial()`, `recomendarPorUbicacion()` |
| **ClimaService** | Integración con OpenWeather API | `obtenerClimaPorCoordenadas()`, `obtenerClimaPorCiudad()`, `obtenerPronostico()`, `esClimaFavorable()` |
| **GeolocalizacionService** | Cálculos geográficos | `calcularDistancia()` (Haversine), `calcularTiempoViaje()`, `estaEnRadio()`, `calcularAcimut()`, `obtenerDireccionCardinal()` |

### Repositories

| Repository | Entidad | Métodos personalizados |
|------------|---------|----------------------|
| **UsuarioRepository** | Usuario | `findByEmailIgnoreCase()`, `existsByEmail()`, `findByRolAndActivoTrue()` |
| **ProductoRepository** | Producto | `findByNombreContainingIgnoreCase()`, `findByComercioId()`, `findByComercioIdAndActivoTrue()`, `findByActivoTrueOrderByConteoVisualizacionesDesc()`, `findByActivoTrueOrderByCalificacionPromedioDesc()` |
| **ComercioRepository** | Comercio | `findByActivoTrue()`, `findByNombreContainingIgnoreCase()`, `findByCategoriaAndActivoTrue()`, `findByPropietarioId()`, `findByActivoTrueOrderByCalificacionDesc()`, `buscarComerciosCercanos()` (native query Haversine), `buscarComerciosAvanzado()` (native query) |
| **PedidoRepository** | Pedido | `findByUsuarioIdOrderByFechaCreacionDesc()`, `findByComercianteId()` (JPQL) |
| **PedidoItemRepository** | PedidoItem | - |
| **CarritoRepository** | Carrito | `findByUsuarioId()` |
| **CarritoItemRepository** | CarritoItem | `findByCarritoIdAndProductoId()`, `deleteByCarritoId()` |
| **ReservaRepository** | Reserva | `findByUsuarioIdOrderByFechaReservaDesc()`, `findByComercioIdOrderByFechaReservaDesc()`, `countByProductoIdAndEstado()` |
| **InteraccionRepository** | Interaccion | `findByUsuarioId()`, `findByProductoId()`, `findByTipoInteraccion()`, `findByUsuarioIdAndTipoInteraccion()`, `countByProductoIdAndTipoInteraccion()` |

### DTOs

| DTO | Propósito | Campos principales |
|-----|-----------|-------------------|
| **ApiResponseDTO\<T\>** | Envoltorio estándar de respuesta | `exito`, `mensaje`, `datos`, `errores`, `timestamp` |
| **UsuarioDTO** | Datos del usuario (sin contraseña) | `id`, `nombre`, `email`, `rol`, `latitud`, `longitud`, `radioBusquedaKm`, `preferencias`, `activo` |
| **ProductoDTO** | Datos del producto + comercio | `id`, `nombre`, `precio`, `stock`, `categoria`, `estado`, `comercioId`, `nombreComercio`, `etiquetasInteligentes`, `latitud/longitud` del comercio |
| **ComercioDTO** | Datos del comercio + propietario | `id`, `nombre`, `categoria`, `latitud`, `longitud`, `calificacionPromedio`, `propietarioId`, `nombrePropietario` |
| **PedidoDTO** | Pedido completo con items | `id`, `estado`, `total`, `metodoPago`, `items` (List\<PedidoItemDTO\>), `fechaCreacion` |
| **PedidoItemDTO** | Item de pedido | `productoId`, `nombreProducto`, `precioUnitario`, `cantidad`, `subtotal`, `urlImagen` |
| **CarritoDTO** | Carrito con items y totales | `id`, `items` (List\<CarritoItemDTO\>), `totalItems`, `subtotal` |
| **CarritoItemDTO** | Item de carrito con datos de producto | `productoId`, `nombreProducto`, `precioUnitario`, `cantidad`, `subtotal`, `nombreComercio`, `stockDisponible` |
| **ReservaDTO** | Reserva con datos de usuario/producto/comercio | `id`, `usuarioId`, `productoId`, `comercioId`, `cantidad`, `estado`, `fechaExpiracion` |
| **InteraccionDTO** | Interacción con contexto | `usuarioId`, `productoId`, `tipo`, `fuente`, `latitudUsuario`, `climaContexto`, `fecha` |
| **LoginRequestDTO** | Petición de login/registro | `email`, `contrasena`, `rol` (validados con Jakarta Validation) |
| **LoginResponseDTO** | Respuesta de login/registro | `token`, `tipo`, `usuarioId`, `nombre`, `email`, `rol` |

### Config

| Clase | Propósito |
|-------|-----------|
| **WebConfig** | Configura `ResourceHandler` para servir archivos estáticos desde `uploads/` |
| **DataSeeder** | `CommandLineRunner` que inserta datos de prueba (5 usuarios + 4 comercios) si la BD está vacía |

### Utilidades

No se encontraron clases de utilidad separadas. La funcionalidad auxiliar está incluida dentro de las clases que la usan:
- `JwtProvider`: utilidad para JWT
- `GeolocalizacionService`: cálculos geográficos (Haversine)
- `ClimaService` interno: parseo de respuestas OpenWeather, `generarClimaSimulado()` como fallback

---

## 12. Integraciones

### OpenWeather API

| Aspecto | Detalle |
|---------|---------|
| **URL base** | `https://api.openweathermap.org/data/2.5/weather` |
| **URL pronóstico** | `https://api.openweathermap.org/data/2.5/forecast` |
| **API Key** | `83b13a49fe1a0deb82bc51e5adb16413` (en `application-local.properties`) |
| **Librería** | `RestTemplate` (Spring) |
| **Método** | GET |
| **Parámetros** | `lat`, `lon`, `appid`, `units=metric`, `lang=es` |
| **Respuesta** | JSON con `main.temp`, `main.feels_like`, `main.humidity`, `weather[0].description`, `weather[0].icon`, `wind.speed`, `visibility`, `name` (ciudad), `sys.country` |
| **Fallback** | Si no hay API key o falla la conexión, genera datos simulados (18°C, parcialmente nublado, Punata, Bolivia) |

**Endpoints que la consumen:**
- `GET /api/clima?latitud=X&longitud=Y` — Clima actual por coordenadas
- `GET /api/clima/ciudad?ciudad=Nombre` — Clima por ciudad
- `GET /api/clima/pronostico?latitud=X&longitud=Y&dias=N` — Pronóstico (máx 5 días)
- `MotorRecomendacionService` — Internamente para recomendaciones por clima

**Información recibida:**
- Temperatura actual (°C)
- Sensación térmica
- Humedad (%)
- Presión atmosférica (hPa)
- Descripción de condición climática
- Icono del clima
- Velocidad del viento (convertida a km/h)
- Visibilidad (metros)
- Nombre de ciudad y país

### Google Maps API (Pendiente)

| Aspecto | Detalle |
|---------|---------|
| **Estado** | **NO implementada** - placeholder solamente |
| **API Key** | `google.maps.api.key` en `application.properties` (vacío) |
| **Uso planeado** | Geocodificación (dirección → coordenadas) y geocodificación inversa (coordenadas → dirección) |
| **Código placeholder** | `GeolocalizacionService.obtenerCoordenadasPorDireccion()` y `obtenerDireccionPorCoordenadas()` retornan `null` con TODO comentado |

### Leaflet (Frontend)

Se menciona en el contexto general pero no se encontró implementación activa en los archivos fuente del frontend. No hay importación ni uso de Leaflet en los componentes analizados.

### Navigator Geolocation API (Frontend)

| Aspecto | Detalle |
|---------|---------|
| **Uso** | Obtener ubicación actual del usuario para recomendaciones y clima |
| **Método** | `navigator.geolocation.getCurrentPosition()` |
| **Servicio** | `geolocalizacionService.js` → `obtenerUbicacionActual()` |
| **Retorno** | `{ latitud, longitud }` (coordenadas del navegador) |

---

## 13. Instalación

### Requisitos

| Requisito | Versión | Descarga |
|-----------|---------|----------|
| Java JDK | 21+ | https://adoptium.net/ |
| Maven | 3.6+ | https://maven.apache.org/download.cgi |
| Node.js | 18+ | https://nodejs.org/ |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/ |
| Git | Cualquiera | https://git-scm.com/ |

### Clonar repositorio

```bash
git clone <url-repositorio>
cd marketplace-pacccioli
```

### Crear base de datos MySQL

```sql
CREATE DATABASE marketplace_pacccioli CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

O configurar `spring.datasource.url` con `createDatabaseIfNotExist=true` (ya incluido) para que Hibernate lo cree automáticamente.

### Configurar variables de entorno (opcional)

Crear archivo `backend/src/main/resources/application-local.properties`:

```properties
openweather.api.key=TU_API_KEY_OPENWEATHER
```

### Levantar Backend

```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

El backend se iniciará en `http://localhost:8080`.

**Alternativa:** Ejecutar el JAR directamente:

```bash
cd backend
mvn clean package -DskipTests
java -jar target/marketplace-pacccioli-1.0.0.jar
```

### Levantar Frontend

```bash
cd frontend
npm install
npm run dev
```

El frontend se iniciará en `http://localhost:3000`.

### Verificar instalación

1. Backend funcionando: `http://localhost:8080/api/productos` debería retornar JSON
2. Frontend funcionando: `http://localhost:3000` debería mostrar la aplicación
3. El proxy de Vite redirige `/api/*` a `http://localhost:8080`

---

## 14. Compilación

### Backend (JAR)

```bash
cd backend
mvn clean package
```

Genera `backend/target/marketplace-pacccioli-1.0.0.jar`

Para omitir tests:

```bash
mvn clean package -DskipTests
```

### Frontend (Build estático)

```bash
cd frontend
npm run build
```

Genera carpeta `frontend/dist/` con archivos HTML, CSS, JS estáticos.

Para previsualizar el build:

```bash
npm run preview
```

### PWA

El build de frontend incluye:
- Service worker generado por Workbox (`vite-plugin-pwa`)
- Manifiesto PWA (`manifest.json`)
- Cache de API de OpenWeather (NetworkFirst, 24h)
- Cache de API local (`/api/`, NetworkFirst, 5 min)
- Instalable como aplicación standalone

---

## 15. Despliegue

### Opción 1: Backend + Frontend separados

**Backend:**
```bash
cd backend
mvn clean package -DskipTests
java -jar target/marketplace-pacccioli-1.0.0.jar --server.port=8080
```

**Frontend** (servir archivos estáticos con Nginx o similar):
```bash
cd frontend
npm run build
# Copiar contenido de dist/ al directorio web del servidor
```

Configurar Nginx para redirigir `/api/` al backend:

```nginx
server {
    listen 80;
    server_name midominio.com;
    root /var/www/marketplace/dist;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /uploads/ {
        proxy_pass http://localhost:8080;
    }
}
```

### Opción 2: JAR único con frontend embebido

No se encontró configuración para embeber el frontend dentro del JAR de Spring Boot. Requeriría copiar `frontend/dist/` a `backend/src/main/resources/static/` y recompilar.dw

### Configuración de producción

| Variable | Valor recomendado |
|----------|-------------------|
| `spring.datasource.url` | URL de la BD de producción |
| `spring.datasource.username` | Usuario de BD |
| `spring.datasource.password` | Contraseña de BD |
| `jwt.secret` | Clave secreta larga y segura (mínimo 256 bits) |
| `jwt.expiration` | 86400000 (24h) |
| `openweather.api.key` | API key de OpenWeather |
| `server.port` | 8080 (o 80 con proxy) |
| `spring.jpa.show-sql` | `false` en producción |
| `spring.jpa.hibernate.ddl-auto` | `update` o usar migraciones |

---

## 16. Posibles Errores

| Error | Causa | Solución |
|-------|-------|----------|
| **`java.sql.SQLNonTransientConnectionException: Public Key Retrieval is not allowed`** | MySQL 8+ requiere `allowPublicKeyRetrieval=true` | Ya incluido en URL. Si persiste, agregar `&useSSL=false` |
| **`Failed to configure a DataSource: 'url' attribute is not specified`** | MySQL no está corriendo o credenciales incorrectas | Verificar que MySQL esté iniciado. Verificar usuario/contraseña en `application.properties` |
| **OpenWeather returns null/data simulated** | API key no configurada o inválida | Configurar `openweather.api.key` en `application-local.properties` |
| **`401 Unauthorized` en todas las peticiones** | Token JWT expirado o inválido | Hacer login nuevamente o verificar `jwt.secret` |
| **CORS bloqueado** | Frontend en puerto no permitido | Agregar el origen a `CorsConfig.java` |
| **`403 Forbidden` al crear producto** | Usuario no tiene rol COMERCIANTE | Cambiar rol en Perfil o registrarse como COMERCIANTE |
| **Error al subir archivo: "Tipo de archivo no permitido"** | Extensión no soportada | Usar jpg, jpeg, png, webp o gif |
| **Error al subir archivo: excede tamaño máximo** | Archivo > 5MB | Reducir tamaño de imagen o aumentar `spring.servlet.multipart.max-file-size` |
| **`npm install` falla** | Node.js versión incorrecta o falta de permisos | Usar Node 18+. Eliminar `node_modules` y `package-lock.json`, reintentar |
| **Puerto 8080 en uso** | Otro proceso usando el puerto | Cambiar `server.port` o detener el otro proceso |
| **Puerto 3000 en uso** | Otro proceso usando el puerto | Cambiar en `vite.config.js` `server.port` |
| **Error en login: "Credenciales inválidas"** | Email o contraseña incorrectos | Verificar credenciales. Usuarios de prueba: `juan@example.com` / `nano123` (BCrypt hash: `$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO`) |
| **DataSeeder se ejecuta siempre** | Base de datos vacía | El seeder solo se ejecuta si `usuarioRepository.count() == 0` |
| **Error de compilación: Lombok no procesa anotaciones** | Missing annotation processor | Verificar configuración de `maven-compiler-plugin` y `annotationProcessorPaths` |
| **Base de datos no se actualiza con nuevas entidades** | `ddl-auto=update` no detecta cambios | Cambiar temporalmente a `ddl-auto=create` o ejecutar script SQL manual |

---

## 17. Diagramas Sugeridos

Para incluir en el Manual Técnico completo:

| Diagrama | Descripción |
|----------|-------------|
| **Diagrama de Arquitectura del Sistema** | Muestra la comunicación entre Frontend (React + Vite) y Backend (Spring Boot + MySQL), incluyendo APIs externas (OpenWeather) |
| **Diagrama Entidad-Relación (DER)** | Diagrama completo de la base de datos con todas las tablas, campos, tipos, PKs, FKs y relaciones |
| **Diagrama de Flujo de Petición** | Secuencia React → Axios → JwtFilter → Controller → Service → Repository → BD → Respuesta |
| **Diagrama de Secuencia - Login** | Muestra el flujo Login.jsx → AuthController → JwtProvider → Respuesta con token |
| **Diagrama de Secuencia - Recomendaciones** | Muestra cómo el MotorRecomendacionService combina clima + ubicación + historial |
| **Diagrama de Componentes - Frontend** | Árbol de componentes React con relaciones y flujo de datos |
| **Diagrama de Clases - Backend** | Relaciones entre Controllers, Services, Repositories, Entities y DTOs |
| **Diagrama de Casos de Uso** | Actores (CLIENTE, COMERCIANTE, ADMIN) y sus interacciones con el sistema |
| **Diagrama de Despliegue** | Topología de red: servidores, puertos, conexiones |

---

## 18. Tablas Sugeridas

| Tabla | Contenido |
|-------|-----------|
| **Endpoints de la API REST** | Método, ruta, descripción, parámetros, autenticación requerida |
| **Entidades de Base de Datos** | Nombre de tabla, campos, tipos, restricciones, relaciones |
| **DTOs** | Nombre, campos, propósito |
| **Dependencias Backend** | Grupo, artefacto, versión, ámbito, propósito |
| **Dependencias Frontend** | Paquete, versión, tipo (dependencia/devDependencia), propósito |
| **Configuración de aplicación.properties** | Propiedad, valor, descripción |
| **Componentes Frontend** | Nombre, archivo, función, props principales |
| **Servicios Frontend** | Nombre, archivo, métodos, endpoints consumidos |
| **Controllers Backend** | Nombre, ruta base, métodos, endpoints |
| **Códigos de error HTTP** | Código, significado, escenario |
| **Métodos de pago soportados** | EFECTIVO, YAPE, TARJETA (campo MetodoPago) |
| **Estados de Pedido** | PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO |
| **Estados de Reserva** | PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA |
| **Tipos de Interacción** | VISUALIZACION, CLICK, COMPRA, FAVORITO |
| **Fuentes de Interacción** | BUSQUEDA, RECOMENDACION, DIRECTO, PROMOCION, FEED |

---

## 19. Capturas Sugeridas

| Pantalla | Componente | Propósito en el manual |
|----------|------------|----------------------|
| **Login / Registro** | `Login.jsx` | Mostrar formulario de autenticación con selección de rol |
| **Feed de Recomendaciones** | `FeedRecomendaciones.jsx` | Pantalla principal con tarjetas de productos y widget climático |
| **Header con clima** | `Header.jsx` | Barra de navegación con widget de clima, ubicación, carrito y menú |
| **Product Card** | `ProductCard.jsx` | Tarjeta individual de producto con precio, stock y botón carrito |
| **Carrito de compras** | `Carrito.jsx` | Lista de items en el carrito con cantidades y total |
| **Checkout / Pago** | `Checkout.jsx` | Proceso de pago con selección de método y código YAPE |
| **Historial de Pedidos** | `HistorialPedidos.jsx` | Lista de pedidos del usuario con estados |
| **Mis Reservas** | `MisReservas.jsx` | Lista de reservas del usuario |
| **Modal de Reserva** | `ModalReserva.jsx` | Modal para crear una reserva |
| **Perfil de Usuario** | `Perfil.jsx` | Edición de perfil, cambio de rol, preferencias |
| **Panel Comerciante** | `PanelComerciante.jsx` | Panel de administración con formulario y tabla |
| **Formulario de Producto** | `FormularioProducto.jsx` | Formulario para crear/editar producto |
| **Gestión de Pedidos (Comerciante)** | `GestionPedidos.jsx` | Pedidos recibidos con cambio de estado |
| **Gestión de Reservas (Comerciante)** | `GestionReservas.jsx` | Reservas recibidas con opciones |
| **Menú responsive móvil** | `Header.jsx` (menú hamburguesa) | Demostrar diseño mobile-first |
| **Diagrama de base de datos** | (generado externamente) | DER completo |
| **Arquitectura del sistema** | (generado externamente) | Diagrama de componentes y flujo |
| **Consola de la aplicación corriendo** | Backend + Frontend | Mostrar que el sistema funciona |
| **Postman / Insomnia - Prueba de API** | Ejemplo de petición | Demostrar consumo de endpoints |

---

*Documento generado automáticamente mediante análisis del código fuente del proyecto `marketplace-pacccioli`.*
*Versión: 1.0.0 | Fecha: Julio 2026*
