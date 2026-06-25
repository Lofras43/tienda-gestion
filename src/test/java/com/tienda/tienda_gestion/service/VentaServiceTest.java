package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.dao.DetalleVentaRepository;
import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.dao.VentaRepository;
import com.tienda.tienda_gestion.model.DetalleVenta;
import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.model.Usuario;
import com.tienda.tienda_gestion.model.Venta;
import com.tienda.tienda_gestion.exception.StockInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VentaService ventaService;

    private Producto producto;
    private Usuario usuario;
    private List<DetalleVenta> detalles;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche Gloria");
        producto.setStock(50);
        producto.setPrecioVenta(new BigDecimal("5.50"));

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Admin");
        usuario.setEmail("admin@tienda.com");

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(5);
        detalle.setPrecioUnitario(new BigDecimal("5.50"));

        detalles = new ArrayList<>();
        detalles.add(detalle);
    }

    @Test
    void registrarVenta_deberiaCrearVentaCorrectamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArguments()[0]);

        Venta venta = ventaService.registrarVenta(detalles, usuario);

        assertNotNull(venta);
        assertEquals(usuario, venta.getUsuario());
        assertEquals(0, new BigDecimal("27.50").compareTo(venta.getTotal()));
        assertEquals(45, producto.getStock());
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleVentaRepository, times(1)).save(any(DetalleVenta.class));
    }

    @Test
    void registrarVenta_deberiaLanzarExcepcionSiStockInsuficiente() {
        DetalleVenta detalleGrande = new DetalleVenta();
        detalleGrande.setProducto(producto);
        detalleGrande.setCantidad(100);
        detalleGrande.setPrecioUnitario(new BigDecimal("5.50"));
        detalles.clear();
        detalles.add(detalleGrande);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class, () -> {
            ventaService.registrarVenta(detalles, usuario);
        });

        assertEquals(50, producto.getStock());
        verify(ventaRepository, never()).save(any());
        verify(detalleVentaRepository, never()).save(any());
    }

    @Test
    void findAll_deberiaRetornarListaDeVentas() {
        when(ventaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Venta> ventas = ventaService.findAll();

        assertNotNull(ventas);
        verify(ventaRepository).findAll();
    }

    @Test
    void deleteById_deberiaEliminarVenta() {
        doNothing().when(ventaRepository).deleteById(1L);

        ventaService.deleteById(1L);

        verify(ventaRepository).deleteById(1L);
    }
}