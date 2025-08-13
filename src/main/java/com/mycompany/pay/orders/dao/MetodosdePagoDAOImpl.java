package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MetodosdePago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MetodosdePagoDAOImpl implements MetodosdePagoDAO {

    private Connection connection;

    public MetodosdePagoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarMetododePago(MetodosdePago metodo) throws SQLException {
        String sql = "INSERT INTO system.metodos_pago (descripcion, activo) VALUES (?, ?)";
        try (PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, metodo.getDescripcion());
            ps.setBoolean(2, metodo.isActivo());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                throw new SQLException("Error: No se pudo insertar el método de pago.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    metodo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public MetodosdePago obtenerMetododePagoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.metodos_pago WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null; // No se encontró el registro
                }
                MetodosdePago metodo = new MetodosdePago();
                metodo.setId(rs.getInt("id"));
                metodo.setDescripcion(rs.getString("descripcion"));
                metodo.setActivo(rs.getBoolean("activo"));
                return metodo;
            }
        }
    }

    @Override
    public List<MetodosdePago> obtenerTodoslosMetodosdePago() throws SQLException {
        List<MetodosdePago> lista = new ArrayList<>();
        String sql = "SELECT * FROM system.metodos_pago";

        try (PreparedStatement ps = this.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetodosdePago metodo = new MetodosdePago();
                metodo.setId(rs.getInt("id"));
                metodo.setDescripcion(rs.getString("descripcion"));
                metodo.setActivo(rs.getBoolean("activo"));
                lista.add(metodo);
            }
        }

        return lista;
    }

    @Override
    public void actualizarMetododePago(MetodosdePago metodo) throws SQLException {
        String sql = "UPDATE system.metodos_pago SET descripcion = ?, activo = ? WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, metodo.getDescripcion());
            ps.setBoolean(2, metodo.isActivo());
            ps.setInt(3, metodo.getId());

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas == 0) {
                throw new SQLException("Error: No se pudo actualizar el método de pago con ID " + metodo.getId());
            }
        }
    }

    @Override
    public void eliminarMetododePago(int id) throws SQLException {
        String sql = "DELETE FROM system.metodos_pago WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasEliminadas = ps.executeUpdate();
            if (filasEliminadas == 0) {
                throw new SQLException("Error: No se pudo eliminar el método de pago con ID " + id);
            }
        }
    }
}
