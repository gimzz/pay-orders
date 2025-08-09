package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.DetallePedido;
import java.sql.SQLException;
import java.util.List;

public interface DetallePedidoDAO {

    void agregarDetalle(DetallePedido detalle) throws SQLException;

    List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws SQLException;

    void eliminarDetalle(int idDetalle) throws SQLException;

}
