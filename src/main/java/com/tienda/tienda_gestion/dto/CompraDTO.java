package com.tienda.tienda_gestion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
    private Long id;
    private LocalDateTime fechaCompra;
    private BigDecimal total;
    private String proveedor;
    private String numeroFactura;
    private String productoNombre;
    private Integer cantidad;
}