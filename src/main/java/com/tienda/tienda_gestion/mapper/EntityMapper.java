package com.tienda.tienda_gestion.mapper;

import com.tienda.tienda_gestion.dto.ProductoDTO;
import com.tienda.tienda_gestion.dto.VentaDTO;
import com.tienda.tienda_gestion.dto.CompraDTO;
import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.model.Venta;
import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.util.FechaUtil;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {
    
    public ProductoDTO toProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setPrecioCompra(producto.getPrecioCompra());
        dto.setStock(producto.getStock());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setFechaVencimiento(FechaUtil.formatearFecha(producto.getFechaVencimiento()));
        dto.setCategoria(producto.getCategoria());
        dto.setEstaActivo(producto.getEstaActivo());
        
        if (producto.getStock() != null && producto.getStockMinimo() != null) {
            dto.setBajoStock(producto.getStock() <= producto.getStockMinimo());
        }
        
        if (producto.getFechaVencimiento() != null) {
            dto.setPorVencer(FechaUtil.estaPorVencer(producto.getFechaVencimiento(), 7));
            dto.setVencido(FechaUtil.estaVencido(producto.getFechaVencimiento()));
        }
        
        return dto;
    }
    
    public VentaDTO toVentaDTO(Venta venta) {
        if (venta == null) {
            return null;
        }
        
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTotal(venta.getTotal());
        
        if (venta.getUsuario() != null) {
            dto.setUsuarioNombre(venta.getUsuario().getNombre());
        }
        
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            dto.setCantidadProductos(venta.getDetalles().size());
            dto.setProductosNombres(venta.getDetalles().stream()
                    .map(d -> d.getProducto().getNombre())
                    .collect(Collectors.joining(", ")));
        }
        
        return dto;
    }
    
    public CompraDTO toCompraDTO(Compra compra) {
        if (compra == null) {
            return null;
        }
        
        CompraDTO dto = new CompraDTO();
        dto.setId(compra.getId());
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setTotal(compra.getTotal());
        dto.setProveedor(compra.getProveedor());
        dto.setNumeroFactura(compra.getNumeroFactura());
        
        return dto;
    }
    
    public Producto toProducto(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setStock(dto.getStock());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setCategoria(dto.getCategoria());
        producto.setEstaActivo(dto.getEstaActivo());
        
        if (dto.getFechaVencimiento() != null && !dto.getFechaVencimiento().isEmpty()) {
            producto.setFechaVencimiento(java.time.LocalDate.parse(dto.getFechaVencimiento(), 
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        return producto;
    }
}