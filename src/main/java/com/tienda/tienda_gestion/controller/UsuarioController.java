package com.tienda.tienda_gestion.controller;

import com.tienda.tienda_gestion.model.Usuario;
import com.tienda.tienda_gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String registroPost(@ModelAttribute Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está registrado");
            return "registro";
        }
        
        usuario.setRol("USUARIO");
        usuarioService.save(usuario);
        
        redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
        return "redirect:/login";
    }
}