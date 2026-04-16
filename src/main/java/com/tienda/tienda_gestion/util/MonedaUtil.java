package com.tienda.tienda_gestion.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MonedaUtil {
    
    private static final Locale LOCALE_PERU = new Locale("es", "PE");
    private static final String SIMBOLO_MONEDA = "S/ ";
    
    public static String formatearSoles(BigDecimal monto) {
        if (monto == null) {
            return SIMBOLO_MONEDA + "0.00";
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(LOCALE_PERU);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return SIMBOLO_MONEDA + formatter.format(monto);
    }
    
    public static String formatearSoles(double monto) {
        return formatearSoles(BigDecimal.valueOf(monto));
    }
    
    public static BigDecimal calcularSubtotal(BigDecimal precioUnitario, int cantidad) {
        if (precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
    
    public static BigDecimal calcularTotal(BigDecimal... montos) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal monto : montos) {
            if (monto != null) {
                total = total.add(monto);
            }
        }
        return total;
    }
    
    public static BigDecimal calcularGanancia(BigDecimal ventas, BigDecimal compras) {
        if (ventas == null) {
            ventas = BigDecimal.ZERO;
        }
        if (compras == null) {
            compras = BigDecimal.ZERO;
        }
        return ventas.subtract(compras);
    }
    
    public static BigDecimal calcularPorcentaje(BigDecimal parte, BigDecimal todo) {
        if (todo == null || todo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (parte == null) {
            parte = BigDecimal.ZERO;
        }
        return parte.divide(todo, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
    }
}