# Plataforma de Comercio Local Inteligente — Marketplace Pacccioli

Sistema de marketplace con recomendaciones inteligentes basadas en clima, ubicación e historial de usuario. Desarrollado para impulsar el comercio local en Punata, Bolivia.

---

## Características Principales

- **Feed de recomendaciones inteligentes** — Productos sugeridos según clima, ubicación y preferencias del usuario
- **Carrito de compras** — Agrega productos, ajusta cantidades, finaliza pedido
- **Pago adelantado (Yape)** — Pago simulado desde el producto
- **Reserva de productos** — Reserva con expiración automática (24h)
- **Panel comerciante** — Gestión de inventario, pedidos y reservas
- **Sistema de roles** — CLIENTE, COMERCIANTE, ADMIN
- **Autenticación JWT** — Login seguro con tokens
- **PWA** — Instalable en dispositivos móviles
- **Diseño Mobile-First** — Responsive con Tailwind CSS

---

## Stack Tecnológico

### Backend
| Tecnología | Versión |
|---|---|
| Java JDK | 21 |
| Spring Boot | 3.2.0 |
| Maven | 3.8+ |
| Spring Data JPA (Hibernate) | 6.3.1 |
| Spring Security | 6.x |
| MySQL | 8.0+ |
| JWT (jjwt) | 0.12.3 |
| Lombok | 1.18.42 |

### Frontend
| Tecnología | Versión |
|---|---|
| React | 18.2 |
| Vite | 5.0 |
| Tailwind CSS | 3.4 |
| Axios | 1.6 |
| Lucide React | 1.17 |

---

## Requisitos Previos

| Herramienta | Versión Mínima | Instalación |
|---|---|---|
| Java JDK | 17+ | [Adoptium Temurin](https://adoptium.net/) |
| Maven | 3.8+ | `winget install Apache.Maven` |
| MySQL | 8.0+ | `winget install Oracle.MySQL` |
| Node.js | 18+ | `winget install OpenJS.NodeJS.LTS` |
| Git | — | `winget install Git.Git` |

---

## Instalación Paso a Paso

### 1. Clonar el repositorio

```bash
git clone https://github.com/Dayver-zero/PLATAFORMA-DE-COMERCIO-MARKETPLACE.git
cd PLATAFORMA-DE-COMERCIO-MARKETPLACE
```

### 2. Configurar la Base de Datos

Crear la base de datos en MySQL:

```bash
mysql -u root -p
```

```sql
CREATE DATABASE marketplace_pacccioli;
EXIT;
```

### 3. Configurar credenciales de BD

Editar `backend/src/main/resources/application.properties` y cambiar usuario/contraseña si es necesario:

```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

> **Nota:** Hibernate crea automáticamente las 10 tablas al iniciar (`ddl-auto=update`). No necesitas ejecutar ningún script SQL manualmente.

### 4. Iniciar el Backend

```bash
cd backend
mvn spring-boot:run
```

El backend arrancará en `http://localhost:8080`. Al iniciar, ejecuta automáticamente `data.sql` que precarga datos de prueba.

### 5. Iniciar el Frontend

En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

El frontend estará disponible en `http://localhost:3000`.

---

## Cuentas de Prueba

| Email | Contraseña | Rol | Descripción |
|---|---|---|---|
| `mario@example.com` | `password` | COMERCIANTE | Dueño de Tienda Central |
| `patricia@example.com` | `password` | COMERCIANTE | Dueña de Mercado Local |
| `juan@example.com` | `password` | CLIENTE | Cliente regular |
| `maria@example.com` | `password` | CLIENTE | Cliente regular |
| `admin@example.com` | `password` | ADMIN | Administrador del sistema |

### Cambiar de Rol

Un usuario CLIENTE puede volverse COMERCIANTE desde su perfil. Al hacerlo, se crea un comercio por defecto asociado a su cuenta.

---

## Configuración de APIs Externas (Opcional)

El sistema funciona sin estas APIs, pero las recomendaciones por clima y mapa no estarán disponibles.

### OpenWeather (Recomendaciones por clima)

Crear `backend/src/main/resources/application-local.properties`:

```properties
openweather.api.key=TU_API_KEY
```

Obtener una API key gratuita en [https://openweathermap.org/api](https://openweathermap.org/api).

### Google Maps (Ubicación de comercios)

```properties
google.maps.api.key=TU_API_KEY
```

Obtener en [https://console.cloud.google.com/](https://console.cloud.google.com/).

---

## Estructura del Proyecto

```
marketplace-pacccioli/
├── backend/                          # API REST Spring Boot
│   ├── src/main/java/com/marketplace/pacccioli/
│   │   ├── config/                   # Configuraciones (DataSeeder, WebConfig)
│   │   ├── security/                 # JWT, CORS, SecurityConfig
│   │   ├── model/                    # Entidades: Usuario, Producto, Pedido, Pago...
│   │   ├── dto/                      # Objetos de transferencia de datos
│   │   ├── repository/               # Repositorios JPA
│   │   ├── service/                  # Lógica de negocio
│   │   └── controller/               # Controladores REST
│   ├── src/main/resources/
│   │   ├── application.properties    # Configuración principal
│   │   └── data.sql                  # Datos de prueba
│   ├── uploads/productos/            # Imágenes de productos
│   └── pom.xml
├── frontend/                         # SPA React + Vite + Tailwind
│   ├── src/
│   │   ├── components/               # 15 componentes React
│   │   └── services/                 # 10 servicios API (Axios)
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── .env                          # VITE_API_URL
├── .gitignore
├── INSTRUCCIONES.md
├── MANUAL_TECNICO.md
└── README.md
```

---

## Uso del Sistema

### Roles y Navegación

- **CLIENTE**: Navega el feed de recomendaciones, agrega productos al carrito, realiza pedidos, reserva productos
- **COMERCIANTE**: Gestiona su inventario (crear/editar/eliminar productos), ve pedidos recibidos, gestiona reservas
- **ADMIN**: Administra usuarios y comercios desde el panel

### Flujo de Compra

1. El usuario ve productos recomendados en el feed
2. Agrega productos al carrito
3. Va al checkout y selecciona método de pago (Efectivo / Yape)
4. Confirma el pedido
5. El comerciante ve el pedido entrante y puede cambiar su estado (PENDIENTE → CONFIRMADO → ENVIADO → ENTREGADO)

### Reserva de Productos

1. El usuario abre el modal de reserva desde la tarjeta del producto
2. Define cantidad y notas
3. La reserva expira automáticamente en 24 horas
4. El comerciante puede completar o cancelar la reserva

---

## API REST — Resumen de Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/registro` | Registrar usuario |
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Ver producto |
| GET | `/api/carrito` | Ver carrito del usuario |
| POST | `/api/carrito/agregar` | Agregar item al carrito |
| POST | `/api/pedidos/crear` | Crear pedido desde carrito |
| GET | `/api/pedidos/historial` | Historial de pedidos |
| GET | `/api/reservas` | Ver reservas del usuario |
| POST | `/api/reservas/crear` | Crear reserva |
| GET | `/api/recomendaciones` | Feed de recomendaciones |
| GET | `/api/pagos/pedido/{pedidoId}` | Ver pagos de un pedido |

---

## Solución de Problemas

### Puerto 8080 ya está en uso

```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Error de conexión a MySQL

Verificar que MySQL esté corriendo:

```bash
net start MySQL
```

### Las imágenes no se cargan

Las imágenes locales están en `backend/uploads/productos/`. Verificar que el backend esté corriendo en el puerto 8080.

### El frontend no conecta con el backend

Verificar configuración en `frontend/.env`:

```
VITE_API_URL=http://localhost:8080/api
```

### Error de compilación con Lombok

Asegurarse de que el IDE tenga Lombok configurado o compilar con Maven directamente:

```bash
mvn clean compile -DskipTests
```

---

## Licencia

Proyecto desarrollado para fines educativos y de demostración.