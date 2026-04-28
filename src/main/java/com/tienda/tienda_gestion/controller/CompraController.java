package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dao.DetalleCompraRepository;
import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.model.DetalleCompra;
import com.tienda.tienda_gestion.service.CompraService;
import com.tienda.tienda_gestion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
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
                         @RequestParam Double precioUnitario,
                         RedirectAttributes redirectAttributes) {
        try {
            if (cantidad == null || cantidad <= 0) {
                redirectAttributes.addFlashAttribute("error", "La cantidad debe ser mayor a 0");
                return "redirect:/compras/nueva";
            }
            
            if (precioUnitario == null || precioUnitario <= 0) {
                redirectAttributes.addFlashAttribute("error", "El precio debe ser mayor a 0");
                return "redirect:/compras/nueva";
            }
            
            if (productoId != null && productoId == -1) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un producto válido");
                return "redirect:/compras/nueva";
            }
            
            java.math.BigDecimal total = java.math.BigDecimal.valueOf(cantidad * precioUnitario);
            compra.setTotal(total);
            
            productoService.actualizarStock(productoId, cantidad);
            
            compra.setFechaCompra(java.time.LocalDateTime.now());
            compraService.registrarCompra(compra);
            
            redirectAttributes.addFlashAttribute("success", "Compra registrada - Total: S/ " + total);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar compra: " + e.getMessage());
        }
        return "redirect:/compras";
    }
    
    @GetMapping("/ver/{id}")
    public String verCompra(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return compraService.findById(id)
                .map(compra -> {
                    model.addAttribute("compra", compra);
                    return "compras-ver";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Compra no encontrada");
                    return "redirect:/compras";
                });
    }
}