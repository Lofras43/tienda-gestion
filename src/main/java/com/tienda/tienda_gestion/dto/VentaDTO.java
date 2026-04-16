package com.tienda.tienda_gestion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private String usuarioNombre;
    private Integer cantidadProductos;
    private String productosNombres;
}