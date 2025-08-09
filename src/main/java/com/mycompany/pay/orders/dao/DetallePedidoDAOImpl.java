package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.DetallePedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    private Connection connection;

    public DetallePedidoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarDetalle(DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO system.detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario_usd) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Crear detalle de pedido falló, no se insertó ningún registro.");
            }
        }

    }

    @Override
    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws SQLException {
        String sql = "SELECT * FROM system.detalle_pedido WHERE id_pedido = ?";
        List<DetallePedido> detalles = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(rs.getInt("id_pedido"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario_usd"));
                    detalle.setSubtotalUsd(rs.getBigDecimal("subtotal_usd"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    @Override
    public void eliminarDetalle(int idDetalle) throws SQLException {
        String sql = "DELETE FROM system.detalle_pedido WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró detalle con id " + idDetalle + " para eliminar.");
            }
        }
    }

    public void actualizarDetalle(DetallePedido detalle) throws SQLException {
        String sql = "UPDATE system.detalle_pedido SET cantidad = ?, precio_unitario_usd = ? WHERE id_pedido = ? AND id_producto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setInt(3, detalle.getIdPedido());
            ps.setInt(4, detalle.getIdProducto());

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas == 0) {
                throw new SQLException("No se encontró ningún detalle para actualizar.");
            }
        }
    }

    public void eliminarDetallesPorPedido(int idPedido) throws SQLException {
        String sql = "DELETE FROM system.detalle_pedido WHERE id_pedido = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.executeUpdate();
        }
    }
}
