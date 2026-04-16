package com.tienda.tienda_gestion.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FechaUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DATE_FORMATTER);
    }
    
    public static String formatearFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            return "";
        }
        return fechaHora.format(DATETIME_FORMATTER);
    }
    
    public static String formatearHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            return "";
        }
        return fechaHora.format(TIME_FORMATTER);
    }
    
    public static long diasEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
    
    public static long diasDesde(LocalDate fecha) {
        if (fecha == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fecha, LocalDate.now());
    }
    
    public static long diasHasta(LocalDate fecha) {
        if (fecha == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), fecha);
    }
    
    public static boolean estaVencido(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            return false;
        }
        return fechaVencimiento.isBefore(LocalDate.now());
    }
    
    public static boolean estaPorVencer(LocalDate fechaVencimiento, int diasLimite) {
        if (fechaVencimiento == null) {
            return false;
        }
        LocalDate fechaLimite = LocalDate.now().plusDays(diasLimite);
        return !fechaVencimiento.isBefore(LocalDate.now()) && !fechaVencimiento.isAfter(fechaLimite);
    }
}