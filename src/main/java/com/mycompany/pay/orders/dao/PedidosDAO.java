/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.DetallePedido;
import com.mycompany.pay.orders.model.Pedidos;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author gimz
 */
public interface PedidosDAO {
    void crearPedido(Pedidos pedido) throws SQLException;
    Pedidos obtenerPedidoPorId(int id) throws SQLException;
void actualizarEstadoEntrega(int pedidoId, boolean entregado) throws SQLException;
void actualizarEstadoPago(int pedidoId, String estadoPago) throws SQLException;
    void eliminarPedido(int id) throws SQLException;
    List<Pedidos> obtenerTodosLosPedidos() throws SQLException;
    List<Pedidos> obtenerPedidosEntregados() throws SQLException;
    List<Pedidos> obtenerPedidosNoEntregados() throws SQLException;
    String obtenerEstadoPago(int pedidoId) throws SQLException;
public void actualizarTotalPedido(int pedidoId, BigDecimal total) throws SQLException;
}
