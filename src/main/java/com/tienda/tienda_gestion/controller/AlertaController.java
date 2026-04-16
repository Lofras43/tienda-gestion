package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/alertas")
public class AlertaController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public String listarAlertas(Model model) {
        List<Producto> stockBajo = productoService.findByStockMinimo();
        List<Producto> porVencer = productoService.findByProductosPorVencer(7);
        List<Producto> vencidos = productoService.findByProductosVencidos();
        
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("porVencer", porVencer);
        model.addAttribute("vencidos", vencidos);
        
        return "alertas";
    }
}