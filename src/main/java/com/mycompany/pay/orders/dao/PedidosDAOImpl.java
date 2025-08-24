package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Pedidos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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
            ps.setBigDecimal(3, pedido.getTotalUsd() != null ? pedido.getTotalUsd() : BigDecimal.ZERO);
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
public int obtenerIdMetodoPago(int idPedido) throws SQLException {
    String sql = "SELECT id_metodo_pago FROM system.pagos_pedido WHERE id_pedido = ? ORDER BY fecha_pago DESC LIMIT 1";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idPedido);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_metodo_pago");
            }
        }
    }
    return 0; // Si no tiene ningún método de pago registrado
}

    @Override
    public Pedidos obtenerPedidoPorId(int id) throws SQLException {
        // <CHANGE> Incluir campo pagado en la consulta SQL
        String sql = "SELECT id, cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado, pagado FROM system.pedidos WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapearPedido(rs);
            }
        }
    }

    public List<Pedidos> obtenerPedidosPorCliente(int clienteId) throws SQLException {
        List<Pedidos> pedidos = new ArrayList<>();
        String sql = "SELECT id, cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado, pagado FROM system.pedidos WHERE cliente_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedidos pedido = mapearPedido(rs);
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }

    public void actualizarPedido(Pedidos pedido) throws SQLException {
        String sql = "UPDATE system.pedidos SET cliente_id = ?, fecha = ?, total_usd = ?, tasa_cambio_aplicada = ?, entregado = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pedido.getClienteId());
            ps.setTimestamp(2, pedido.getFechaPedido() != null ? Timestamp.valueOf(pedido.getFechaPedido()) : null);
            ps.setBigDecimal(3, pedido.getTotalUsd() != null ? pedido.getTotalUsd() : BigDecimal.ZERO);
            ps.setBigDecimal(4, pedido.getTasaCambioAplicada());
            ps.setBoolean(5, pedido.isEntregado());
            ps.setInt(6, pedido.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró pedido con ID " + pedido.getId() + " para actualizar.");
            }
        }
    }

    public void actualizarTotalPedido(int pedidoId, BigDecimal total) throws SQLException {
        String sql = "UPDATE system.pedidos SET total_usd = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, total);
            ps.setInt(2, pedidoId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró pedido con ID " + pedidoId + " para actualizar total.");
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

    public String obtenerMetodoPago(int pedidoId) throws SQLException {
        String sql = "SELECT mp.descripcion " +
                "FROM system.pagos_pedido pp " +
                "JOIN system.metodos_pago mp ON pp.id_metodo_pago = mp.id " +
                "WHERE pp.id_pedido = ? " +
                "ORDER BY pp.fecha_pago DESC " +
                "LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("descripcion");
                } else {
                    return "Desconocido";
                }
            }
        }
    }

    @Override
    public List<Pedidos> obtenerTodosLosPedidos() throws SQLException {
        String sql = "SELECT id, cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado, pagado FROM system.pedidos";
        List<Pedidos> pedidos = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
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
        String sql = "SELECT id, cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado, pagado FROM system.pedidos WHERE entregado = ?";
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
                rs.getBoolean("entregado"),
                rs.getBoolean("pagado")  // Incluir campo pagado
        );
    }

    public boolean obtenerPagadoPorPedido(int pedidoId) throws SQLException {
        String sql = "SELECT pagado FROM system.pedidos WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("pagado");
                } else {
                    throw new SQLException("Pedido no encontrado");
                }
            }
        }
    }

    @Override
    public void actualizarPagado(int pedidoId, boolean pagado) throws SQLException {
        String sql = "UPDATE system.pedidos SET pagado = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, pagado);
            ps.setInt(2, pedidoId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró pedido con ID " + pedidoId);
            }
        }
    }
}