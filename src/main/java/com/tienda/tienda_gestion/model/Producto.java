package com.tienda.tienda_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;
    
    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 10;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(name = "esta_activo")
    private Boolean estaActivo = true;
}