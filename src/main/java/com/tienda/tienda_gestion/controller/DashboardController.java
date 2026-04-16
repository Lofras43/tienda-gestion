package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dto.ResumenDashboardDTO;
import com.tienda.tienda_gestion.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private ReporteService reporteService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        ResumenDashboardDTO resumen = reporteService.generarResumenDashboard();
        
        model.addAttribute("totalProductos", resumen.getTotalProductos());
        model.addAttribute("productosStockBajo", resumen.getProductosStockBajo());
        model.addAttribute("productosPorVencer", resumen.getProductosPorVencer());
        model.addAttribute("productosVencidos", resumen.getProductosVencidos());
        model.addAttribute("ventasDia", resumen.getVentasDia());
        model.addAttribute("ventasMes", resumen.getVentasMes());
        model.addAttribute("comprasMes", resumen.getComprasMes());
        
        return "dashboard";
    }
}