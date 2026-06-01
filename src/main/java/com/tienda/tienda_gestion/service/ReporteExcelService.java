package com.tienda.tienda_gestion.service;

import com.tienda.tienda_gestion.model.Producto;
import com.tienda.tienda_gestion.model.Venta;
import com.tienda.tienda_gestion.model.Compra;
import com.tienda.tienda_gestion.model.DetalleVenta;
import com.tienda.tienda_gestion.model.DetalleCompra;
import com.tienda.tienda_gestion.dao.ProductoRepository;
import com.tienda.tienda_gestion.dao.VentaRepository;
import com.tienda.tienda_gestion.dao.CompraRepository;
import com.tienda.tienda_gestion.util.FechaUtil;
import com.tienda.tienda_gestion.util.MonedaUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteExcelService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CompraRepository compraRepository;

    public byte[] generarExcelCompleto() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        hojaResumen(workbook);
        hojaProductos(workbook);
        hojaVentas(workbook);
        hojaCompras(workbook);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    private void hojaResumen(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Resumen");
        CellStyle headerStyle = estiloCabecera(workbook);
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);

        String[][] datos = {
            {"Indicador", "Valor"},
            {"Total de productos", String.valueOf(productoRepository.count())},
            {"Valor del inventario", MonedaUtil.formatearSoles(
                productoRepository.findAll().stream()
                    .filter(p -> p.getEstaActivo() != null && p.getEstaActivo())
                    .map(p -> {
                        if (p.getPrecioCompra() == null || p.getStock() == null) return java.math.BigDecimal.ZERO;
                        return p.getPrecioCompra().multiply(java.math.BigDecimal.valueOf(p.getStock()));
                    })
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
            )},
            {"Total de ventas", String.valueOf(ventaRepository.count())},
            {"Total de compras", String.valueOf(compraRepository.count())}
        };

        for (int i = 0; i < datos.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < datos[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(datos[i][j]);
                cell.setCellStyle(i == 0 ? headerStyle : dataStyle);
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void hojaProductos(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Productos");
        CellStyle headerStyle = estiloCabecera(workbook);

        String[] headers = {"ID", "Nombre", "Precio Venta", "Precio Compra", "Stock", "Stock Mínimo", "Categoría", "Activo"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Producto> productos = productoRepository.findAll();
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
            row.createCell(1).setCellValue(p.getNombre() != null ? p.getNombre() : "");
            row.createCell(2).setCellValue(p.getPrecioVenta() != null ? p.getPrecioVenta().doubleValue() : 0);
            row.createCell(3).setCellValue(p.getPrecioCompra() != null ? p.getPrecioCompra().doubleValue() : 0);
            row.createCell(4).setCellValue(p.getStock() != null ? p.getStock() : 0);
            row.createCell(5).setCellValue(p.getStockMinimo() != null ? p.getStockMinimo() : 0);
            row.createCell(6).setCellValue(p.getCategoria() != null ? p.getCategoria() : "");
            row.createCell(7).setCellValue(p.getEstaActivo() != null && p.getEstaActivo() ? "Sí" : "No");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void hojaVentas(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Ventas");
        CellStyle headerStyle = estiloCabecera(workbook);

        String[] headers = {"ID", "Fecha", "Producto", "Cantidad", "Precio Unit.", "Subtotal", "Total Venta"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Venta> ventas = ventaRepository.findAll();
        int rowNum = 1;
        for (Venta venta : ventas) {
            List<DetalleVenta> detalles = venta.getDetalles();
            if (detalles == null || detalles.isEmpty()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(venta.getId() != null ? venta.getId() : 0);
                row.createCell(1).setCellValue(venta.getFechaVenta() != null ? FechaUtil.formatearFechaHora(venta.getFechaVenta()) : "");
                row.createCell(5).setCellValue(venta.getTotal() != null ? venta.getTotal().doubleValue() : 0);
            } else {
                for (DetalleVenta d : detalles) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(venta.getId() != null ? venta.getId() : 0);
                    row.createCell(1).setCellValue(venta.getFechaVenta() != null ? FechaUtil.formatearFechaHora(venta.getFechaVenta()) : "");
                    row.createCell(2).setCellValue(d.getProducto() != null && d.getProducto().getNombre() != null ? d.getProducto().getNombre() : "");
                    row.createCell(3).setCellValue(d.getCantidad() != null ? d.getCantidad() : 0);
                    row.createCell(4).setCellValue(d.getPrecioUnitario() != null ? d.getPrecioUnitario().doubleValue() : 0);
                    row.createCell(5).setCellValue(d.getSubtotal() != null ? d.getSubtotal().doubleValue() : 0);
                    row.createCell(6).setCellValue(venta.getTotal() != null ? venta.getTotal().doubleValue() : 0);
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void hojaCompras(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Compras");
        CellStyle headerStyle = estiloCabecera(workbook);

        String[] headers = {"ID", "Fecha", "Proveedor", "N° Factura", "Producto", "Cantidad", "Precio Unit.", "Subtotal", "Total Compra"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Compra> compras = compraRepository.findAll();
        int rowNum = 1;
        for (Compra compra : compras) {
            List<DetalleCompra> detalles = compra.getDetalles();
            if (detalles == null || detalles.isEmpty()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(compra.getId() != null ? compra.getId() : 0);
                row.createCell(1).setCellValue(compra.getFechaCompra() != null ? FechaUtil.formatearFechaHora(compra.getFechaCompra()) : "");
                row.createCell(2).setCellValue(compra.getProveedor() != null ? compra.getProveedor() : "");
                row.createCell(3).setCellValue(compra.getNumeroFactura() != null ? compra.getNumeroFactura() : "");
                row.createCell(7).setCellValue(compra.getTotal() != null ? compra.getTotal().doubleValue() : 0);
            } else {
                for (DetalleCompra d : detalles) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(compra.getId() != null ? compra.getId() : 0);
                    row.createCell(1).setCellValue(compra.getFechaCompra() != null ? FechaUtil.formatearFechaHora(compra.getFechaCompra()) : "");
                    row.createCell(2).setCellValue(compra.getProveedor() != null ? compra.getProveedor() : "");
                    row.createCell(3).setCellValue(compra.getNumeroFactura() != null ? compra.getNumeroFactura() : "");
                    row.createCell(4).setCellValue(d.getProducto() != null && d.getProducto().getNombre() != null ? d.getProducto().getNombre() : "");
                    row.createCell(5).setCellValue(d.getCantidad() != null ? d.getCantidad() : 0);
                    row.createCell(6).setCellValue(d.getPrecioUnitario() != null ? d.getPrecioUnitario().doubleValue() : 0);
                    row.createCell(7).setCellValue(d.getSubtotal() != null ? d.getSubtotal().doubleValue() : 0);
                    row.createCell(8).setCellValue(compra.getTotal() != null ? compra.getTotal().doubleValue() : 0);
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle estiloCabecera(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }
}