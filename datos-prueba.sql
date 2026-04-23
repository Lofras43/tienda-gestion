-- =====================================================
-- DATOS DE PRUEBA PARA TESTEAR BPM VENTA Y COMPRA
-- =====================================================

-- 1. Insertar usuario de prueba
INSERT INTO usuarios (email, nombre, password, rol, fecha_registro, esta_activo) 
VALUES ('admin@tienda.com', 'Administrador', '$2a$10$8Rk1pT5QqGkQ3vQYL8qLnO.Y5oqzqJqK8qKqK8qKqK8qKqK8qKqKqK', 'ADMIN', NOW(), 1);

-- 2. Insertar productos de prueba
INSERT INTO productos (nombre, descripcion, precio_venta, precio_compra, stock, stock_minimo, categoria, esta_activo, fecha_vencimiento) VALUES
('Leche Gloria','Leche evaporada entera', 5.50, 4.00, 50, 10, 'Lácteos', 1, '2026-12-31'),
('Panificación Integral','Pan integral fresco', 3.00, 2.00, 30, 5, 'Panadería', 1, '2026-05-01'),
('Agua Mineral 1L','Agua mineral sin gas', 2.00, 1.20, 100, 20, 'Bebidas', 1, NULL),
('防护口罩','Mascarillas desechables', 15.00, 10.00, 5, 20, 'Limpieza', 1, '2027-01-01'),
('Galletas Oreo','Galletas con crema', 4.50, 3.00, 25, 8, 'Confitería', 1, '2026-11-30'),
('Yogurt Danlac','Yogurt frutal', 3.50, 2.50, 40, 12, 'Lácteos', 1, '2026-06-15'),
('Arroz Costeño','Arroz superior 1kg', 6.00, 4.50, 60, 15, 'Abarrotes', 1, NULL),
('Fideos Don Vittorio','Fideos spaghetti', 3.50, 2.80, 45, 10, 'Abarrotes', 1, '2027-03-01'),
('Detergente阿里','Detergente líquido 1L', 8.00, 5.50, 3, 5, 'Limpieza', 1, '2027-06-01'),
('Gaseosa Inca Kola','Gaseosa 600ml', 4.00, 2.80, 80, 15, 'Bebidas', 1, '2027-02-28');

-- 3. Verificar datos
SELECT 'Usuarios registrados:', COUNT(*) FROM usuarios;
SELECT 'Productos registrados:', COUNT(*) FROM productos;
SELECT 'Productos con stock bajo:', COUNT(*) FROM productos WHERE stock <= stock_minimo;