# ESPECIFICACIÓN TÉCNICA — Plataforma de Comercio Local Inteligente

---

## 1. DESCRIPCIÓN FUNCIONAL COMPLETA DEL SISTEMA

### 1.1 Qué hace el sistema

La **Plataforma de Comercio Local Inteligente** es un marketplace web que conecta compradores con comercios locales de una región geográfica delimitada (Punata, Cochabamba, Bolivia). Su principal innovación es un **motor de recomendaciones contextuales** que sugiere productos basándose en tres factores: la ubicación geográfica del usuario, las condiciones climáticas actuales y el historial de comportamiento del usuario (visualizaciones, clics, compras previas).

Además, el sistema implementa funcionalidades de comercio electrónico completas: carrito de compras persistente, gestión de pedidos, reserva de productos con vencimiento temporizado, y simulación de pagos digitales (Yape).

### 1.2 Cómo funciona

El sistema opera con una arquitectura de tres capas:

1. **Frontend (React + Vite + Tailwind CSS):** Interfaz de usuario SPA (Single Page Application) que se comunica con el backend mediante peticiones HTTP REST.
2. **Backend (Spring Boot + Java 21):** API REST que procesa la lógica de negocio, aplica seguridad JWT, coordina el motor de recomendaciones y las APIs externas.
3. **Base de datos (MySQL):** Almacenamiento persistente de usuarios, comercios, productos, interacciones, pedidos, carritos y reservas.

El flujo de datos se completa con dos APIs externas: **OpenWeather** para obtener el clima actual de la ubicación del usuario, y **Leaflet/Google Maps** para visualización cartográfica y cálculos de distancia.

### 1.3 Flujo completo desde que un usuario entra hasta que realiza una compra

1. **Ingreso:** El usuario accede a la aplicación a través del navegador.
2. **Registro/Inicio de sesión:** Si no tiene cuenta, se registra con email y contraseña. Si ya tiene, inicia sesión. El backend genera un JWT que el frontend almacena en localStorage.
3. **Geolocalización:** El frontend solicita permiso de geolocalización al navegador. Si el usuario acepta, envía sus coordenadas al backend.
4. **Obtención del clima:** El backend usa las coordenadas para consultar OpenWeather y obtener la condición climática actual (temperatura, humedad, descripción).
5. **Feed de recomendaciones:** El backend ejecuta el motor de recomendaciones:
   - Filtra productos activos con stock disponible.
   - Calcula la distancia de cada producto al usuario mediante la fórmula de Haversine.
   - Filtra productos por etiquetas inteligentes que coinciden con el clima actual (ej: "lluvia" → paraguas, "frío" → chaqueta).
   - Ponderación híbrida: 30% clima, 30% ubicación, 40% historial del usuario.
   - Ordena los productos por puntuación descendente.
6. **Exploración:** El usuario visualiza los productos recomendados en un feed con tarjetas que muestran nombre, precio, calificación, comercio y distancia.
7. **Agregar al carrito:** El usuario hace clic en "Agregar al carrito". El backend persiste el item en la tabla `carrito_items` asociada al usuario.
8. **Reserva (opcional):** Si el producto lo permite, el usuario puede reservarlo por 24 horas. El backend crea un registro en `reservas` con expiración automática.
9. **Checkout:** El usuario ingresa al carrito y procede al pago. Puede elegir entre pago contra entrega o Yape simulado.
10. **Confirmación del pedido:** El backend crea un `pedido` en estado CONFIRMADO y vacía el carrito.
11. **Seguimiento:** El usuario puede ver el estado de su pedido (CONFIRMADO → ENVIADO → ENTREGADO) desde su perfil.

### 1.4 Flujo del comerciante

1. El comerciante inicia sesión con su cuenta (rol COMERCIANTE).
2. Accede al **Panel del Comerciante** donde puede:
   - Ver sus productos listados y crear nuevos.
   - **Gestionar pedidos recibidos:** visualizar pedidos de clientes, cambiar estados (CONFIRMADO → ENVIADO → ENTREGADO).
   - **Gestionar reservas:** ver reservas activas, marcarlas como completadas.
   - **Confirmar pagos YAPE:** ver pedidos marcados como "YAPE", ingresar el código de referencia que el cliente le proporciona, y confirmar el pago.
3. Cuando un cliente hace un pedido de un producto del comerciante, este aparece automáticamente en la lista de pedidos del comerciante.

### 1.5 Flujo del administrador

1. El administrador inicia sesión con rol ADMIN.
2. Tiene acceso a un panel de administración con:
   - Gestión de usuarios (ver, activar/desactivar).
   - Gestión de comercios (ver, aprobar, suspender).
   - Reportes básicos (cantidad de usuarios, pedidos, ingresos simulados).

---

## 2. ARQUITECTURA GENERAL

### 2.1 Arquitectura MVC (Backend)

El backend sigue el patrón **Model-View-Controller**, donde la "vista" son los DTOs y respuestas JSON:

- **Model (Entity):** Clases JPA anotadas con `@Entity` que representan las tablas de la base de datos (`Usuario`, `Producto`, `Comercio`, `Pedido`, `Carrito`, etc.).
- **View (DTO):** Objetos de transferencia de datos (`LoginRequestDTO`, `ProductoDTO`, `CarritoDTO`, `ApiResponseDTO`) que serializan a JSON para las respuestas HTTP.
- **Controller:** Clases anotadas con `@RestController` que definen los endpoints REST (`AuthController`, `ProductoController`, `CarritoController`, `PedidoController`).
- **Service:** Capa intermedia con la lógica de negocio (`AuthService`, `CarritoService`, `RecomendacionService`).
- **Repository:** Interfaces Spring Data JPA que encapsulan el acceso a datos (`UsuarioRepository`, `ProductoRepository`).

### 2.2 Arquitectura REST

Todos los endpoints siguen principios RESTful:
- Uso correcto de verbos HTTP: GET (lectura), POST (creación), PUT (actualización), DELETE (eliminación).
- URLs semánticas con nombres en plural: `/api/productos`, `/api/carrito/items`.
- Respuestas JSON con estructura uniforme mediante `ApiResponseDTO<T>` que encapsula `exito`, `mensaje`, `datos`, `errores` y `timestamp`.
- Códigos de estado HTTP estándar: 200 (OK), 201 (Created), 400 (Bad Request), 401 (Unauthorized), 404 (Not Found), 500 (Internal Server Error).

### 2.3 Arquitectura en capas

```
┌──────────────────────────────────────────────────┐
│                   FRONTEND                        │
│           React + Vite + Tailwind                  │
│  (Componentes: App, Header, ProductCard, Carrito)  │
│  (Servicios: api.js, authService, carritoService)  │
└──────────────────┬───────────────────────────────┘
                   │ HTTP/JSON (Axios)
                   ▼
┌──────────────────────────────────────────────────┐
│               CAPA DE PRESENTACIÓN               │
│         Controllers (REST endpoints)              │
│  AuthController, ProductoController,              │
│  CarritoController, PedidoController,             │
│  ReservaController, ComercioController,           │
│  UsuarioController, RecomendacionController       │
├──────────────────────────────────────────────────┤
│               CAPA DE NEGOCIO                     │
│         Services (Lógica de negocio)              │
│  AuthService, CarritoService, PedidoService,      │
│  ReservaService, RecomendacionService             │
├──────────────────────────────────────────────────┤
│               CAPA DE ACCESO A DATOS             │
│         Repositories (Spring Data JPA)            │
│  UsuarioRepository, ProductoRepository,           │
│  CarritoRepository, PedidoRepository, ...          │
├──────────────────────────────────────────────────┤
│               BASE DE DATOS                       │
│                 MySQL 8.x                          │
└──────────────────────────────────────────────────┘
```

### 2.4 Diagrama de comunicación

```
Navegador Web
     │
     ├── Geolocation API (navegador) → coordenadas GPS
     │
     ▼
React App (http://localhost:5173)
     │
     ├── Axios HTTP ──────────────────→ Backend Spring Boot (http://localhost:8080)
     │                                      │
     │                                      ├── JPA/Hibernate ──→ MySQL
     │                                      │
     │                                      ├── OpenWeather API (HTTPS) → datos climáticos
     │                                      │
     │                                      └── Leaflet/Google Maps API → mapas, geocoding
     │
     └── localStorage (JWT token, datos de sesión)
```

---

## 3. TECNOLOGÍAS UTILIZADAS

| Tecnología | Versión | Propósito | Motivo de elección |
|-----------|---------|-----------|-------------------|
| **Java** | 21 | Lenguaje de programación del backend | LTS más reciente con features modernas (records, pattern matching, virtual threads). Amplio soporte empresarial. |
| **Spring Boot** | 3.2.0 | Framework de aplicación | Configuración automática, embedded Tomcat, facilitad de integración con JPA, Security, REST. Estándar industrial. |
| **Spring Security** | 6.x | Seguridad y autenticación | Integración nativa con Spring Boot, soporte para JWT, filtros personalizados, control de acceso basado en roles. |
| **JWT (jjwt)** | 0.12.3 | Tokens de autenticación | Stateless, escalable (no requiere sesión en servidor), estándar abierto (RFC 7519), fácil de verificar en frontend. |
| **Hibernate** | 6.3.1 (ORM) | Mapeo objeto-relacional | Implementación JPA más madura, caché de segundo nivel, lazy loading, soporte de herencia y consultas JPQL. |
| **Spring Data JPA** | 3.x | Capa de persistencia | Repositorios automáticos, consultas derivadas de nombres de métodos, paginación integrada. |
| **React** | 18 | Biblioteca de interfaz de usuario | Arquitectura basada en componentes, virtual DOM para rendimiento, ecosistema extenso, hooks para estado. |
| **Vite** | 5.x | Bundler y dev server | Extremadamente rápido (ESBuild para bundling), HMR instantáneo, configuración mínima. |
| **Tailwind CSS** | 3.x | Framework de estilos | Utilidades first, no escribe CSS personalizado, build final pequeño (purga de clases no usadas), diseño responsivo rápido. |
| **Axios** | 1.x | Cliente HTTP para frontend | API basada en promesas, interceptores (para agregar JWT automáticamente), manejo de errores consistente. |
| **MySQL** | 8.x | Base de datos relacional | Gratuito, rendimiento excelente, ampliamente soportado en entornos de hosting compartido, funciones JSON nativas. |
| **Maven** | 3.x | Gestión de dependencias | Estándar en proyectos Spring, gestión declarativa (pom.xml), resolución transitiva de dependencias. |
| **OpenWeather API** | — | Datos climáticos | API gratuita con límite generoso (60 llamadas/min), datos precisos, respuesta JSON simple. |
| **Leaflet** | — | Mapas interactivos | Ligero (38KB), código abierto, sin clave API requerida, integración con OpenStreetMap. |
| **Lombok** | — | Reducción de boilerplate | Anotaciones `@Data`, `@Slf4j`, `@RequiredArgsConstructor` eliminan getters/setters/constructores manuales. |
| **BCrypt** | — | Hash de contraseñas | Algoritmo adaptativo (costo configurable), resistente a ataques de rainbow tables, sal automática. |

---

## 4. MÓDULOS DEL SISTEMA

### 4.1 Autenticación (`/api/auth`)
- Registro de usuarios (CLIENTE o COMERCIANTE).
- Inicio de sesión con email y contraseña.
- Generación y validación de JWT.
- Verificación de disponibilidad de email.

### 4.2 Usuarios (`/api/usuarios`)
- CRUD de usuarios.
- Perfil con datos personales, preferencias, coordenadas.
- Actualización de ubicación y radio de búsqueda.

### 4.3 Comercios (`/api/comercios`)
- CRUD de comercios.
- Búsqueda por nombre, categoría, ubicación (Haversine).
- Comercios cercanos geolocalizados.
- Calificación y reseñas.

### 4.4 Productos (`/api/productos`)
- CRUD de productos.
- Búsqueda por nombre, comercio, etiquetas inteligentes.
- Filtros por stock, precio, categoría.
- Productos por comercio.

### 4.5 Categorías
- Enumeraciones de categorías (Producto.Categoria, Comercio.Categoria).
- Filtrado de productos y comercios por categoría.

### 4.6 Motor de recomendaciones (`/api/recomendaciones`)
- Algoritmo híbrido (clima + ubicación + historial).
- Consulta a OpenWeather API para clima actual.
- Cálculo de distancias con fórmula de Haversine.
- Ponderación configurable (pesos: clima=0.3, ubicación=0.3, historial=0.4).
- Fallback robusto: si falla OpenWeather, recomienda solo por ubicación e historial.
- Si falla la geolocalización, recomienda solo por clima o aleatorio.

### 4.7 Ubicación
- Captura de coordenadas del usuario vía Geolocation API del navegador.
- Almacenamiento de latitud/longitud en usuarios y comercios.
- Cálculo de distancias con query nativa Haversine.

### 4.8 Clima
- Consulta a OpenWeather API usando coordenadas.
- Mapeo de condiciones climáticas a etiquetas inteligentes (lluvia, frío, calor, soleado).
- Almacenamiento temporal de datos climáticos para reducir llamadas API.

### 4.9 Carrito de compras (`/api/carrito`)
- Carrito persistente por usuario (tabla `carritos`, uno a uno con usuario).
- Items del carrito (tabla `carrito_items`).
- Agregar, actualizar cantidad, eliminar items.
- Limpiar carrito después de compra.
- Conteo de items (badge en Header).

### 4.10 Pedidos (`/api/pedidos`)
- Creación de pedido desde carrito.
- Estados: CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO.
- Items del pedido (tabla `pedido_items`).
- Historial de pedidos por usuario.
- Gestión de pedidos por comerciante.
- Método de pago: CONTRA_ENTREGA, YAPE.
- Referencia Yape para verificación.

### 4.11 Reservas (`/api/reservas`)
- Reserva de producto con vencimiento a 24 horas.
- Estados: ACTIVA, COMPLETADA, EXPIRADA, CANCELADA.
- Expiración automática (validada en servicio).
- Límite de stock durante la reserva.
- Gestión por comerciante.

### 4.12 Favoritos
- Botón de corazón en ProductCard (actualmente solo logging, preparado para implementación futura).

### 4.13 Historial
- Interacciones del usuario: VISUALIZACION, CLICK, COMPRA.
- Alimenta el motor de recomendaciones.
- Registro de contexto (ubicación, precio, clima).

### 4.14 Reportes
- Panel de administración con conteos básicos.
- Reporte de pedidos por estado.
- Reporte de productos más vistos.

### 4.15 Administración
- Panel de administrador con gestión de usuarios y comercios.
- Visibilidad de todo el sistema.

### 4.16 Notificaciones
- Mensajes toast en frontend para feedback de acciones.
- Preparado para expandir a notificaciones push o email.

---

## 5. TIPOS DE USUARIOS

### 5.1 ADMIN
- **Descripción:** Superadministrador del sistema.
- **Permisos:**
  - Gestionar todos los usuarios (crear, activar, desactivar).
  - Gestionar todos los comercios (aprobar, suspender).
  - Ver reportes globales.
  - Acceso total a todas las APIs.

### 5.2 COMERCIANTE
- **Descripción:** Dueño o administrador de uno o más comercios.
- **Permisos:**
  - Gestionar sus propios productos (CRUD).
  - Ver pedidos recibidos (productos de sus comercios).
  - Cambiar estado de pedidos (CONFIRMADO → ENVIADO → ENTREGADO).
  - Gestionar reservas de sus productos.
  - Confirmar pagos Yape.
  - Acceso al Panel del Comerciante.

### 5.3 CLIENTE
- **Descripción:** Comprador final.
- **Permisos:**
  - Ver feed de recomendaciones personalizado.
  - Explorar productos y comercios.
  - Agregar productos al carrito.
  - Realizar pedidos (checkout).
  - Reservar productos (si aplica).
  - Ver historial de pedidos.
  - Ver historial de reservas.
  - Gestionar su perfil.

---

## 6. CASOS DE USO

### CU-001: Registro de usuario
- **Nombre:** Registrar nuevo usuario
- **Descripción:** El usuario crea una cuenta en el sistema proporcionando email, contraseña y rol.
- **Actor:** Visitante (no autenticado)
- **Precondiciones:** El email no debe estar registrado previamente.
- **Postcondiciones:** Se crea un nuevo usuario en estado activo. Si el rol es COMERCIANTE, se crea automáticamente un comercio por defecto. Se genera y devuelve un JWT.
- **Flujo principal:**
  1. El usuario completa el formulario de registro (email, contraseña, rol opcional).
  2. El frontend envía POST `/api/auth/registro`.
  3. El backend valida que el email no exista.
  4. El backend hashea la contraseña con BCrypt.
  5. El backend guarda el usuario en la base de datos.
  6. Si el rol es COMERCIANTE, crea un comercio por defecto.
  7. El backend genera un JWT y lo devuelve.
  8. El frontend almacena el JWT en localStorage.
  9. El usuario es redirigido al feed principal.
- **Flujo alternativo (email duplicado):** El backend retorna 400 con mensaje "Email ya registrado".

### CU-002: Inicio de sesión
- **Nombre:** Iniciar sesión
- **Descripción:** El usuario autenticado ingresa al sistema.
- **Actor:** Visitante (no autenticado)
- **Precondiciones:** El usuario debe estar registrado y activo.
- **Postcondiciones:** Se genera y devuelve un JWT. El frontend almacena el token y los datos del usuario.
- **Flujo principal:**
  1. El usuario ingresa email y contraseña.
  2. El frontend envía POST `/api/auth/login`.
  3. El backend busca el usuario por email.
  4. El backend verifica la contraseña con BCrypt.matches().
  5. El backend verifica que el usuario esté activo.
  6. El backend genera un JWT con usuarioId, email, rol.
  7. El backend devuelve el token y datos del usuario.
  8. El frontend almacena en localStorage y redirige al feed.

### CU-003: Ver feed de recomendaciones
- **Nombre:** Visualizar feed de productos recomendados
- **Descripción:** El usuario autenticado ve el feed principal con productos ordenados por relevancia contextual.
- **Actor:** CLIENTE
- **Precondiciones:** Usuario autenticado con JWT válido.
- **Postcondiciones:** Se muestra lista de productos recomendados.
- **Flujo principal:**
  1. El frontend solicita geolocalización al navegador.
  2. Si hay coordenadas, el backend consulta OpenWeather.
  3. El backend ejecuta el motor de recomendaciones.
  4. El backend devuelve productos ordenados por puntuación.
  5. El frontend renderiza las tarjetas de producto.

### CU-004: Agregar producto al carrito
- **Nombre:** Agregar item al carrito de compras
- **Descripción:** El usuario agrega un producto a su carrito persistente.
- **Actor:** CLIENTE
- **Precondiciones:** Usuario autenticado. Producto existe, activo y con stock suficiente.
- **Postcondiciones:** Se crea o actualiza un item en carrito_items.
- **Flujo principal:**
  1. El usuario hace clic en "Agregar al carrito".
  2. El frontend envía POST `/api/carrito/items` con productoId y cantidad.
  3. El backend obtiene o crea el carrito del usuario.
  4. Si el producto ya está en el carrito, incrementa la cantidad.
  5. Si no, crea un nuevo CarritoItem.
  6. Retorna el item creado.

### CU-005: Realizar pedido (Checkout)
- **Nombre:** Confirmar pedido desde el carrito
- **Descripción:** El usuario convierte los items del carrito en un pedido.
- **Actor:** CLIENTE
- **Precondiciones:** Carrito con al menos un item. Usuario autenticado.
- **Postcondiciones:** Se crea un Pedido con estado CONFIRMADO. El carrito se vacía. El stock de productos se decrementa.
- **Flujo principal:**
  1. El usuario va al carrito y hace clic en "Pagar".
  2. Selecciona método de pago (CONTRA_ENTREGA o YAPE).
  3. Si es YAPE, ingresa número de referencia.
  4. El frontend envía POST `/api/pedidos`.
  5. El backend verifica stock.
  6. El backend crea el Pedido y PedidoItems.
  7. El backend vacía el carrito.
  8. El backend decrementa el stock.
  9. Retorna el pedido creado.

### CU-006: Gestionar pedidos (Comerciante)
- **Nombre:** Gestionar pedidos recibidos
- **Descripción:** El comerciante visualiza y actualiza el estado de los pedidos.
- **Actor:** COMERCIANTE
- **Precondiciones:** Usuario autenticado con rol COMERCIANTE.
- **Postcondiciones:** El estado del pedido puede cambiar.
- **Flujo principal:**
  1. El comerciante accede al panel de gestión de pedidos.
  2. El frontend solicita GET `/api/pedidos/comerciante`.
  3. Se muestran pedidos con items de sus comercios.
  4. El comerciante puede cambiar estado: CONFIRMADO → ENVIADO → ENTREGADO.

### CU-007: Reservar producto
- **Nombre:** Reservar un producto por 24 horas
- **Descripción:** El usuario reserva un producto que permite reserva.
- **Actor:** CLIENTE
- **Precondiciones:** Producto permite reserva (permiteReserva=true). Stock suficiente.
- **Postcondiciones:** Se crea una reserva con expiración en 24h.
- **Flujo principal:**
  1. El usuario hace clic en "Reservar" en la tarjeta del producto.
  2. El frontend muestra un modal con cantidad y fecha.
  3. El usuario confirma la reserva.
  4. POST `/api/reservas`.
  5. Backend crea la reserva con fechaExpiracion = now + 24h.
  6. Retorna la reserva creada.

---

## 7. BASE DE DATOS

### Tabla: `usuarios`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| username | VARCHAR(50) | | | NOT NULL, UNIQUE |
| password | VARCHAR(255) | | | NOT NULL |
| email | VARCHAR(100) | | | NOT NULL, UNIQUE |
| nombre | VARCHAR(100) | | | |
| apellidos | VARCHAR(100) | | | |
| telefono | VARCHAR(20) | | | |
| direccion | VARCHAR(255) | | | |
| rol | VARCHAR(20) | | | NOT NULL (CLIENTE,COMERCIANTE,ADMIN) |
| preferencias | TEXT | | | JSON |
| historial_busqueda | TEXT | | | JSON |
| latitud | DOUBLE | | | |
| longitud | DOUBLE | | | |
| radio_busqueda_km | INT | | | DEFAULT 5 |
| activo | BOOLEAN | | | NOT NULL DEFAULT true |
| fecha_creacion | DATETIME | | | NOT NULL |
| fecha_actualizacion | DATETIME | | | NOT NULL |

### Tabla: `comercios`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| nombre | VARCHAR(100) | | | NOT NULL |
| descripcion | VARCHAR(500) | | | NOT NULL |
| direccion | VARCHAR(255) | | | |
| telefono | VARCHAR(20) | | | |
| email | VARCHAR(100) | | | |
| horario_atencion | VARCHAR(100) | | | |
| categoria | VARCHAR(50) | | | NOT NULL |
| latitud | DOUBLE | | | NOT NULL |
| longitud | DOUBLE | | | NOT NULL |
| imagen_url | VARCHAR(500) | | | |
| calificacion | DOUBLE | | | DEFAULT 0.0 |
| numero_reseñas | INT | | | DEFAULT 0 |
| activo | BOOLEAN | | | NOT NULL DEFAULT true |
| propietario_id | BIGINT | | FK → usuarios(id) | NOT NULL |
| fecha_creacion | DATETIME | | | NOT NULL |
| fecha_actualizacion | DATETIME | | | NOT NULL |

### Tabla: `productos`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| nombre | VARCHAR(100) | | | NOT NULL |
| descripcion | VARCHAR(500) | | | NOT NULL |
| precio | DECIMAL(10,2) | | | NOT NULL |
| stock | INT | | | NOT NULL |
| url_imagen | VARCHAR(500) | | | |
| categoria | VARCHAR(50) | | | NOT NULL |
| etiquetas_inteligentes | TEXT | | | JSON |
| estado | VARCHAR(20) | | | NOT NULL DEFAULT 'DISPONIBLE' |
| calificacion_promedio | DOUBLE | | | DEFAULT 0.0 |
| conteo_visualizaciones | INT | | | DEFAULT 0 |
| conteo_compras | INT | | | DEFAULT 0 |
| activo | BOOLEAN | | | NOT NULL DEFAULT true |
| permite_reserva | BOOLEAN | | | NOT NULL DEFAULT false |
| permite_pago_adelantado | BOOLEAN | | | NOT NULL DEFAULT false |
| comercio_id | BIGINT | | FK → comercios(id) | NOT NULL |
| fecha_creacion | DATETIME | | | NOT NULL |
| fecha_actualizacion | DATETIME | | | NOT NULL |

### Tabla: `interacciones`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| usuario_id | BIGINT | | FK → usuarios(id) | NOT NULL |
| producto_id | BIGINT | | FK → productos(id) | NOT NULL |
| tipo_interaccion | VARCHAR(20) | | | NOT NULL (VISUALIZACION,CLICK,COMPRA,FAVORITO) |
| cantidad | INT | | | DEFAULT 1 |
| precio_momento | DECIMAL(10,2) | | | |
| contexto_clima | TEXT | | | JSON |
| latitud_usuario | DOUBLE | | | |
| longitud_usuario | DOUBLE | | | |
| fuente | VARCHAR(20) | | | (BUSQUEDA,RECOMENDACION,DIRECTO,PROMOCION,FEED) |
| fecha_interaccion | DATETIME | | | NOT NULL |

### Tabla: `carritos`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| usuario_id | BIGINT | | FK → usuarios(id) | NOT NULL, UNIQUE |
| fecha_creacion | DATETIME | | | NOT NULL |
| fecha_actualizacion | DATETIME | | | NOT NULL |

### Tabla: `carrito_items`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| carrito_id | BIGINT | | FK → carritos(id) | NOT NULL |
| producto_id | BIGINT | | FK → productos(id) | NOT NULL |
| cantidad | INT | | | NOT NULL DEFAULT 1 |
| fecha_agregado | DATETIME | | | NOT NULL |

### Tabla: `pedidos`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| usuario_id | BIGINT | | FK → usuarios(id) | NOT NULL |
| estado | VARCHAR(20) | | | NOT NULL (CONFIRMADO,ENVIADO,ENTREGADO,CANCELADO) |
| metodo_pago | VARCHAR(20) | | | (CONTRA_ENTREGA,YAPE) |
| referencia_yape | VARCHAR(100) | | | |
| yape_confirmado | BOOLEAN | | | DEFAULT false |
| total | DECIMAL(10,2) | | | NOT NULL |
| fecha_creacion | DATETIME | | | NOT NULL |
| fecha_actualizacion | DATETIME | | | NOT NULL |

### Tabla: `pedido_items`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| pedido_id | BIGINT | | FK → pedidos(id) | NOT NULL |
| producto_id | BIGINT | | FK → productos(id) | NOT NULL |
| nombre_producto | VARCHAR(100) | | | |
| precio_unitario | DECIMAL(10,2) | | | |
| cantidad | INT | | | NOT NULL |
| subtotal | DECIMAL(10,2) | | | |
| nombre_comercio | VARCHAR(100) | | | |

### Tabla: `reservas`
| Campo | Tipo | PK | FK | Restricciones |
|-------|------|----|----|--------------|
| id | BIGINT | PK | | AUTO_INCREMENT |
| usuario_id | BIGINT | | FK → usuarios(id) | NOT NULL |
| producto_id | BIGINT | | FK → productos(id) | NOT NULL |
| cantidad | INT | | | NOT NULL DEFAULT 1 |
| estado | VARCHAR(20) | | | NOT NULL (ACTIVA,COMPLETADA,EXPIRADA,CANCELADA) |
| fecha_reserva | DATETIME | | | NOT NULL |
| fecha_expiracion | DATETIME | | | NOT NULL |
| comercio_id | BIGINT | | FK → comercios(id) | NOT NULL |

---

## 8. MODELO ENTIDAD RELACIÓN

```
usuarios 1───* interacciones *───1 productos
usuarios 1───1 carritos
carritos 1───* carrito_items *───1 productos
usuarios 1───* pedidos
pedidos 1───* pedido_items *───1 productos
usuarios 1───* reservas
reservas *───1 productos
reservas *───1 comercios
usuarios 1───* comercios (propietario)
comercios 1───* productos
productos *───1 comercios
```

### Descripción de relaciones:
- **Usuario → Carrito:** Uno a uno. Cada usuario tiene exactamente un carrito.
- **Usuario → Pedido:** Uno a muchos. Un usuario puede tener múltiples pedidos.
- **Usuario → Reserva:** Uno a muchos. Un usuario puede tener múltiples reservas.
- **Usuario → Comercio:** Uno a muchos. Un comerciante puede tener múltiples comercios.
- **Carrito → CarritoItem:** Uno a muchos. Un carrito contiene múltiples items.
- **CarritoItem → Producto:** Muchos a uno. Cada item referencia un producto.
- **Pedido → PedidoItem:** Uno a muchos. Un pedido contiene múltiples items.
- **PedidoItem → Producto:** Muchos a uno. Cada item referencia un producto.
- **Comercio → Producto:** Uno a muchos. Un comercio tiene múltiples productos.
- **Reserva → Producto:** Muchos a uno. Una reserva referencia un producto.
- **Reserva → Comercio:** Muchos a uno. Una reserva referencia un comercio.

---

## 9. DICCIONARIO DE DATOS

| Atributo | Entidad | Descripción | Tipo | Ejemplo |
|----------|---------|------------|------|---------|
| id | Usuario | Identificador único del usuario | BIGINT | 1 |
| username | Usuario | Nombre de usuario (parte local del email) | VARCHAR(50) | "juan" |
| password | Usuario | Hash BCrypt de la contraseña | VARCHAR(255) | "$2a$10$..." |
| email | Usuario | Correo electrónico (usado para login) | VARCHAR(100) | "juan@example.com" |
| rol | Usuario | Rol del usuario en el sistema | ENUM | "CLIENTE" |
| latitud | Usuario | Coordenada de latitud para geolocalización | DOUBLE | -17.5528 |
| longitud | Usuario | Coordenada de longitud para geolocalización | DOUBLE | -65.8756 |
| radio_busqueda_km | Usuario | Radio de búsqueda en kilómetros | INT | 5 |
| preferencias | Usuario | Preferencias en formato JSON | TEXT | {"categorias":["Ropa"]} |
| precio | Producto | Precio del producto en Bolivianos | DECIMAL(10,2) | 45.99 |
| stock | Producto | Cantidad disponible en inventario | INT | 20 |
| etiquetas_inteligentes | Producto | Etiquetas para matching contextual | TEXT(JSON) | ["lluvia","abrigo"] |
| permite_reserva | Producto | Indica si el producto se puede reservar | BOOLEAN | true |
| permite_pago_adelantado | Producto | Indica si acepta Yape | BOOLEAN | true |
| tipo_interaccion | Interaccion | Tipo de interacción del usuario | ENUM | "VISUALIZACION" |
| fuente | Interaccion | Fuente de la interacción | ENUM | "RECOMENDACION" |
| estado | Pedido | Estado actual del pedido | ENUM | "CONFIRMADO" |
| metodo_pago | Pedido | Método de pago seleccionado | ENUM | "YAPE" |
| referencia_yape | Pedido | Código de referencia para pago Yape | VARCHAR(100) | "YAPE-ABC123" |
| total | Pedido | Monto total del pedido | DECIMAL(10,2) | 150.75 |
| fecha_expiracion | Reserva | Fecha límite de la reserva | DATETIME | 2026-06-30 14:00:00 |

---

## 10. APIs REST

### 10.1 Autenticación

#### POST /api/auth/registro
- **Descripción:** Registra un nuevo usuario.
- **Request:**
```json
{"email": "nuevo@example.com", "contrasena": "miPassword123", "rol": "COMERCIANTE"}
```
- **Response 201:**
```json
{"exito": true, "mensaje": "Registro exitoso", "datos": {"token": "eyJ...", "tipo": "Bearer", "usuario": {...}}}
```
- **Errores:** 400 (email duplicado), 500 (error interno).

#### POST /api/auth/login
- **Descripción:** Inicia sesión.
- **Request:**
```json
{"email": "juan@example.com", "contrasena": "password"}
```
- **Response 200:**
```json
{"exito": true, "mensaje": "Login exitoso", "datos": {"token": "eyJ...", "tipo": "Bearer", "usuario": {"id": 1, "nombre": "Juan Pérez", "email": "juan@example.com", "rol": "CLIENTE"}}}
```
- **Errores:** 401 (credenciales inválidas).

#### POST /api/auth/verificar-email?email=juan@example.com
- **Descripción:** Verifica disponibilidad de email.
- **Response 200:** `{"exito": true, "datos": true}`

### 10.2 Productos

#### GET /api/productos
- **Descripción:** Lista todos los productos activos.
- **Response 200:** Array de productos con datos del comercio.

#### GET /api/productos/{id}
- **Descripción:** Obtiene un producto por ID.
- **Response 200:** Producto completo.

#### GET /api/productos/comercio/{comercioId}
- **Descripción:** Productos de un comercio específico.

#### GET /api/productos/recomendados?latitud=-17.5528&longitud=-65.8756
- **Descripción:** Obtiene productos recomendados por ubicación.

### 10.3 Carrito

#### GET /api/carrito
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Obtiene el carrito del usuario autenticado.

#### POST /api/carrito/items
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"productoId": 1, "cantidad": 1}`
- **Descripción:** Agrega un producto al carrito.
- **Response 201:** Item creado.

#### PUT /api/carrito/items/{itemId}
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"cantidad": 3}`
- **Descripción:** Actualiza cantidad (0 elimina el item).

#### DELETE /api/carrito/items/{itemId}
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Elimina un item del carrito.

#### DELETE /api/carrito
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Vacía el carrito completo.

### 10.4 Pedidos

#### POST /api/pedidos
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"metodoPago": "YAPE", "referenciaYape": "REF-123"}`
- **Descripción:** Crea un pedido desde el carrito.

#### GET /api/pedidos
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Historial de pedidos del usuario.

#### GET /api/pedidos/comerciante
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Pedidos que contienen productos del comerciante.

#### PUT /api/pedidos/{id}/estado
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"estado": "ENVIADO"}`
- **Descripción:** Actualiza estado del pedido (COMERCIANTE).

#### PUT /api/pedidos/{id}/confirmar-yape
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"referenciaYape": "YAPE-ABC123"}`
- **Descripción:** Comerciante confirma pago Yape.

### 10.5 Reservas

#### POST /api/reservas
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"productoId": 1, "cantidad": 2}`
- **Descripción:** Crea una reserva de producto.

#### GET /api/reservas
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Reservas del usuario.

#### GET /api/reservas/comerciante
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Reservas de productos del comerciante.

#### PUT /api/reservas/{id}/estado
- **Headers:** `Authorization: Bearer <token>`
- **Request:** `{"estado": "COMPLETADA"}`
- **Descripción:** Actualiza estado de reserva.

### 10.6 Recomendaciones

#### GET /api/recomendaciones?latitud=-17.5528&longitud=-65.8756
- **Headers:** `Authorization: Bearer <token>`
- **Descripción:** Obtiene feed de recomendaciones personalizado.
- **Response:**
```json
{"exito": true, "mensaje": "Recomendaciones obtenidas", "datos": [{"id": 1, "nombre": "Paraguas Premium", "precio": 45.99, "distancia": 0.5, "puntuacion": 0.85, "nombreComercio": "Tienda Central", ...}]}
```

### 10.7 ApiResponseDTO (Estructura unificada)
```json
{
  "exito": true|false,
  "mensaje": "Descripción del resultado",
  "datos": { ... } | [ ... ] | null,
  "errores": ["Error 1", "Error 2"] | null,
  "timestamp": "2026-06-29T14:00:00"
}
```

---

## 11. SEGURIDAD

### 11.1 JWT (JSON Web Token)
- **Algoritmo:** HS384 (HMAC-SHA384)
- **Claims:**
  - `sub`: usuarioId
  - `email`: email del usuario
  - `rol`: rol del usuario (CLIENTE, COMERCIANTE, ADMIN)
  - `iat`: fecha de emisión
  - `exp`: fecha de expiración (24 horas)
- **Firma:** Secreto configurable en `jwt.secret`.
- **Validación:** Cada request a `/api/**` pasa por `JwtFilter` que extrae y valida el token.

### 11.2 Spring Security
- **Configuración:**
  - CSRF deshabilitado (API stateless).
  - CORS configurado para origen del frontend.
  - Todas las rutas `/api/**` permitidas sin autenticación.
  - Filtro JWT se ejecuta antes de `UsernamePasswordAuthenticationFilter`.
- **Flujo de autenticación:**
  1. `JwtFilter.doFilterInternal()` extrae el header `Authorization: Bearer <token>`.
  2. Valida el token con `JwtProvider.validateToken()`.
  3. Si es válido, establece `request.setAttribute("usuarioId", id)` y crea `UsernamePasswordAuthenticationToken` en el `SecurityContextHolder`.
  4. Los controllers obtienen `usuarioId` vía `request.getAttribute("usuarioId")`.

### 11.3 Roles y permisos
| Endpoint | ADMIN | COMERCIANTE | CLIENTE |
|----------|-------|-------------|---------|
| GET /api/productos | ✓ | ✓ | ✓ |
| POST /api/carrito/items | ✓ | ✓ | ✓ |
| GET /api/pedidos | ✓ | ✓ | ✓ |
| GET /api/pedidos/comerciante | ✓ | ✓ | ✗ |
| PUT /api/pedidos/{id}/estado | ✓ | ✓ | ✗ |
| GET /api/reservas | ✓ | ✓ | ✓ |
| GET /api/reservas/comerciante | ✓ | ✓ | ✗ |
| POST /api/productos | ✓ | ✓ | ✗ |
| PUT /api/productos/{id} | ✓ | ✓ | ✗ |

### 11.4 Protección de rutas (Frontend)
- El frontend verifica la existencia del JWT en localStorage.
- Según el rol, muestra/oculta componentes (Header, PanelComerciante, AdminPanel).
- Si el token expira (response 401), el interceptor de Axios limpia localStorage y redirige al login.

### 11.5 Manejo de contraseñas
- Las contraseñas se almacenan hasheadas con BCrypt (costo 10).
- Nunca se devuelven en respuestas.
- No hay recuperación de contraseña (funcionalidad futura).

---

## 12. MOTOR DE RECOMENDACIONES

### 12.1 Algoritmo
El motor implementa un **sistema de recomendación híbrido** que combina tres factores con pesos configurables:

```
Puntuacion = (peso_clima * factor_clima) + (peso_ubicacion * factor_ubicacion) + (peso_historial * factor_historial)
```

Donde: `peso_clima + peso_ubicacion + peso_historial = 1.0`

### 12.2 Paso a paso

1. **Obtener ubicación del usuario:** Se recibe `latitud` y `longitud` como parámetros de consulta.
2. **Obtener clima:** Se consulta OpenWeather API con las coordenadas. Se obtiene `main.temp`, `weather[0].main`, `weather[0].description`.
3. **Mapear clima a etiquetas inteligentes:**
   - `Rain`, `Drizzle`, `Thunderstorm` → "lluvia"
   - `Snow` → "frío", "invierno"
   - `Clear` → "calor" (si temp > 25°C), "soleado"
   - `Clouds` → depende de temperatura
4. **Consultar productos activos:** Se obtienen todos los productos con `activo=true` y `stock > 0`.
5. **Calcular factor_clima (0-1):** Porcentaje de etiquetas del producto que coinciden con las etiquetas derivadas del clima.
6. **Calcular factor_ubicacion (0-1):** Distancia del producto al usuario usando Haversine. A menor distancia, mayor puntuación. Productos a más de 10km obtienen 0.
7. **Calcular factor_historial (0-1):**
   - Se consultan interacciones del usuario.
   - Productos ya comprados tienen prioridad.
   - Categorías del historial tienen peso adicional.
   - Se usa el conteo de interacciones por categoría.
8. **Calcular puntuación final ponderada.**
9. **Ordenar descendente** y devolver top N (configurable, default 20).

### 12.3 Fórmula de Haversine
```
a = sin²(Δlat/2) + cos(lat1)·cos(lat2)·sin²(Δlon/2)
c = 2 · atan2(√a, √(1-a))
d = 6371 · c  (distancia en km)
```

### 12.4 Fallbacks
- **OpenWeather no responde:** Se ignora el factor clima, se recomienda solo por ubicación + historial.
- **Geolocalización falla:** Se recomienda solo por clima + historial (o aleatorio si ambos fallan).
- **Usuario sin historial:** Se usa factor de popularidad global (productos más vistos).
- **Sin productos recomendados:** Se devuelven productos aleatorios con stock disponible.

---

## 13. APIs EXTERNAS

### 13.1 OpenWeather API
- **Endpoint:** `https://api.openweathermap.org/data/2.5/weather`
- **Método:** GET
- **Parámetros:** `lat`, `lon`, `appid`, `units=metric`, `lang=es`
- **Response:**
```json
{
  "main": {"temp": 22.5, "humidity": 65},
  "weather": [{"main": "Clear", "description": "cielo claro"}],
  "name": "Punata"
}
```
- **Implementación:** `ClimaService.java` con `RestTemplate`, timeout 5s.

### 13.2 Leaflet + OpenStreetMap
- **Librería:** Leaflet (CDN), sin clave API.
- **Uso:** Visualización de mapas con marcadores de comercios cercanos.
- **Tile Layer:** OpenStreetMap estándar.
- **Funcionalidad:** Mostrar ubicación del usuario y comercios en el mapa.

### 13.3 Geolocation API (Navegador)
- **API:** `navigator.geolocation.getCurrentPosition()`
- **Uso:** Obtener coordenadas del usuario al cargar el feed.
- **Permisos:** Solicita permiso explícito al usuario.
- **Fallback:** Si el usuario deniega, coordenadas por defecto (Punata).

---

## 14. INTERFACES DEL SISTEMA

### 14.1 Login
- **Función:** Autenticar usuario.
- **Campos:** Email (input email), Contraseña (input password).
- **Botones:** "Iniciar Sesión", "Registrarse".
- **Validaciones:** Email requerido y formato válido, contraseña requerida.

### 14.2 Registro
- **Función:** Crear nueva cuenta.
- **Campos:** Email, Contraseña, Confirmar Contraseña, Rol (select: Cliente/Comerciante).
- **Botones:** "Crear Cuenta".
- **Validaciones:** Email único, contraseña >= 6 caracteres, confirmación coincide.

### 14.3 Feed de Recomendaciones
- **Función:** Mostrar productos recomendados.
- **Componentes:** Header con navegación, grilla de ProductCards.
- **Cada tarjeta:** Imagen, nombre, descripción, precio, calificación, comercio, distancia, etiquetas.
- **Botones por tarjeta:** "Agregar al carrito", "Reservar" (si aplica), "Yape" (si aplica).
- **Header:** Logo, icono de carrito con badge de conteo, menú de usuario.

### 14.4 Carrito
- **Función:** Ver y gestionar items del carrito.
- **Componentes:** Lista de items con imagen, nombre, precio unitario, cantidad (input numérico), subtotal, botón eliminar.
- **Resumen:** Subtotal total, botón "Pagar".
- **Modal de pago:** Seleccionar método (Contra entrega / Yape), ingresar referencia si Yape.

### 14.5 Perfil de Usuario
- **Función:** Ver y editar datos personales.
- **Datos mostrados:** Nombre, email, rol, coordenadas, preferencias.
- **Enlaces:** "Mis Pedidos", "Mis Reservas".

### 14.6 Historial de Pedidos
- **Función:** Ver pedidos realizados.
- **Tabla:** ID, fecha, total, estado, método de pago.
- **Expansión:** Detalle de items del pedido.

### 14.7 Mis Reservas
- **Función:** Ver reservas activas y pasadas.
- **Tabla:** Producto, cantidad, estado, fecha expiración.

### 14.8 Panel del Comerciante
- **Pestañas:** Pedidos Recibidos, Gestión de Reservas, Productos.
- **Pedidos:** Lista de pedidos con items del comercio, botones para cambiar estado, confirmar Yape.
- **Reservas:** Lista de reservas de sus productos, botón "Completar".

### 14.9 Panel de Administración (futuro)
- **Función:** Gestión global del sistema.
- **Componentes:** Tablas de usuarios y comercios con acciones.

---

## 15. VALIDACIONES

### 15.1 Frontend (React)
- Formulario de login: email formato válido (regex), contraseña no vacía.
- Formulario de registro: email formato válido, contraseña >= 6 caracteres, confirmación coincide.
- Carrito: cantidad mínima 1, máxima stock disponible.
- Checkout: selección de método de pago requerida, referencia Yape requerida si aplica.
- UI feedback: spinner durante carga, mensajes toast de éxito/error.

### 15.2 Backend (Spring)
- `@Valid` + `@NotBlank`, `@Email` en LoginRequestDTO.
- `@Column(nullable=false)` en entidades JPA.
- Validación de existencia de entidades con `orElseThrow(EntityNotFoundException)`.
- Validación de stock antes de agregar al carrito (`stock < cantidad`).
- Validación de stock antes de crear pedido.
- Validación de permisos: `usuarioId` del carrito/pedido debe coincidir con el del token.
- Validación de estado: transiciones permitidas (CONFIRMADO → ENVIADO → ENTREGADO).

### 15.3 Base de datos (MySQL)
- UNIQUE en `usuarios.email` y `usuarios.username`.
- FOREIGN KEY constraints con integridad referencial.
- NOT NULL en campos obligatorios.
- DEFAULT values para campos opcionales.

---

## 16. DIAGRAMAS (descripción textual)

### 16.1 Diagrama de clases (Backend)
```
Usuario ──1──→* Comercio (propietario)
Usuario ──1──→1 Carrito
Usuario ──1──→* Pedido
Usuario ──1──→* Interaccion
Usuario ──1──→* Reserva

Carrito ──1──→* CarritoItem
CarritoItem ──*──→1 Producto

Pedido ──1──→* PedidoItem
PedidoItem ──*──→1 Producto

Producto ──*──→1 Comercio

Reserva ──*──→1 Producto
Reserva ──*──→1 Comercio
Reserva ──*──→1 Usuario

Interaccion ──*──→1 Producto
Interaccion ──*──→1 Usuario
```

### 16.2 Diagrama de componentes
```
[React SPA] ←→ [Spring Boot REST API] ←→ [MySQL]
                   ↕
            [OpenWeather API]
                   ↕
            [Leaflet/OSM]
```

### 16.3 Diagrama de secuencia (Compra)
```
Usuario → Frontend → Backend → MySQL
   │         │          │        │
   │──click "Pagar"────→│        │
   │         │──POST /api/pedidos─→│
   │         │          │──verify stock─→│
   │         │          │──create pedido→│
   │         │          │──clear carrito→│
   │         │          │──decrement stock→│
   │         │←─201 pedido creado──│
   │←─redirect a pedidos──│        │
```

### 16.4 Diagrama de actividades (Recomendación)
```
Inicio → Obtener coordenadas (GPS/input)
       → Consultar clima (OpenWeather)
       → Obtener productos activos
       → Para cada producto:
           → Calcular factor clima (coincidencia etiquetas)
           → Calcular factor ubicación (distancia Haversine)
           → Calcular factor historial (interacciones previas)
           → Calcular puntuación ponderada
       → Ordenar por puntuación descendente
       → Devolver top N
       → Fin
```

### 16.5 Diagrama de paquetes (Backend)
```
com.marketplace.pacccioli
├── config (CorsConfig, DataSeeder)
├── controller (AuthController, ProductoController, CarritoController, ...)
├── dto (LoginRequestDTO, LoginResponseDTO, ApiResponseDTO, CarritoDTO, ...)
├── model (Usuario, Producto, Comercio, Carrito, Pedido, ...)
├── repository (UsuarioRepository, ProductoRepository, ...)
├── security (JwtProvider, JwtFilter, SecurityConfig)
└── service (CarritoService, PedidoService, ReservaService, RecomendacionService, ClimaService)
```

### 16.6 Diagrama de despliegue
```
[Navegador Cliente]
       │
       │ HTTPS
       ▼
[Servidor Web (Spring Boot Embedded Tomcat)]  ←→ [MySQL Server (localhost:3306)]
       │
       │ HTTPS
       ▼
[OpenWeather API (externa)]

[Leaflet CDN (externa)]
```

---

## 17. FLUJO COMPLETO DEL SISTEMA

### Registro
1. Usuario completa formulario → frontend valida datos.
2. POST `/api/auth/registro` → backend valida email único.
3. Backend hashea contraseña con BCrypt.
4. Backend guarda Usuario en MySQL.
5. Si es COMERCIANTE, crea Comercio por defecto.
6. Backend genera JWT → frontend almacena en localStorage.
7. Redirección al feed.

### Inicio de sesión
1. Usuario ingresa credenciales → POST `/api/auth/login`.
2. Backend busca por email, verifica password con BCrypt.
3. Backend genera JWT (usuarioId, email, rol, exp: 24h).
4. Frontend guarda token y objeto usuario en localStorage.
5. Redirección al feed.

### Recomendaciones
1. Frontend solicita geolocalización al navegador.
2. Si hay coordenadas, se envían a GET `/api/recomendaciones?lat=...&lon=...`.
3. Backend consulta OpenWeather con las coordenadas.
4. Backend ejecuta motor de recomendaciones híbrido.
5. Backend devuelve productos ordenados por puntuación.
6. Frontend renderiza ProductCards.

### Compra
1. Usuario agrega productos al carrito.
2. Usuario ingresa al carrito, revisa items.
3. Usuario hace clic en "Pagar".
4. Selecciona método de pago (CONTRA_ENTREGA / YAPE).
5. Si YAPE: ingresa código de referencia, hace clic en "Pagar con Yape".
6. POST `/api/pedidos` → backend verifica stock.
7. Backend crea Pedido (estado CONFIRMADO) y PedidoItems.
8. Backend vacía carrito (DELETE carrito_items).
9. Backend decrementa stock de productos.
10. Frontend muestra confirmación y redirige a historial de pedidos.

### Pago Yape (simulado)
1. Backend genera código único `YAPE-XXXXXXXX` y lo asocia al pedido.
2. El cliente le muestra el código al comerciante (físicamente o por foto).
3. El comerciante ingresa el código en el panel de gestión de pedidos.
4. PUT `/api/pedidos/{id}/confirmar-yape` → backend verifica código.
5. Backend marca `yapeConfirmado = true`.
6. El pedido puede avanzar a ENVIADO.

### Historial
1. Usuario navega a "Mis Pedidos".
2. GET `/api/pedidos` → backend retorna pedidos del usuario.
3. Frontend muestra tabla con pedidos (ID, fecha, total, estado).
4. Usuario puede expandir para ver items.

### Reportes (Admin)
1. Administrador accede a panel.
2. GET `/api/admin/reportes` → backend consulta conteos.
3. Frontend muestra gráficos/tablas básicos.

---

## 18. PRUEBAS

### 18.1 Pruebas unitarias (Backend)
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | `JwtProviderTest.testGenerateAndValidateToken` | Genera y valida un JWT correctamente. |
| 2 | `JwtProviderTest.testExpiredToken` | Rechaza un token expirado. |
| 3 | `CarritoServiceTest.testAgregarItem` | Agrega un item al carrito. |
| 4 | `CarritoServiceTest.testAgregarItemStockInsuficiente` | Lanza excepción si stock < cantidad. |
| 5 | `CarritoServiceTest.testEliminarItem` | Elimina un item del carrito. |
| 6 | `PedidoServiceTest.testCrearPedido` | Crea un pedido y vacía el carrito. |
| 7 | `RecomendacionServiceTest.testRecomendar` | Genera recomendaciones correctamente. |
| 8 | `RecomendacionServiceTest.testFalloSinClima` | Funciona sin datos climáticos. |
| 9 | `AuthControllerTest.testLoginExitoso` | Login con credenciales correctas. |
| 10 | `AuthControllerTest.testLoginFallido` | Login con contraseña incorrecta retorna 401. |
| 11 | `ProductoControllerTest.testListarProductos` | Lista productos activos. |
| 12 | `ReservaServiceTest.testCrearReserva` | Crea una reserva con expiración. |
| 13 | `ReservaServiceTest.testReservaExpirada` | Verifica que reserva expirada no sea válida. |

### 18.2 Pruebas funcionales
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | Registro de cliente | Registra usuario, verifica JWT recibido. |
| 2 | Registro de comerciante | Registra comerciante, verifica comercio creado. |
| 3 | Login y persistencia de sesión | Login, recarga página, token sigue válido. |
| 4 | Feed de recomendaciones | Muestra productos con datos correctos. |
| 5 | Agregar al carrito | Item aparece en carrito con badge actualizado. |
| 6 | Actualizar cantidad en carrito | Cambia cantidad, subtotal se actualiza. |
| 7 | Eliminar item del carrito | Item desaparece, badge decrementa. |
| 8 | Checkout con contra entrega | Pedido creado, carrito vacío. |
| 9 | Checkout con Yape | Pedido creado con referencia Yape. |
| 10 | Confirmar Yape como comerciante | Pedido marcado como pagado. |
| 11 | Reservar producto | Reserva creada con fecha expiración. |
| 12 | Gestionar pedidos como comerciante | Cambio de estado exitoso. |

### 18.3 Pruebas de integración
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | Flujo completo auth → recomendaciones → carrito → pedido | Prueba end-to-end del ciclo de compra. |
| 2 | Integración con MySQL | Conexión, schema creado, datos persistentes. |
| 3 | Integración con OpenWeather (mock) | ClimaService funciona con mock. |
| 4 | Filtro JWT en endpoints protegidos | Requests sin token son rechazados. |

### 18.4 Pruebas de aceptación
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | Usuario sin cuenta puede registrarse | Flujo completo de registro. |
| 2 | Usuario puede ver productos y comprar | Ciclo completo de compra. |
| 3 | Comerciante puede gestionar pedidos | Panel funcional. |
| 4 | Recomendaciones reflejan clima/ubicación | Feed contextual correcto. |

### 18.5 Pruebas de seguridad
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | Inyección SQL | Caracteres especiales en inputs no alteran queries. |
| 2 | Acceso sin token | Endpoints protegidos retornan 401. |
| 3 | Acceso de otro usuario | No se puede ver carrito/pedido de otro usuario. |
| 4 | XSS | Inputs escapados en frontend. |

### 18.6 Pruebas de rendimiento
| # | Prueba | Descripción |
|---|--------|-------------|
| 1 | Carga de feed con 50+ productos | < 2 segundos. |
| 2 | 100 requests concurrentes a GET /api/productos | Sin errores. |
| 3 | Múltiples usuarios agregando al carrito | Sin condiciones de carrera. |

---

## 19. IMPLEMENTACIÓN

### 19.1 Servidor
- **Entorno:** Local (desarrollo) / VPS con Ubuntu 22.04 (producción).
- **Java:** OpenJDK 21.
- **Node.js:** 20.x (para build frontend).
- **Servidor web:** Spring Boot embedded Tomcat (backend) + Nginx (proxy inverso opcional).

### 19.2 Base de datos
- **Motor:** MySQL 8.x.
- **Configuración:**
  - `character-set-server=utf8mb4`
  - `collation-server=utf8mb4_unicode_ci`
  - `max_connections=151`
- **Inicialización:** Hibernate `ddl-auto=update` + `DataSeeder` programático.
- **Respaldos:** `mysqldump` semanal programado con cron.

### 19.3 Configuración (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/marketplace_pacccioli?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=nano123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=miClaveSecretaParaFirmarTokensJWT2024PlataformaComercioLocal
jwt.expiration=86400000
openweather.api.key=
```

### 19.4 Variables de entorno
| Variable | Propósito |
|----------|-----------|
| `SPRING_DATASOURCE_URL` | URL de conexión MySQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario MySQL |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña MySQL |
| `JWT_SECRET` | Clave secreta para JWT |
| `OPENWEATHER_API_KEY` | API Key de OpenWeather |
| `VITE_API_URL` | URL base del backend para frontend |

### 19.5 Despliegue (producción)
1. Build backend: `mvn clean package -DskipTests` → genera `target/marketplace-pacccioli-1.0.0.jar`.
2. Build frontend: `npm run build` → genera `frontend/dist/`.
3. Copiar artefactos al servidor.
4. Ejecutar backend: `java -jar marketplace-pacccioli-1.0.0.jar`.
5. Servir frontend con Nginx desde `dist/`.

### 19.6 Respaldos
- Base de datos: `mysqldump -u root -p marketplace_pacccioli > backup_$(date +%Y%m%d).sql`.
- Frecuencia: diaria.
- Retención: 30 días.

---

## 20. LIMITACIONES

1. **Autenticación stateless:** No hay soporte para refresh tokens. Si el JWT expira, el usuario debe volver a iniciar sesión.
2. **Sin websockets:** Los cambios de estado de pedidos no se actualizan en tiempo real (requiere recarga manual o polling).
3. **OpenWeather API gratuita:** Límite de 60 llamadas/minuto. En producción con muchos usuarios, puede excederse.
4. **Sin paginación en listas grandes:** Actualmente no hay paginación implementada en los endpoints de productos/pedidos.
5. **Yape simulado:** No es una integración real con la API de Yape. Es una validación manual con código de referencia.
6. **Sin subida de imágenes real:** Las imágenes de productos son URLs de placeholder.
7. **Sin recuperación de contraseña:** No hay flujo de "olvidé mi contraseña".
8. **Sin búsqueda por texto completo:** La búsqueda usa `LIKE %...%` que no escala bien con miles de productos.
9. **Single servidor:** La aplicación corre en un solo proceso, sin clustering ni balanceo de carga.
10. **Sin caché distribuida:** La caché de clima es en memoria local (no Redis).

---

## 21. MEJORAS FUTURAS

1. **Refresh tokens:** Implementar refresh token rotativo para mejorar seguridad y experiencia de usuario.
2. **WebSockets (STOMP):** Notificaciones en tiempo real de cambios de estado de pedidos.
3. **Pasarela de pago real:** Integración con API de Yape real, QR simple, transferencia bancaria.
4. **Paginación infinita:** Scroll infinito con `page` y `size` en endpoints.
5. **Subida de imágenes:** Integración con Cloudinary o S3 para imágenes de productos.
6. **Recuperación de contraseña:** Flujo de email con token de reseteo.
7. **Búsqueda full-text:** Migrar a índices FULLTEXT de MySQL o Elasticsearch para búsqueda eficiente.
8. **Notificaciones push:** Integración con Firebase Cloud Messaging.
9. **Panel de administración avanzado:** Dashboard con gráficos (Chart.js), exportación a CSV/PDF.
10. **Sistema de reseñas:** Usuarios pueden calificar y comentar productos comprados.
11. **Caché Redis:** Cachear clima, productos populares y sesiones.
12. **Multi-idioma:** Soporte para quechua, inglés usando i18n.
13. **App móvil:** Versión React Native o Flutter.
14. **Despliegue Dockerizado:** `docker-compose` con backend + frontend + MySQL + Redis.
15. **CI/CD:** Integración con GitHub Actions para pruebas automáticas y despliegue continuo.
