package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.dao.DetalleVentaRepository;
import com.tienda.tienda_gestion.model.DetalleVenta;
import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.service.ProductoService;
import com.tienda.tienda_gestion.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @GetMapping
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "ventas";
    }
    
    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("detalles", new ArrayList<DetalleVenta>());
        return "ventas-form";
    }
    
    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam List<Long> productoIds,
                               @RequestParam List<Integer> cantidades,
                               @RequestParam List<Double> precios,
                               RedirectAttributes redirectAttributes) {
        try {
            List<DetalleVenta> detalles = new ArrayList<>();
            for (int i = 0; i < productoIds.size(); i++) {
                DetalleVenta detalle = new DetalleVenta();
                Producto producto = new Producto();
                producto.setId(productoIds.get(i));
                detalle.setProducto(producto);
                detalle.setCantidad(cantidades.get(i));
                detalle.setPrecioUnitario(new java.math.BigDecimal(precios.get(i)));
                detalles.add(detalle);
            }
            ventaService.registrarVenta(detalles, null);
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }
    
    @GetMapping("/ver/{id}")
    public String verVenta(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return ventaService.findById(id)
                .map(venta -> {
                    List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(id);
                    model.addAttribute("venta", venta);
                    model.addAttribute("detalles", detalles);
                    return "ventas-ver";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/ventas";
                });
    }
}