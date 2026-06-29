-- =====================================================================
-- Script de datos de prueba para marketplace-pacccioli
-- Se ejecuta automáticamente al iniciar (spring.jpa.hibernate.ddl-auto=update)
-- =====================================================================

-- Limpiar datos existentes (opcional, comentado por defecto)
-- DELETE FROM interaccion;
-- DELETE FROM producto;
-- DELETE FROM comercio;
-- DELETE FROM usuario;

-- =====================================================================
-- Usuarios de Prueba
-- =====================================================================
INSERT IGNORE INTO usuario (id, username, nombre, email, password, rol, latitud, longitud, radio_busqueda_km, preferencias, historial_busqueda, activo, fecha_creacion, fecha_actualizacion) VALUES
-- Cliente 1: Juan Pérez (cliente regular)
(1, 'juan@example.com', 'Juan Pérez', 'juan@example.com', '$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO', 'CLIENTE', -17.5528, -65.8756, 5, '{"favoriteCategories":["Alimentos","Ropa"]}', '["paraguas","chaqueta"]', true, NOW(), NOW()),
-- Cliente 2: María García (cliente con ubicación diferente)
(2, 'maria@example.com', 'María García', 'maria@example.com', '$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO', 'CLIENTE', -17.5550, -65.8700, 5, '{"favoriteCategories":["Electrónica"]}', '["celular"]', true, NOW(), NOW()),
-- Comerciante 1: Mario López (dueño de Tienda Central)
(3, 'mario@example.com', 'Mario López', 'mario@example.com', '$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO', 'COMERCIANTE', -17.5528, -65.8756, 0, '{"storeSpecialties":["General"]}', '[]', true, NOW(), NOW()),
-- Comerciante 2: Patricia Morales (dueña de Mercado Local)
(4, 'patricia@example.com', 'Patricia Morales', 'patricia@example.com', '$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO', 'COMERCIANTE', -17.5530, -65.8750, 0, '{"storeSpecialties":["Alimentos"]}', '[]', true, NOW(), NOW()),
-- Admin del sistema
(5, 'admin@example.com', 'Admin System', 'admin@example.com', '$2a$10$GRLdNijSQmLFd4Z9xB5h.eKl3l4YzHPyKR2p5l8ZXQz3QMzWEW.YO', 'ADMIN', -17.5528, -65.8756, 0, '{"role":"administrator"}', '[]', true, NOW(), NOW());

-- =====================================================================
-- Comercios (Tiendas)
-- =====================================================================
INSERT IGNORE INTO comercio (id, nombre, descripcion, direccion, telefono, horario_atencion, categoria, latitud, longitud, calificacion, numero_resenas, propietario_id, activo, fecha_creacion, fecha_actualizacion) VALUES
-- Tienda Central
(1, 'Tienda Central Punata', 'Tienda general con variedad de productos', 'Calle Principal 123, Punata', '+591-4-123-4567', '09:00-18:00 (L-V), 09:00-17:00 (S)', 'OTROS', -17.5528, -65.8756, 4.5, 12, 3, true, NOW(), NOW()),
-- Mercado Local
(2, 'Mercado Local', 'Verduras, frutas y alimentos frescos', 'Mercado Principal, Punata', '+591-4-123-4568', '06:00-19:00 (L-D)', 'SUPERMERCADO', -17.5530, -65.8750, 4.7, 18, 4, true, NOW(), NOW()),
-- Tienda de Ropa
(3, 'Boutique María', 'Ropa y accesorios de moda', 'Avenida Central 456, Punata', '+591-4-123-4569', '10:00-18:00 (L-S)', 'TIENDA_ROPA', -17.5535, -65.8760, 4.2, 8, 3, true, NOW(), NOW()),
-- Tienda de Electrónica
(4, 'TechStore Punata', 'Electrónica y accesorios tecnológicos', 'Calle Comercio 789, Punata', '+591-4-123-4570', '09:00-19:00 (L-S)', 'ELECTRONICA', -17.5540, -65.8770, 4.3, 10, 4, true, NOW(), NOW());

-- =====================================================================
-- Productos
-- =====================================================================
-- Productos con etiquetas inteligentes que se usan para matching de clima:
-- "lluvia", "frío", "calor", "soleado" son palabras clave
-- =====================================================================
INSERT IGNORE INTO producto (id, nombre, descripcion, precio, stock, url_imagen, categoria, etiquetas_inteligentes, calificacion_promedio, conteo_visualizaciones, conteo_compras, comercio_id, activo, permite_reserva, permite_pago_adelantado, fecha_creacion, fecha_actualizacion) VALUES
-- Productos de Tienda Central (1)
(1, 'Paraguas Premium', 'Paraguas resistente al agua, 3 paneles apertura automática', 45.99, 20, '/uploads/productos/paraguas-premium.jpg', 'OTROS', '["lluvia","proteccion","imprescindible"]', 4.8, 150, 12, 1, true, true, true, NOW(), NOW()),
(2, 'Chaqueta Térmica', 'Chaqueta acolchada para abrigo en clima frío', 89.99, 15, '/uploads/productos/chaqueta-termica.jpg', 'ROPA', '["frío","abrigo","invierno"]', 4.7, 200, 25, 1, true, true, false, NOW(), NOW()),
(3, 'Sandalias Cómodas', 'Sandalias para clima cálido', 34.50, 30, '/uploads/productos/sandalias-comodas.jpg', 'ROPA', '["calor","verano","comodidad"]', 4.4, 180, 35, 1, true, false, false, NOW(), NOW()),
(4, 'Gafas de Sol UV', 'Gafas de protección solar', 52.00, 25, '/uploads/productos/gafas-de-sol-uv.jpg', 'ACCESORIOS', '["soleado","proteccion","moda"]', 4.6, 220, 40, 1, true, true, true, NOW(), NOW()),

-- Productos de Mercado Local (2)
(5, 'Tomates Frescos', 'Tomates de temporada, producción local', 3.50, 100, '/uploads/productos/tomates-frescos.jpg', 'COMIDA', '["alimentos","fresco","saludable"]', 4.9, 350, 120, 2, true, false, false, NOW(), NOW()),
(6, 'Lechuga Orgánica', 'Lechuga verde orgánica, sin pesticidas', 2.75, 80, '/uploads/productos/lechuga-organica.jfif', 'COMIDA', '["alimentos","fresco","organico"]', 4.8, 280, 95, 2, true, false, false, NOW(), NOW()),
(7, 'Papas Locales', 'Papas de variedad local, ideales para cocinar', 1.50, 200, '/uploads/productos/papas-locales.jpg', 'COMIDA', '["alimentos","basico","fresco"]', 4.7, 400, 250, 2, true, false, false, NOW(), NOW()),
(8, 'Manzanas Frescas', 'Manzanas variedad roja, cosecha reciente', 4.20, 150, '/uploads/productos/manzanas-frescas.jpg', 'COMIDA', '["alimentos","fruta","saludable"]', 4.5, 300, 110, 2, true, false, false, NOW(), NOW()),

-- Productos de Boutique María (3)
(9, 'Suéter de Lana', 'Suéter tejido de lana para abrigarse', 65.00, 10, '/uploads/productos/sueter-de-lana.jpg', 'ROPA', '["frío","abrigo","comodidad"]', 4.6, 120, 18, 3, true, true, true, NOW(), NOW()),
(10, 'Shorts Deportivos', 'Shorts cómodos para clima cálido', 28.99, 25, '/uploads/productos/shorts-deportivos.jpg', 'ROPA', '["calor","deporte","verano"]', 4.3, 95, 22, 3, true, false, false, NOW(), NOW()),

-- Productos de TechStore (4)
(11, 'Power Bank 20000mAh', 'Batería externa con carga rápida', 55.00, 18, '/uploads/productos/power-bank-20000mah.jpg', 'ELECTRONICA', '["tecnologia","practico","viaje"]', 4.7, 160, 28, 4, true, true, true, NOW(), NOW()),
(12, 'Cable USB-C', 'Cable de carga USB tipo C', 12.50, 50, '/uploads/productos/cable-usb-c.jpeg', 'ELECTRONICA', '["tecnologia","basico","accesorio"]', 4.4, 180, 65, 4, true, false, true, NOW(), NOW());

-- =====================================================================
-- Interacciones de Usuario (comportamiento del usuario)
-- =====================================================================
-- Tipos: VISUALIZACION, CLICK, COMPRA
INSERT IGNORE INTO interaccion (id, usuario_id, producto_id, tipo_interaccion, fuente, latitud_usuario, longitud_usuario, precio_momento, fecha_interaccion) VALUES
-- Juan Pérez (usuario 1) interacciones
(1, 1, 1, 'VISUALIZACION', 'RECOMENDACION', -17.5528, -65.8756, 45.99, NOW()),
(2, 1, 1, 'CLICK', 'FEED', -17.5528, -65.8756, 45.99, NOW()),
(3, 1, 1, 'COMPRA', 'DIRECTO', -17.5528, -65.8756, 45.99, NOW()),
(4, 1, 5, 'VISUALIZACION', 'BUSQUEDA', -17.5528, -65.8756, 3.50, NOW()),
(5, 1, 5, 'COMPRA', 'DIRECTO', -17.5528, -65.8756, 3.50, NOW()),
(6, 1, 2, 'VISUALIZACION', 'RECOMENDACION', -17.5528, -65.8756, 89.99, NOW()),

-- María García (usuario 2) interacciones
(7, 2, 11, 'VISUALIZACION', 'BUSQUEDA', -17.5550, -65.8700, 55.00, NOW()),
(8, 2, 11, 'CLICK', 'FEED', -17.5550, -65.8700, 55.00, NOW()),
(9, 2, 12, 'VISUALIZACION', 'RECOMENDACION', -17.5550, -65.8700, 12.50, NOW()),
(10, 2, 5, 'VISUALIZACION', 'BUSQUEDA', -17.5550, -65.8700, 3.50, NOW()),

-- Usuario 1 más visualizaciones para establecer historial
(11, 1, 6, 'VISUALIZACION', 'RECOMENDACION', -17.5528, -65.8756, 2.75, NOW()),
(12, 1, 7, 'VISUALIZACION', 'RECOMENDACION', -17.5528, -65.8756, 1.50, NOW()),
(13, 1, 4, 'VISUALIZACION', 'FEED', -17.5528, -65.8756, 52.00, NOW());

-- Resetear contadores de auto-increment después de inserts con IDs explícitos
ALTER TABLE usuario AUTO_INCREMENT = 6;
ALTER TABLE comercio AUTO_INCREMENT = 5;
ALTER TABLE producto AUTO_INCREMENT = 13;
ALTER TABLE interaccion AUTO_INCREMENT = 14;
ALTER TABLE carrito AUTO_INCREMENT = 1;
ALTER TABLE carrito_items AUTO_INCREMENT = 1;
ALTER TABLE pedido AUTO_INCREMENT = 1;
ALTER TABLE pedido_items AUTO_INCREMENT = 1;
ALTER TABLE reserva AUTO_INCREMENT = 1;

-- =====================================================================
-- Nota sobre el script:
-- - Las contraseñas están hasheadas con BCrypt: contraseña = "password"
-- - Todos los usuarios tienen la misma contraseña por simplicidad en testing
-- - Los productos tienen etiquetas inteligentes para matching con clima
-- - Las interacciones sirven para alimentar el MotorRecomendacionService
-- - Los datos están diseñados para casos de prueba realistas
-- =====================================================================
