package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.DetallePedido;
import com.mycompany.pay.orders.model.Pedidos;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PedidosDAO {
    void crearPedido(Pedidos pedido) throws SQLException;
    Pedidos obtenerPedidoPorId(int id) throws SQLException;
    void actualizarEstadoEntrega(int pedidoId, boolean entregado) throws SQLException;
    void eliminarPedido(int id) throws SQLException;
    List<Pedidos> obtenerTodosLosPedidos() throws SQLException;
    List<Pedidos> obtenerPedidosEntregados() throws SQLException;
    List<Pedidos> obtenerPedidosNoEntregados() throws SQLException;
    void actualizarTotalPedido(int pedidoId, BigDecimal total) throws SQLException;
    String obtenerMetodoPago(int pedidoId) throws SQLException;
    boolean obtenerPagadoPorPedido(int pedidoId) throws SQLException;
    void actualizarPagado(int pedidoId, boolean pagado) throws SQLException;
    int obtenerIdMetodoPago(int idPedido) throws SQLException;
}
