package com.tienda.tienda_gestion.constant;

public class AppConstants {
    
    public static final String APP_NAME = "Tienda Gestión";
    public static final String APP_VERSION = "1.0.0";
    
    public static final int DIAS_POR_VENCER = 7;
    public static final int STOCK_MINIMO_DEFAULT = 10;
    
    public static final String MONEDA_SIMBOLO = "S/ ";
    public static final String MONEDA_CODIGO = "PEN";
    public static final String MONEDA_NOMBRE = "Sol Peruano";
    
    public static final String ROL_ADMINISTRADOR = "ADMIN";
    public static final String ROL_USUARIO = "USUARIO";
    
    public static final String MENSAJE_EXITO = "Operación exitosa";
    public static final String MENSAJE_ERROR = "Ha ocurrido un error";
    public static final String MENSAJE_REGISTRO_EXITOSO = "Registro realizado exitosamente";
    public static final String MENSAJE_ACTUALIZACION_EXITOSA = "Actualización realizada exitosamente";
    public static final String MENSAJE_ELIMINACION_EXITOSA = "Eliminación realizada exitosamente";
    
    public static final String CATEGORIA_ABARROTES = "Abarrotes";
    public static final String CATEGORIA_BEBIDAS = "Bebidas";
    public static final String CATEGORIA_LACTEOS = "Lácteos";
    public static final String CATEGORIA_PANADERIA = "Panadería";
    public static final String CATEGORIA_CONFITERIA = "Confitería";
    public static final String CATEGORIA_LIMPIEZA = "Limpieza";
    public static final String CATEGORIA_OTROS = "Otros";
    
    public static final String[] CATEGORIAS = {
        CATEGORIA_ABARROTES,
        CATEGORIA_BEBIDAS,
        CATEGORIA_LACTEOS,
        CATEGORIA_PANADERIA,
        CATEGORIA_CONFITERIA,
        CATEGORIA_LIMPIEZA,
        CATEGORIA_OTROS
    };
    
    public static final String FECHA_FORMATO = "dd/MM/yyyy";
    public static final String FECHA_HORA_FORMATO = "dd/MM/yyyy HH:mm";
    public static final String HORA_FORMATO = "HH:mm";
    
    private AppConstants() {
    }
}