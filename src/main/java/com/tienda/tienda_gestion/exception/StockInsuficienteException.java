package com.tienda.tienda_gestion.exception;

public class StockInsuficienteException extends RuntimeException {
    
    private final Long productoId;
    private final int stockActual;
    private final int stockSolicitado;
    
    public StockInsuficienteException(Long productoId, int stockActual, int stockSolicitado) {
        super(String.format("Stock insuficiente para el producto ID %d. Stock actual: %d, solicitado: %d", 
                productoId, stockActual, stockSolicitado));
        this.productoId = productoId;
        this.stockActual = stockActual;
        this.stockSolicitado = stockSolicitado;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public int getStockActual() {
        return stockActual;
    }
    
    public int getStockSolicitado() {
        return stockSolicitado;
    }
}