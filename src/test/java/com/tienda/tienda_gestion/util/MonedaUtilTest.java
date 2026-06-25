package com.tienda.tienda_gestion.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MonedaUtilTest {

    @Test
    void formatearSoles_deberiaFormatearMontoCorrectamente() {
        String resultado = MonedaUtil.formatearSoles(new BigDecimal("1234.56"));
        assertTrue(resultado.startsWith("S/ "));
        assertTrue(resultado.contains("1,234.56") || resultado.contains("1234.56"));
    }

    @Test
    void formatearSoles_deberiaRetornarCeroSiMontoEsNull() {
        assertEquals("S/ 0.00", MonedaUtil.formatearSoles((BigDecimal) null));
    }

    @Test
    void calcularSubtotal_deberiaCalcularCorrectamente() {
        BigDecimal subtotal = MonedaUtil.calcularSubtotal(new BigDecimal("10.50"), 3);
        assertEquals(0, new BigDecimal("31.50").compareTo(subtotal));
    }

    @Test
    void calcularTotal_deberiaSumarMontos() {
        BigDecimal total = MonedaUtil.calcularTotal(
            new BigDecimal("100.00"),
            new BigDecimal("50.50"),
            new BigDecimal("25.25")
        );
        assertEquals(0, new BigDecimal("175.75").compareTo(total));
    }

    @Test
    void calcularGanancia_deberiaCalcularDiferencia() {
        BigDecimal ganancia = MonedaUtil.calcularGanancia(
            new BigDecimal("1000.00"),
            new BigDecimal("600.00")
        );
        assertEquals(0, new BigDecimal("400.00").compareTo(ganancia));
    }

    @Test
    void calcularPorcentaje_deberiaCalcularCorrectamente() {
        BigDecimal porcentaje = MonedaUtil.calcularPorcentaje(
            new BigDecimal("25.00"),
            new BigDecimal("100.00")
        );
        assertEquals(0, new BigDecimal("25.0000").compareTo(porcentaje));
    }

    @Test
    void calcularPorcentaje_deberiaRetornarCeroSiTodoEsCero() {
        assertEquals(BigDecimal.ZERO, MonedaUtil.calcularPorcentaje(
            new BigDecimal("10.00"), BigDecimal.ZERO));
    }
}