package com.tienda.tienda_gestion.util;

import com.tienda.tienda_gestion.model.Producto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductoValidator {
    
    public static List<String> validar(Producto producto) {
        List<String> errores = new ArrayList<>();
        
        if (producto == null) {
            errores.add("El producto no puede ser null");
            return errores;
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            errores.add("El nombre del producto es obligatorio");
        } else if (producto.getNombre().length() > 200) {
            errores.add("El nombre del producto no puede exceder 200 caracteres");
        }
        
        if (producto.getPrecioVenta() == null) {
            errores.add("El precio de venta es obligatorio");
        } else if (producto.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El precio de venta debe ser mayor a 0");
        }
        
        if (producto.getPrecioCompra() != null && producto.getPrecioCompra().compareTo(BigDecimal.ZERO) < 0) {
            errores.add("El precio de compra no puede ser negativo");
        }
        
        if (producto.getStock() == null) {
            errores.add("El stock es obligatorio");
        } else if (producto.getStock() < 0) {
            errores.add("El stock no puede ser negativo");
        }
        
        if (producto.getStockMinimo() == null) {
            errores.add("El stock mínimo es obligatorio");
        } else if (producto.getStockMinimo() < 0) {
            errores.add("El stock mínimo no puede ser negativo");
        }
        
        if (producto.getPrecioVenta() != null && producto.getPrecioCompra() != null) {
            if (producto.getPrecioVenta().compareTo(producto.getPrecioCompra()) < 0) {
                errores.add("El precio de venta no puede ser menor al precio de compra");
            }
        }
        
        return errores;
    }
    
    public static boolean esValido(Producto producto) {
        return validar(producto).isEmpty();
    }
    
    public static boolean necesitaReposicion(Producto producto) {
        if (producto == null || producto.getStockMinimo() == null) {
            return false;
        }
        return producto.getStock() <= producto.getStockMinimo();
    }
}