package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.Productos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductosController {

    private ProductosDAO productosDAO;

    public ProductosController(ProductosDAO productosDAO) {
        this.productosDAO = productosDAO;
    }

    public void agregarProducto(Productos producto) throws SQLException, IllegalArgumentException {
        validarProducto(producto, true);
        productosDAO.agregarProducto(producto);
    }

    public Productos obtenerProductoPorId(int id) throws SQLException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que 0");
        }
        return productosDAO.obtenerProductoPorId(id);
    }

    public List<Productos> obtenerTodosLosProductos() throws SQLException {
        return productosDAO.obtenerTodosLosProductos();
    }

    public void actualizarProducto(Productos producto) throws SQLException, IllegalArgumentException {
        if (producto.getId() <= 0) {
            throw new IllegalArgumentException("El ID del producto para actualizar debe ser mayor que 0");
        }
        validarProducto(producto, false);
        productosDAO.actualizarProducto(producto);
    }

    public void eliminarProducto(int id) throws SQLException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto a eliminar debe ser mayor que 0");
        }
        productosDAO.eliminarProducto(id);
    }

    public List<Productos> obtenerProductosActivos() throws SQLException {
        return productosDAO.obtenerProductosActivos();
    }

    public List<Productos> obtenerProductosPorDebajoDelStockMinimo() throws SQLException {
        return productosDAO.obtenerProductosPorDebajoDelStockMinimo();
    }

    // Validaciones básicas para el objeto Productos antes de insertar o actualizar
    private void validarProducto(Productos producto, boolean esNuevo) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecioUsd() == null || producto.getPrecioUsd().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor positivo o cero");
        }
        if (producto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo");
        }
        if (producto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        if (producto.getStockMinimo() > producto.getStockActual()) {
            throw new IllegalArgumentException("El stock mínimo no puede ser mayor que el stock actual");
        }
    }
}
