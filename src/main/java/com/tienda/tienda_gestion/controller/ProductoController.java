package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.service.ProductoService;
import com.tienda.tienda_gestion.util.ProductoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.trim().isEmpty()) {
                    setValue(LocalDate.parse(text, formatter));
                } else {
                    setValue(null);
                }
            }
        });
    }
    
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "productos";
    }
    
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos-form";
    }
    
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        List<String> errores = ProductoValidator.validar(producto);
        if (!errores.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.join(", ", errores));
            return "redirect:/productos/nuevo";
        }
        productoService.save(producto);
        redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
        return "redirect:/productos";
    }
    
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        productoService.findById(id).ifPresent(producto -> model.addAttribute("producto", producto));
        return "productos-form";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productoService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        return "redirect:/productos";
    }
}