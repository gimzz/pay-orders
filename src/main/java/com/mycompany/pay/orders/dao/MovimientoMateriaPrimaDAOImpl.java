package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MovimientoMateriaPrima;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoMateriaPrimaDAOImpl implements MovimientoMateriaPrimaDAO {

    private final Connection connection;

    public MovimientoMateriaPrimaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarMovimiento(MovimientoMateriaPrima movimiento) throws SQLException {
        String sql = "INSERT INTO system.movimientos_materia_prima (id_materia_prima, tipo_movimiento, cantidad, motivo, fecha_movimiento) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, movimiento.getIdMateriaPrima());
            ps.setString(2, movimiento.getTipoMovimiento());
            ps.setInt(3, movimiento.getCantidad());
            ps.setString(4, movimiento.getMotivo());
            ps.setTimestamp(5, Timestamp.valueOf(movimiento.getFechaMovimiento()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Agregar movimiento de materia prima falló, no se insertó ningún registro.");
            }

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    movimiento.setId(claves.getInt(1));
                } else {
                    throw new SQLException("No se obtuvo el ID generado para movimiento.");
                }
            }
        }
    }

    @Override
    public MovimientoMateriaPrima obtenerMovimientoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.movimientos_materia_prima WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimiento(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<MovimientoMateriaPrima> obtenerMovimientosPorMateriaPrima(int idMateriaPrima) throws SQLException {
        String sql = "SELECT * FROM system.movimientos_materia_prima WHERE id_materia_prima = ? ORDER BY fecha_movimiento DESC";
        List<MovimientoMateriaPrima> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMateriaPrima);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<MovimientoMateriaPrima> obtenerTodosMovimientos() throws SQLException {
        String sql = "SELECT * FROM system.movimientos_materia_prima ORDER BY fecha_movimiento DESC";
        List<MovimientoMateriaPrima> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        }
        return lista;
    }

    private MovimientoMateriaPrima mapearMovimiento(ResultSet rs) throws SQLException {
        MovimientoMateriaPrima m = new MovimientoMateriaPrima();
        m.setId(rs.getInt("id"));
        m.setIdMateriaPrima(rs.getInt("id_materia_prima"));
        m.setTipoMovimiento(rs.getString("tipo_movimiento"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setMotivo(rs.getString("motivo"));
        Timestamp ts = rs.getTimestamp("fecha_movimiento");
        m.setFechaMovimiento(ts != null ? ts.toLocalDateTime() : null);
        return m;
    }
}
