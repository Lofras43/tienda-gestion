package com.tienda.tienda_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(length = 200)
    private String proveedor;
    
    @Column(length = 100)
    private String numeroFactura;
    
    @Column(name = "esta_activo")
    private Boolean estaActivo = true;
    
    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
    }
}