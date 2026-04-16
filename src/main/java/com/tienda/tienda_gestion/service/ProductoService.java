package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
    
    public List<Producto> findByEstaActivo(Boolean estaActivo) {
        return productoRepository.findByEstaActivo(estaActivo);
    }
    
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }
    
    public List<Producto> findByStockMinimo() {
        return productoRepository.findByStockMinimo();
    }
    
    public List<Producto> findByProductosPorVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return productoRepository.findByFechaVencimientoBefore(fechaLimite);
    }
    
    public List<Producto> findByProductosVencidos() {
        LocalDate today = LocalDate.now();
        return productoRepository.findByFechaVencimientoBefore(today);
    }
    
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
    
    public boolean actualizarStock(Long productoId, Integer cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(producto.getStock() + cantidad);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }
}