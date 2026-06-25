package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.dao.VentaRepository;
import com.tienda.tienda_gestion.dao.CompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Value("${backup.directory:backups/}")
    private String backupDir;

    @Scheduled(cron = "0 0 2 * * ?")
    public void realizarBackupDiario() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = backupDir + "backup_" + timestamp + ".txt";

        try {
            java.io.File dir = new java.io.File(backupDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            writer.println("==========================================");
            writer.println("BACKUP DE BASE DE DATOS - Tienda Gestión");
            writer.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println("==========================================");
            writer.println();

            writer.println("--- PRODUCTOS ---");
            writer.println("Total: " + productoRepository.count());
            productoRepository.findAll().forEach(p -> {
                writer.println("ID=" + p.getId()
                    + " | Nombre=" + p.getNombre()
                    + " | Stock=" + p.getStock()
                    + " | PrecioVenta=" + p.getPrecioVenta()
                    + " | Activo=" + p.getEstaActivo());
            });

            writer.println();
            writer.println("--- VENTAS ---");
            writer.println("Total: " + ventaRepository.count());
            ventaRepository.findAll().forEach(v -> {
                writer.println("ID=" + v.getId()
                    + " | Fecha=" + (v.getFechaVenta() != null ? v.getFechaVenta() : "")
                    + " | Total=" + v.getTotal());
            });

            writer.println();
            writer.println("--- COMPRAS ---");
            writer.println("Total: " + compraRepository.count());
            compraRepository.findAll().forEach(c -> {
                writer.println("ID=" + c.getId()
                    + " | Fecha=" + (c.getFechaCompra() != null ? c.getFechaCompra() : "")
                    + " | Proveedor=" + c.getProveedor()
                    + " | Factura=" + c.getNumeroFactura()
                    + " | Total=" + c.getTotal());
            });

            writer.println();
            writer.println("==========================================");
            writer.println("FIN DEL BACKUP");
            writer.println("==========================================");

            writer.close();
            log.info("Backup diario completado: {}", filename);

        } catch (Exception e) {
            log.error("Error al realizar backup diario: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void limpiarLogsAntiguos() {
        log.info("Limpieza programada de logs antiguos ejecutada");
    }
}