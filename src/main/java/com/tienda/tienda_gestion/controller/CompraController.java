package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.service.CompraService;
import com.tienda.tienda_gestion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/compras")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public String listarCompras(Model model) {
        model.addAttribute("compras", compraService.findAll());
        return "compras";
    }
    
    @GetMapping("/nueva")
    public String nuevaCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("productos", productoService.findAll());
        return "compras-form";
    }
    
    @PostMapping("/guardar")
    public String guardarCompra(@ModelAttribute Compra compra,
                                 @RequestParam Long productoId,
                                 @RequestParam Integer cantidad,
                                 RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarStock(productoId, cantidad);
            compra.setTotal(compra.getTotal());
            compraService.registrarCompra(compra);
            redirectAttributes.addFlashAttribute("success", "Compra registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar compra: " + e.getMessage());
        }
        return "redirect:/compras";
    }
}