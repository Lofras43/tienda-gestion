package com.tienda.tienda_gestion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("errorCode", ex.getErrorCode());
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("errorCode", "RESOURCE_NOT_FOUND");
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficienteException(StockInsuficienteException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("errorCode", "STOCK_INSUFFICIENT");
        response.put("productoId", ex.getProductoId());
        response.put("stockActual", ex.getStockActual());
        response.put("stockSolicitado", ex.getStockSolicitado());
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Ha ocurrido un error inesperado");
        response.put("message", ex.getMessage());
        response.put("errorCode", "INTERNAL_ERROR");
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public static String getErrorMessage(Exception ex, RedirectAttributes redirectAttributes) {
        String message = ex.getMessage();
        if (message == null || message.isEmpty()) {
            message = "Ha ocurrido un error";
        }
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "error";
    }
}