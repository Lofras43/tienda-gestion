INSERT INTO ventas (fecha_venta, total, usuario_id) VALUES ('2026-06-25 10:30:00', 22.50, 1);
SET @venta1 = LAST_INSERT_ID();
INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(@venta1, 1, 3, 5.50, 16.50),
(@venta1, 2, 2, 3.00, 6.00);

INSERT INTO ventas (fecha_venta, total, usuario_id) VALUES ('2026-06-25 15:45:00', 22.00, 2);
SET @venta2 = LAST_INSERT_ID();
INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(@venta2, 3, 5, 2.00, 10.00),
(@venta2, 9, 3, 4.00, 12.00);

INSERT INTO compras (fecha_compra, total, proveedor, numero_factura) VALUES ('2026-06-25 08:00:00', 147.50, 'Distribuidora Mayorista SAC', 'F001-000001');
SET @compra1 = LAST_INSERT_ID();
INSERT INTO detalle_compras (compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(@compra1, 1, 20, 4.00, 80.00),
(@compra1, 6, 15, 4.50, 67.50);

INSERT INTO compras (fecha_compra, total, proveedor, numero_factura) VALUES ('2026-06-25 09:15:00', 106.00, 'Corporación Alimentaria EIRL', 'F001-000002');
SET @compra2 = LAST_INSERT_ID();
INSERT INTO detalle_compras (compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(@compra2, 3, 30, 1.20, 36.00),
(@compra2, 7, 25, 2.80, 70.00);
