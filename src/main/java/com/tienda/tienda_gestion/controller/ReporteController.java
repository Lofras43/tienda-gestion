package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dto.ResumenDashboardDTO;
import com.tienda.tienda_gestion.service.ReporteService;
import com.tienda.tienda_gestion.service.ReporteExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteExcelService reporteExcelService;
    
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

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarExcel() {
        try {
            byte[] excelBytes = reporteExcelService.generarExcelCompleto();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reporte_tienda.xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}