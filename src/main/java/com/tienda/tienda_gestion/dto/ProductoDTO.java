package com.tienda.tienda_gestion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private BigDecimal precioCompra;
    private Integer stock;
    private Integer stockMinimo;
    private String fechaVencimiento;
    private String categoria;
    private Boolean estaActivo;
    private Boolean bajoStock;
    private Boolean porVencer;
    private Boolean vencido;
}