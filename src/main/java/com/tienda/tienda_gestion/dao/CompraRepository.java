package com.tienda.tienda_gestion.dao;

import com.tienda.tienda_gestion.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    @Query("SELECT c FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin")
    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT SUM(c.total) FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin")
    Double sumTotalByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}