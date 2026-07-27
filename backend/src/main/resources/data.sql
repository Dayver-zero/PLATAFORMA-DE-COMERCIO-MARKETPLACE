-- =====================================================================
-- Script de datos de prueba para marketplace-pacccioli
-- Se ejecuta automáticamente al iniciar (spring.jpa.hibernate.ddl-auto=update)
-- =====================================================================

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

ALTER TABLE usuario AUTO_INCREMENT = 6;
ALTER TABLE comercio AUTO_INCREMENT = 5;
