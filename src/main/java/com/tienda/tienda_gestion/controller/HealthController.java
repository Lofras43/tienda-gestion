package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.dao.VentaRepository;
import com.tienda.tienda_gestion.dao.CompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("estado", "UP");
        status.put("aplicacion", "Tienda Gestión");
        status.put("version", "1.0.0");
        status.put("timestamp", java.time.LocalDateTime.now().toString());

        try {
            long totalProductos = productoRepository.count();
            long totalVentas = ventaRepository.count();
            long totalCompras = compraRepository.count();

            Map<String, Object> db = new LinkedHashMap<>();
            db.put("estado", "UP");
            db.put("totalProductos", totalProductos);
            db.put("totalVentas", totalVentas);
            db.put("totalCompras", totalCompras);
            status.put("baseDeDatos", db);

            log.info("Health check OK - Productos: {}, Ventas: {}, Compras: {}",
                totalProductos, totalVentas, totalCompras);

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            Map<String, Object> db = new LinkedHashMap<>();
            db.put("estado", "DOWN");
            db.put("error", e.getMessage());
            status.put("baseDeDatos", db);
            status.put("estado", "DEGRADED");

            log.warn("Health check: base de datos no disponible - {}", e.getMessage());

            return ResponseEntity.ok(status);
        }
    }

    @GetMapping("/api/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("aplicacion", "Tienda Gestión");
        info.put("version", "1.0.0");
        info.put("descripcion", "Sistema de Gestión Inteligente para Tiendas Pequeñas");
        info.put("tecnologia", "Spring Boot 2.7.18 + Java 21 + MySQL");
        info.put("repositorio", "https://github.com/Lofras43/tienda-gestion");
        return ResponseEntity.ok(info);
    }
}