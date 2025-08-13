/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MovimientoInventario;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author gimz
 */


public interface MovimientoInventarioDAO {

    void agregarMovimientos(MovimientoInventario movimiento) throws SQLException;

    List<MovimientoInventario> obtenerMovimientoPorProducto(int idProducto) throws SQLException;

    List<MovimientoInventario> obtenerMovimientosPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws SQLException;

    void eliminarMovimiento(int id) throws SQLException;
}
