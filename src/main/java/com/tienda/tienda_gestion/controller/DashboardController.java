package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.service.ProductoService;
import com.tienda.tienda_gestion.service.VentaService;
import com.tienda.tienda_gestion.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class DashboardController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private CompraService compraService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime inicioSemana = ahora.minusDays(7);
        
        int totalProductos = productoService.findAll().size();
        int productosStockBajo = productoService.findByStockMinimo().size();
        int productosPorVencer = productoService.findByProductosPorVencer(7).size();
        int productosVencidos = productoService.findByProductosVencidos().size();
        
        Double ventasDia = ventaService.sumTotalByFechaBetween(inicioDia, ahora);
        Double ventasMes = ventaService.sumTotalByFechaBetween(inicioMes, ahora);
        Double comprasMes = compraService.sumTotalByFechaBetween(inicioMes, ahora);
        
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("productosStockBajo", productosStockBajo);
        model.addAttribute("productosPorVencer", productosPorVencer);
        model.addAttribute("productosVencidos", productosVencidos);
        model.addAttribute("ventasDia", ventasDia != null ? ventasDia : 0);
        model.addAttribute("ventasMes", ventasMes != null ? ventasMes : 0);
        model.addAttribute("comprasMes", comprasMes != null ? comprasMes : 0);
        
        return "dashboard";
    }
}