package com.tienda.tienda_gestion.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenDashboardDTO {
    private int totalProductos;
    private int productosActivos;
    private int productosStockBajo;
    private int productosPorVencer;
    private int productosVencidos;
    private BigDecimal ventasDia;
    private BigDecimal ventasMes;
    private BigDecimal comprasMes;
    private BigDecimal gananciaMes;
    private int totalVentas;
    private int totalCompras;
}