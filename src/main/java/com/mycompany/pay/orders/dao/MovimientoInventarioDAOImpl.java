package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MovimientoInventario;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {

    private final Connection connection;

    public MovimientoInventarioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarMovimientos(MovimientoInventario movimiento) throws SQLException {
        String sql = "INSERT INTO system.movimientosinventario (id_producto, tipo_movimiento, cantidad, fecha_movimiento, motivo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, movimiento.getIdProducto());
            ps.setString(2, movimiento.getTipoMovimiento());
            ps.setInt(3, movimiento.getCantidad());
            
            LocalDateTime fechaMovimiento = movimiento.getFechaMovimiento();
            if (fechaMovimiento != null) {
                ps.setTimestamp(4, Timestamp.valueOf(fechaMovimiento));
            } else {
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }

            ps.setString(5, movimiento.getMotivo());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al insertar movimiento de inventario, no se afectaron filas.");
            }

            // Obtener id generado y asignar al objeto
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<MovimientoInventario> obtenerMovimientoPorProducto(int idProducto) throws SQLException {
        List<MovimientoInventario> listaMovimientos = new ArrayList<>();
        String sql = "SELECT * FROM system.movimientosinventario WHERE id_producto = ? ORDER BY fecha_movimiento DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoInventario movimiento = mapResultSetToMovimiento(rs);
                    listaMovimientos.add(movimiento);
                }
            }
        }

        return listaMovimientos;
    }
    
    public List<MovimientoInventario> obtenerMovimientosPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws SQLException {
        List<MovimientoInventario> listaMovimientos = new ArrayList<>();
        String sql = "SELECT * FROM system.movimientosinventario WHERE fecha_movimiento BETWEEN ? AND ? ORDER BY fecha_movimiento DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fechaDesde));
            ps.setTimestamp(2, Timestamp.valueOf(fechaHasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoInventario movimiento = mapResultSetToMovimiento(rs);
                    listaMovimientos.add(movimiento);
                }
            }
        }

        return listaMovimientos;
    }

    @Override
    public void eliminarMovimiento(int id) throws SQLException {
        String sql = "DELETE FROM system.movimientosinventario WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No se encontró movimiento con id " + id + " para eliminar.");
            }
        }
    }

    private MovimientoInventario mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setId(rs.getInt("id"));
        movimiento.setIdProducto(rs.getInt("id_producto"));
        movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
        movimiento.setCantidad(rs.getInt("cantidad"));
        Timestamp ts = rs.getTimestamp("fecha_movimiento");
        if (ts != null) {
            movimiento.setFechaMovimiento(ts.toLocalDateTime());
        }
        movimiento.setMotivo(rs.getString("motivo"));
        return movimiento;
    }
}
