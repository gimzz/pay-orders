package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Pedidos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.pay.orders.dao.PedidosDAO;

public class PedidosDAOImpl implements PedidosDAO {

    private Connection connection;

    public PedidosDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crearPedido(Pedidos pedido) throws SQLException {
        String sql = "INSERT INTO system.pedidos (cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getClienteId());
            ps.setTimestamp(2, pedido.getFechaPedido() != null ? Timestamp.valueOf(pedido.getFechaPedido()) : null);
            ps.setBigDecimal(3, pedido.getTotalUsd());
            ps.setBigDecimal(4, pedido.getTasaCambioAplicada());
            ps.setBoolean(5, pedido.isEntregado());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crear pedido falló, no se insertó ningún registro.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se obtuvo el ID generado para el pedido.");
                }
            }
        }
    }

    @Override
    public Pedidos obtenerPedidoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.pedidos WHERE id = ?";

        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Timestamp timestamp = rs.getTimestamp("fecha");
                java.time.LocalDateTime fechaPedido = timestamp != null ? timestamp.toLocalDateTime() : null;

                return new Pedidos(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        fechaPedido,
                        rs.getBigDecimal("total_usd"),
                        rs.getBigDecimal("tasa_cambio_aplicada"),
                        rs.getBoolean("entregado")
                );
            }
        }
    }
    @Override
public void actualizarEstadoPago(int pedidoId, String estadoPago) throws SQLException {
    String sql = "UPDATE system.pedidos SET estado_pago = ? WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, estadoPago);
        ps.setInt(2, pedidoId);
        int rows = ps.executeUpdate();
        if (rows == 0) {
            throw new SQLException("No se encontró pedido con ID " + pedidoId);
        }
    }
}
@Override
public String obtenerEstadoPago(int pedidoId) throws SQLException {
    String sql = "SELECT estado_pago FROM system.pedidos WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, pedidoId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("estado_pago");
            } else {
                throw new SQLException("Pedido no encontrado");
            }
        }
    }
}
    
@Override
public void actualizarEstadoEntrega(int pedidoId, boolean entregado) throws SQLException {
    String sql = "UPDATE system.pedidos SET entregado = ? WHERE id = ?";

    try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
        ps.setBoolean(1, entregado);
        ps.setInt(2, pedidoId);
        int rows = ps.executeUpdate();

        if (rows == 0) {
            throw new SQLException("No se encontró ningún pedido con ID " + pedidoId + " para actualizar.");
        }
    }
}



    @Override
    public void eliminarPedido(int id) throws SQLException {
        String sql = "DELETE FROM system.pedidos WHERE id = ?";

        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró ningún pedido con ID " + id + " para eliminar.");
            }
        }
    }

    @Override
    public List<Pedidos> obtenerTodosLosPedidos() throws SQLException {
        String sql = "SELECT * FROM system.pedidos";
        List<Pedidos> pedidos = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedidos pedido = mapearPedido(rs);
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    public List<Pedidos> obtenerPedidosEntregados() throws SQLException {
        return obtenerPedidosPorEstadoEntrega(true);
    }

    @Override
    public List<Pedidos> obtenerPedidosNoEntregados() throws SQLException {
        return obtenerPedidosPorEstadoEntrega(false);
    }

    private List<Pedidos> obtenerPedidosPorEstadoEntrega(boolean entregado) throws SQLException {
        String sql = "SELECT * FROM system.pedidos WHERE entregado = ?";
        List<Pedidos> pedidos = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setBoolean(1, entregado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedidos pedido = mapearPedido(rs);
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }

    private Pedidos mapearPedido(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("fecha");
        java.time.LocalDateTime fechaPedido = timestamp != null ? timestamp.toLocalDateTime() : null;

        return new Pedidos(
                rs.getInt("id"),
                rs.getInt("cliente_id"),
                fechaPedido,
                rs.getBigDecimal("total_usd"),
                rs.getBigDecimal("tasa_cambio_aplicada"),
                rs.getBoolean("entregado")
        );
    }
}
