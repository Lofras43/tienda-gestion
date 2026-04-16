package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.model.Venta;
import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.service.ProductoService;
import com.tienda.tienda_gestion.service.VentaService;
import com.tienda.tienda_gestion.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private CompraService compraService;
    
    @GetMapping
    public String reportes(Model model) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        
        List<Producto> productos = productoService.findAll();
        List<Venta> ventas = ventaService.findAll();
        List<Compra> compras = compraService.findAll();
        
        Double totalVentas = ventaService.sumTotalByFechaBetween(inicioMes, ahora);
        Double totalCompras = compraService.sumTotalByFechaBetween(inicioMes, ahora);
        
        long productosActivos = productos.stream().filter(p -> p.getEstaActivo() != null && p.getEstaActivo()).count();
        long productosStockBajo = productoService.findByStockMinimo().size();
        
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("productosActivos", productosActivos);
        model.addAttribute("productosStockBajo", productosStockBajo);
        model.addAttribute("totalVentas", totalVentas != null ? totalVentas : 0);
        model.addAttribute("totalCompras", totalCompras != null ? totalCompras : 0);
        model.addAttribute("totalVentasCount", ventas.size());
        model.addAttribute("totalComprasCount", compras.size());
        
        double ganancia = (totalVentas != null ? totalVentas : 0) - (totalCompras != null ? totalCompras : 0);
        model.addAttribute("ganancia", ganancia);
        
        return "reportes";
    }
}