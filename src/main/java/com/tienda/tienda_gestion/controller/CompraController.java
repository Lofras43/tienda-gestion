package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.model.DetalleCompra;
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
    public String guardarCompra(@RequestParam String proveedor,
                         @RequestParam(required = false) String numeroFactura,
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
            
            if (productoId == null || productoId <= 0) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un producto válido");
                return "redirect:/compras/nueva";
            }
            
            if (proveedor == null || proveedor.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El proveedor es obligatorio");
                return "redirect:/compras/nueva";
            }
            
            java.math.BigDecimal total = java.math.BigDecimal.valueOf(cantidad * precioUnitario);
            
            Compra compra = new Compra();
            compra.setProveedor(proveedor);
            compra.setNumeroFactura(numeroFactura != null ? numeroFactura : "FAC-" + System.currentTimeMillis());
            compra.setFechaCompra(java.time.LocalDateTime.now());
            compra.setTotal(total);
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(new com.tienda.tienda_gestion.model.Producto());
            detalle.getProducto().setId(productoId);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(java.math.BigDecimal.valueOf(precioUnitario));
            detalle.setSubtotal(total);
            detalle.setCompra(compra);
            
            java.util.List<DetalleCompra> detalles = new java.util.ArrayList<>();
            detalles.add(detalle);
            
            compraService.registrarCompra(compra, detalles);
            productoService.aumentarStock(productoId, cantidad);
            
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