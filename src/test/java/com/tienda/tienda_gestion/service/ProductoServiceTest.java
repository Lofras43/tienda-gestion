package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche Gloria");
        producto.setStock(50);
        producto.setStockMinimo(10);
        producto.setPrecioVenta(new BigDecimal("5.50"));
        producto.setPrecioCompra(new BigDecimal("4.00"));
        producto.setEstaActivo(true);
    }

    @Test
    void actualizarStock_deberiaReducirStockCorrectamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        boolean resultado = productoService.actualizarStock(1L, 10);

        assertTrue(resultado);
        assertEquals(40, producto.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarStock_deberiaRetornarFalseSiProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = productoService.actualizarStock(99L, 10);

        assertFalse(resultado);
        verify(productoRepository, never()).save(any());
    }

    @Test
    void aumentarStock_deberiaIncrementarStockCorrectamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        boolean resultado = productoService.aumentarStock(1L, 20);

        assertTrue(resultado);
        assertEquals(70, producto.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void aumentarStock_deberiaRetornarFalseSiProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = productoService.aumentarStock(99L, 20);

        assertFalse(resultado);
        verify(productoRepository, never()).save(any());
    }

    @Test
    void save_deberiaGuardarProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto guardado = productoService.save(producto);

        assertNotNull(guardado);
        assertEquals("Leche Gloria", guardado.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void findById_deberiaRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Leche Gloria", resultado.get().getNombre());
    }

    @Test
    void deleteById_deberiaEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(productoRepository).deleteById(1L);
    }
}