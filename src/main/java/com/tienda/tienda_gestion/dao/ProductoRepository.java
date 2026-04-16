package com.tienda.tienda_gestion.dao;

import com.tienda.tienda_gestion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByEstaActivo(Boolean estaActivo);
    
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.estaActivo = true")
    List<Producto> findByStockMinimo();
    
    @Query("SELECT p FROM Producto p WHERE p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento <= :fecha AND p.estaActivo = true")
    List<Producto> findByFechaVencimientoBefore(LocalDate fecha);
    
    @Query("SELECT p FROM Producto p WHERE p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin AND p.estaActivo = true")
    List<Producto> findByFechaVencimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);
}