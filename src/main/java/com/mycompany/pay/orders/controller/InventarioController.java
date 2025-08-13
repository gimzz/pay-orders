package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MovimientoInventarioDAO;
import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.MovimientoInventario;
import com.mycompany.pay.orders.model.Productos;
import com.mycompany.pay.orders.model.TipoMovimiento;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class InventarioController {

    private final MovimientoInventarioDAO movimientoInventarioDAO;
    private final ProductosDAO productosDAO;

    public InventarioController(MovimientoInventarioDAO movimientoInventarioDAO, ProductosDAO productosDAO) {
        this.movimientoInventarioDAO = movimientoInventarioDAO;
        this.productosDAO = productosDAO;
    }

  public void registrarMovimiento(MovimientoInventario movimiento) throws SQLException {
    TipoMovimiento tipoMovimiento;
    try {
        tipoMovimiento = TipoMovimiento.fromString(movimiento.getTipoMovimiento());
    } catch (IllegalArgumentException e) {
        throw new SQLException("Tipo de movimiento inválido o no soportado: " + movimiento.getTipoMovimiento());
    }

    Productos producto = productosDAO.obtenerProductoPorId(movimiento.getIdProducto());
    if (producto == null) {
        throw new SQLException("Producto no encontrado con id: " + movimiento.getIdProducto());
    }
    
    int stockActual = producto.getStockActual();
    int cantidadMovimiento = movimiento.getCantidad();

    if (tipoMovimiento == TipoMovimiento.SALIDA) {
        if (movimiento.getMotivo() == null || movimiento.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar el motivo para un egreso (salida) de inventario.");
        }
    }

    switch (tipoMovimiento) {
        case ENTRADA:
            stockActual += cantidadMovimiento;
            break;
        case SALIDA:
            if (stockActual < cantidadMovimiento) {
                throw new SQLException("Stock insuficiente para este movimiento.");
            }
            stockActual -= cantidadMovimiento;
            break;
        case AJUSTE:
            stockActual += cantidadMovimiento;
            if (stockActual < 0) {
                throw new SQLException("El ajuste lleva el stock a valor negativo, operación no permitida.");
            }
            break;
        default:
            throw new SQLException("Error inesperado: tipo de movimiento desconocido.");
    }

    producto.setStockActual(stockActual);
    productosDAO.actualizarProducto(producto);

    movimiento.setFechaMovimiento(LocalDateTime.now());
    movimientoInventarioDAO.agregarMovimientos(movimiento);
}


    public List<MovimientoInventario> obtenerMovimientosPorProducto(int idProducto) throws SQLException {
        return movimientoInventarioDAO.obtenerMovimientoPorProducto(idProducto);
    }

    public List<MovimientoInventario> obtenerMovimientosPorFecha(LocalDateTime desde, LocalDateTime hasta) throws SQLException {
        return movimientoInventarioDAO.obtenerMovimientosPorFecha(desde, hasta);
    }

}
