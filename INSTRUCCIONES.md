# Plataforma de Comercio Local Inteligente — Marketplace

Sistema de marketplace con recomendaciones inteligentes basadas en clima, ubicación e historial de usuario.

---

## Requisitos previos

| Herramienta | Versión mínima | Instalación |
|---|---|---|
| **Java JDK** | 17+ | [Adoptium Temurin](https://adoptium.net/) |
| **Maven** | 3.8+ | `winget install Apache.Maven` |
| **MySQL** | 8.0+ | `winget install Oracle.MySQL` |
| **Node.js** | 18+ | `winget install OpenJS.NodeJS.LTS` |
| **Git** | — | `winget install Git.Git` |

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/Dayver-zero/PLATAFORMA-DE-COMERCIO-MARKETPLACE.git
cd PLATAFORMA-DE-COMERCIO-MARKETPLACE
```

---

## 2. Configurar la base de datos

### 2.1 Crear la base de datos

```bash
mysql -u root -p
```

Dentro de MySQL:

```sql
CREATE DATABASE marketplace_pacccioli;
EXIT;
```

### 2.2 Configurar credenciales

Editar `backend/src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

> **Nota:** Si tu usuario o contraseña de MySQL son diferentes a `root`, cámbialos aquí.

---

## 3. Iniciar el backend

```bash
cd backend
mvn spring-boot:run
```

> La primera vez descargará dependencias de Maven. Puede tardar unos minutos.

El backend se iniciará en `http://localhost:8080`. Al arrancar, ejecutará automáticamente el script `data.sql` que crea los datos de prueba (productos, usuarios, comercios).

---

## 4. Iniciar el frontend

En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

El frontend se iniciará en `http://localhost:3000`. Abrí esa URL en el navegador.

---

## 5. Cuentas de prueba

| Email | Contraseña | Rol | Descripción |
|---|---|---|---|
| `mario@example.com` | `password` | **COMERCIANTE** | Dueño de Tienda Central — puede crear productos |
| `patricia@example.com` | `password` | **COMERCIANTE** | Dueña de Mercado Local |
| `juan@example.com` | `password` | **CLIENTE** | Cliente regular |
| `maria@example.com` | `password` | **CLIENTE** | Cliente regular |
| `admin@example.com` | `password` | **ADMIN** | Administrador del sistema |

### Cambiar de rol

Un usuario **CLIENTE** puede volverse **COMERCIANTE** desde su perfil (botón "Volverse Comerciante"). Al hacerlo, se crea un comercio por defecto asociado a su cuenta.

---

## 6. Funcionalidades principales

- **Feed de recomendaciones:** Muestra productos según el clima actual, ubicación y preferencias
- **Carrito de compras:** Agrega productos, ajusta cantidades, finaliza pedido
- **Pago adelantado (Yape):** Pago simulado desde el producto
- **Reserva de productos:** Reserva con fecha de expiración
- **Panel comerciante:** Gestión de inventario, pedidos, reservas
- **Sistema de roles:** CLIENTE, COMERCIANTE, ADMIN

---

## 7. APIs externas (opcional)

El sistema usa dos APIs externas. **El sistema funciona sin ellas**, pero las recomendaciones por clima y mapa no estarán disponibles.

### OpenWeather (recomendaciones por clima)

Crear `backend/src/main/resources/application-local.properties`:

```properties
openweather.api.key=TU_API_KEY
```

Obtener una API key gratuita en [https://openweathermap.org/api](https://openweathermap.org/api).

### Google Maps (opcional)

```properties
google.maps.api.key=TU_API_KEY
```

Obtener en [https://console.cloud.google.com/](https://console.cloud.google.com/).

---

## 8. Solución de problemas

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

Las imágenes locales están en `backend/uploads/productos/`. Si no se ven, verificar que el backend esté corriendo en el puerto 8080.

### El frontend no conecta con el backend

Verificar que el backend esté corriendo en `http://localhost:8080`. La configuración de conexión está en `frontend/.env`:

```
VITE_API_URL=http://localhost:8080/api
```

---

## 9. Estructura del proyecto

```
marketplace-pacccioli/
├── backend/                    # Spring Boot API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../       # Código Java
│   │   │   └── resources/      # Configuración y SQL
│   ├── uploads/productos/      # Imágenes de productos
│   └── pom.xml
├── frontend/                   # React + Vite + Tailwind
│   ├── public/                 # Favicon, manifest
│   ├── src/
│   │   ├── components/         # Componentes React
│   │   └── services/           # Servicios API
│   └── package.json
├── .gitignore
├── INSTRUCCIONES.md
└── README.md
```
