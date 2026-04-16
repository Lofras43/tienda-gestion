package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.CompraRepository;
import com.tienda.tienda_gestion.model.Compra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private ProductoService productoService;
    
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }
    
    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }
    
    public List<Compra> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }
    
    public Double sumTotalByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = compraRepository.sumTotalByFechaCompraBetween(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
    
    public Compra registrarCompra(Compra compra) {
        return compraRepository.save(compra);
    }
    
    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }
}