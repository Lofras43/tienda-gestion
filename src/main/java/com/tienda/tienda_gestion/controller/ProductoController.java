package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.service.ProductoService;
import com.tienda.tienda_gestion.util.FechaUtil;
import com.tienda.tienda_gestion.util.MonedaUtil;
import com.tienda.tienda_gestion.util.ProductoValidator;
import com.tienda.tienda_gestion.dto.ProductoDTO;
import com.tienda.tienda_gestion.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductoValidator productoValidator;
    
    @Autowired
    private EntityMapper entityMapper;
    
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.findAll();
        List<ProductoDTO> productosDTO = productos.stream()
            .map(p -> entityMapper.toProductoDTO(p))
            .collect(Collectors.toList());
        model.addAttribute("productos", productos);
        model.addAttribute("productosDTO", productosDTO);
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