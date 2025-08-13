package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Productos;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ProductosDAO {

    void agregarProducto(Productos productos) throws SQLException;

    Productos obtenerProductoPorId(int id) throws SQLException;

    BigDecimal obtenerPrecioUnitarioProducto(int id) throws SQLException;

    List<Productos> obtenerTodosLosProductos() throws SQLException;

    void actualizarProducto(Productos productos) throws SQLException;

    void eliminarProducto(int id) throws SQLException;

    List<Productos> obtenerProductosActivos() throws SQLException;

    List<Productos> obtenerProductosPorDebajoDelStockMinimo() throws SQLException;

}
