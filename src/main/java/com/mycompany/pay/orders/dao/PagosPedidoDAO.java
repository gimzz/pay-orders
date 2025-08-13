package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.PagosPedido;
import java.sql.SQLException;
import java.util.List;

public interface PagosPedidoDAO {

    void registrarPago(PagosPedido pago) throws SQLException;
    List<PagosPedido> obtenerPagosPorPedido(int idPedido) throws SQLException;
    double obtenerTotalPagadoPorPedido(int idPedido) throws SQLException;
    void eliminarPago(int idPago) throws SQLException;
}
