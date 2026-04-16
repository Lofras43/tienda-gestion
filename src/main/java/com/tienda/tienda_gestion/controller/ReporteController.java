package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dto.ResumenDashboardDTO;
import com.tienda.tienda_gestion.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    @GetMapping
    public String reportes(Model model) {
        ResumenDashboardDTO resumen = reporteService.generarResumenDashboard();
        
        model.addAttribute("totalProductos", resumen.getTotalProductos());
        model.addAttribute("productosActivos", resumen.getProductosActivos());
        model.addAttribute("productosStockBajo", resumen.getProductosStockBajo());
        model.addAttribute("productosPorVencer", resumen.getProductosPorVencer());
        model.addAttribute("productosVencidos", resumen.getProductosVencidos());
        model.addAttribute("totalVentas", resumen.getVentasMes());
        model.addAttribute("totalCompras", resumen.getComprasMes());
        model.addAttribute("ganancia", resumen.getGananciaMes());
        model.addAttribute("totalVentasCount", resumen.getTotalVentas());
        model.addAttribute("totalComprasCount", resumen.getTotalCompras());
        model.addAttribute("valorInventario", reporteService.calcularValorInventario());
        
        return "reportes";
    }
}