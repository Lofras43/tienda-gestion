package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dto.ResumenDashboardDTO;
import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.model.Venta;
import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.dao.VentaRepository;
import com.tienda.tienda_gestion.dao.CompraRepository;
import com.tienda.tienda_gestion.util.FechaUtil;
import com.tienda.tienda_gestion.util.MonedaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private CompraRepository compraRepository;
    
    public ResumenDashboardDTO generarResumenDashboard() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        
        List<Producto> productos = productoRepository.findAll();
        List<Producto> productosActivos = productos.stream()
                .filter(p -> p.getEstaActivo() != null && p.getEstaActivo())
                .collect(Collectors.toList());
        
        int productosStockBajo = productoRepository.findByStockMinimo().size();
        int productosPorVencer = contarProductosPorVencer(productos);
        int productosVencidos = contarProductosVencidos(productos);
        
        BigDecimal ventasDia = convertirAVentaMonto(ventaRepository.sumTotalByFechaVentaBetween(inicioDia, ahora));
        BigDecimal ventasMes = convertirAVentaMonto(ventaRepository.sumTotalByFechaVentaBetween(inicioMes, ahora));
        BigDecimal comprasMes = convertirACompraMonto(compraRepository.sumTotalByFechaCompraBetween(inicioMes, ahora));
        
        BigDecimal gananciaMes = MonedaUtil.calcularGanancia(ventasMes, comprasMes);
        
        int totalVentas = ventaRepository.findAll().size();
        int totalCompras = compraRepository.findAll().size();
        
        ResumenDashboardDTO resumen = new ResumenDashboardDTO();
        resumen.setTotalProductos(productos.size());
        resumen.setProductosActivos(productosActivos.size());
        resumen.setProductosStockBajo(productosStockBajo);
        resumen.setProductosPorVencer(productosPorVencer);
        resumen.setProductosVencidos(productosVencidos);
        resumen.setVentasDia(ventasDia);
        resumen.setVentasMes(ventasMes);
        resumen.setComprasMes(comprasMes);
        resumen.setGananciaMes(gananciaMes);
        resumen.setTotalVentas(totalVentas);
        resumen.setTotalCompras(totalCompras);
        
        return resumen;
    }
    
    public List<Producto> obtenerProductosMasVendidos(int limite) {
        return productoRepository.findAll().stream()
                .sorted((p1, p2) -> Integer.compare(
                        obtenerTotalVentasProducto(p2),
                        obtenerTotalVentasProducto(p1)))
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    public List<Producto> obtenerProductosMenosVendidos(int limite) {
        return productoRepository.findAll().stream()
                .sorted((p1, p2) -> Integer.compare(
                        obtenerTotalVentasProducto(p1),
                        obtenerTotalVentasProducto(p2)))
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    public BigDecimal calcularValorInventario() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getEstaActivo() != null && p.getEstaActivo())
                .map(p -> {
                    if (p.getPrecioCompra() == null || p.getStock() == null) {
                        return BigDecimal.ZERO;
                    }
                    return p.getPrecioCompra().multiply(BigDecimal.valueOf(p.getStock()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calcularValorVentas(LocalDateTime inicio, LocalDateTime fin) {
        Double monto = ventaRepository.sumTotalByFechaVentaBetween(inicio, fin);
        return convertirAVentaMonto(monto);
    }
    
    public BigDecimal calcularValorCompras(LocalDateTime inicio, LocalDateTime fin) {
        Double monto = compraRepository.sumTotalByFechaCompraBetween(inicio, fin);
        return convertirACompraMonto(monto);
    }
    
    public BigDecimal calcularGanancias(LocalDateTime inicio, LocalDateTime fin) {
        BigDecimal ventas = calcularValorVentas(inicio, fin);
        BigDecimal compras = calcularValorCompras(inicio, fin);
        return MonedaUtil.calcularGanancia(ventas, compras);
    }
    
    public int contarProductosPorVencer(List<Producto> productos) {
        return (int) productos.stream()
                .filter(p -> p.getFechaVencimiento() != null)
                .filter(p -> FechaUtil.estaPorVencer(p.getFechaVencimiento(), 7))
                .count();
    }
    
    public int contarProductosVencidos(List<Producto> productos) {
        return (int) productos.stream()
                .filter(p -> p.getFechaVencimiento() != null)
                .filter(p -> FechaUtil.estaVencido(p.getFechaVencimiento()))
                .count();
    }
    
    private int obtenerTotalVentasProducto(Producto producto) {
        return producto.getStock() != null ? producto.getStock() : 0;
    }
    
    private BigDecimal convertirAVentaMonto(Double monto) {
        if (monto == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(monto);
    }
    
    private BigDecimal convertirACompraMonto(Double monto) {
        if (monto == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(monto);
    }
}