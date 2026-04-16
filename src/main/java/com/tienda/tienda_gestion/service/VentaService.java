package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.*;
import com.tienda.tienda_gestion.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoService productoService;
    
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }
    
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }
    
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }
    
    public List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    public Double sumTotalByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = ventaRepository.sumTotalByFechaVentaBetween(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
    
    public Venta registrarVenta(List<DetalleVenta> detalles, Usuario usuario) {
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDateTime.now());
        
        BigDecimal totalVenta = BigDecimal.ZERO;
        
        for (DetalleVenta detalle : detalles) {
            Optional<Producto> productoOpt = productoRepository.findById(detalle.getProducto().getId());
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                if (producto.getStock() >= detalle.getCantidad()) {
                    BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
                    detalle.setProducto(producto);
                    detalle.setVenta(venta);
                    detalle.setSubtotal(subtotal);
                    totalVenta = totalVenta.add(subtotal);
                    
                    producto.setStock(producto.getStock() - detalle.getCantidad());
                    productoRepository.save(producto);
                } else {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
                }
            }
        }
        
        venta.setTotal(totalVenta);
        Venta ventaGuardada = ventaRepository.save(venta);
        
        for (DetalleVenta detalle : detalles) {
            detalleVentaRepository.save(detalle);
        }
        
        return ventaGuardada;
    }
    
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }
}