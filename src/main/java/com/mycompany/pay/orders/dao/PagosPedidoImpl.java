
package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.PagosPedido;
import com.mycompany.pay.orders.model.PagosPedido.TipoMoneda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PagosPedidoImpl implements PagosPedidoDAO {

    private Connection connection;

    public PagosPedidoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registrarPago(PagosPedido pago) throws SQLException {
        String sql = "INSERT INTO system.pagos_pedido " +
                "(id_pedido, id_metodo_pago, tipo_moneda, monto, fecha_pago) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pago.getIdPedido());
            ps.setInt(2, pago.getIdMetodoPago());
            ps.setString(3, pago.getTipoMoneda().name());
            ps.setBigDecimal(4, pago.getMonto());
            ps.setTimestamp(5, Timestamp.valueOf(pago.getFechaPago()));

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el pago.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pago.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<PagosPedido> obtenerPagosPorPedido(int idPedido) throws SQLException {
        List<PagosPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM system.pagos_pedido WHERE id_pedido = ? ORDER BY fecha_pago";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PagosPedido pago = mapResultSetToPagosPedido(rs);
                    lista.add(pago);
                }
            }
        }
        return lista;
    }

    @Override
    public double obtenerTotalPagadoPorPedido(int idPedido) throws SQLException {
        String sql = "SELECT SUM(monto) as total_pagado FROM system.pagos_pedido WHERE id_pedido = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total_pagado");
                    return total != null ? total.doubleValue() : 0.0;
                }
            }
        }
        return 0.0;
    }

    @Override
    public void eliminarPago(int idPago) throws SQLException {
        String sql = "DELETE FROM system.pagos_pedido WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el pago con id " + idPago);
            }
        }
    }

    private PagosPedido mapResultSetToPagosPedido(ResultSet rs) throws SQLException {
        PagosPedido pago = new PagosPedido();
        pago.setId(rs.getInt("id"));
        pago.setIdPedido(rs.getInt("id_pedido"));
        pago.setIdMetodoPago(rs.getInt("id_metodo_pago"));

        String tipoMonedaStr = rs.getString("tipo_moneda");
        try {
            TipoMoneda tipoMoneda = TipoMoneda.valueOf(tipoMonedaStr);
            pago.setTipoMoneda(tipoMoneda);
        } catch (IllegalArgumentException | NullPointerException e) {
            pago.setTipoMoneda(null);
        }

        pago.setMonto(rs.getBigDecimal("monto"));
        Timestamp ts = rs.getTimestamp("fecha_pago");
        if (ts != null) {
            pago.setFechaPago(ts.toLocalDateTime());
        } else {
            pago.setFechaPago(null);
        }

        return pago;
    }
}