# Mercado Local Punata - Frontend

Frontend de la Plataforma de comercio local inteligente para Punata, Bolivia.

## Stack Tecnologico

- **Framework**: React 18 con Vite
- **Estilos**: Tailwind CSS
- **Peticiones HTTP**: Axios
- **Arquitectura**: PWA (Progressive Web App)
- **Iconos**: Lucide React

## Estructura del Proyecto

```
frontend/
├── public/
│   └── manifest.json          # Manifest de PWA
├── src/
│   ├── components/
│   │   ├── Header.jsx         # Barra de navegacion con widget climatico
│   │   ├── FeedRecomendaciones.jsx  # Vista principal de recomendaciones
│   │   ├── ProductCard.jsx    # Tarjeta de producto con etiquetas inteligentes
│   │   ├── Login.jsx          # Login y registro de usuarios
│   │   └── PanelComerciante.jsx    # Panel de gestion para comerciantes
│   ├── services/
│   │   ├── api.js             # Configuracion base de Axios con interceptors JWT
│   │   ├── authService.js     # Servicio de autenticacion (login, registro, logout)
│   │   ├── climaService.js    # Servicio de clima (conecta con backend)
│   │   ├── geolocalizacionService.js  # Servicio de geolocalizacion
│   │   ├── comerciosService.js # Servicio de comercios
│   │   ├── productosService.js # Servicio de productos (CRUD)
│   │   └── recomendacionesService.js # Servicio de recomendaciones personalizadas
│   ├── App.jsx                # Componente principal con navegacion
│   ├── main.jsx               # Punto de entrada
│   └── index.css              # Estilos globales con Tailwind
├── index.html                 # HTML principal
├── package.json               # Dependencias de npm
├── vite.config.js             # Configuracion de Vite, PWA y proxy
├── tailwind.config.js         # Configuracion de Tailwind CSS
└── postcss.config.js          # Configuracion de PostCSS
```

## Instalacion

1. Navegar al directorio del frontend:
```bash
cd frontend
```

2. Instalar dependencias:
```bash
npm install
```

## Desarrollo

Levantar el servidor de desarrollo de Vite:
```bash
npm run dev
```

La aplicacion estara disponible en `http://localhost:3000`

## Build para Produccion

Crear una version optimizada para produccion:
```bash
npm run build
```

Los archivos compilados estaran en la carpeta `dist/`

## Preview de Produccion

Previsualizar la version de produccion:
```bash
npm run preview
```

## Caracteristicas PWA

La aplicacion esta configurada como PWA con:
- Manifest para instalacion en dispositivos moviles
- Service Worker para soporte offline basico
- Caching de APIs externas (OpenWeather, Backend)
- Iconos para diferentes tamanos de pantalla

## Componentes Principales

### Header
- Logo y navegacion
- Buscador de productos
- Widget climatico dinamico (conectado a backend)
- Menu responsive con sesion de usuario
- Diseno Mobile-First

### FeedRecomendaciones
- Vista principal de productos recomendados
- Recomendaciones hibridas (clima + ubicacion + historial)
- Filtros por clima, cercania y ofertas
- Grid responsive de productos
- Fallback a datos mock si el backend no esta disponible

### ProductCard
- Tarjeta de producto con imagen
- Precio, descuento y nombre de tienda
- Etiquetas inteligentes visuales
- Botones de compra y favoritos
- Compatible con estructura ProductoDTO del backend

### Login
- Login y registro con validacion de campos
- Selector de rol (Comprador / Vendedor)
- Diseno con glassmorphism
- Manejo de errores y estados de carga

### PanelComerciante
- Gestion de inventario (CRUD de productos)
- Formulario con categorias y etiquetas inteligentes
- Vista previa de inventario en tabla
- Protegido solo para usuarios COMERCIANTE

## Servicios API

Todos los servicios se conectan al backend en `http://localhost:8080/api`:

- **authService.js**: Login, registro, logout, verificacion de email
- **climaService.js**: Clima actual y pronostico por coordenadas/ciudad
- **geolocalizacionService.js**: Geolocalizacion del navegador, comercios cercanos
- **comerciosService.js**: Obtener comercios del usuario autenticado
- **productosService.js**: CRUD de productos
- **recomendacionesService.js**: Recomendaciones por clima, ubicacion e historial

## Configuracion del Backend

El frontend usa proxy de Vite para desarrollo:
- Peticiones a `/api` se redirigen a `http://localhost:8080`
- Autenticacion via JWT (Bearer token en localStorage)
- Interceptor de Axios agrega token automaticamente

## Notas

- Los componentes usan diseno Mobile-First con Tailwind CSS
- Compatible con estructura ProductoDTO del backend Spring Boot
- El servicio de geolocalizacion usa la API del navegador como fallback
- El PWA esta configurado para ser instalable en dispositivos moviles
- Los datos mock se usan como fallback cuando el backend no responde
