package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.MateriaPrima;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaDAOImpl implements MateriaPrimaDAO {

    private final Connection connection;

    public MateriaPrimaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarMateriaPrima(MateriaPrima materiaPrima) throws SQLException {
        String sql = "INSERT INTO system.materia_prima (nombre, descripcion, unidad_medida, stock_actual, stock_minimo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, materiaPrima.getNombre());
            ps.setString(2, materiaPrima.getDescripcion());
            ps.setString(3, materiaPrima.getUnidadMedida());
            ps.setInt(4, materiaPrima.getStockActual());
            ps.setInt(5, materiaPrima.getStockMinimo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Agregar materia prima falló, no se insertó ningún registro.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    materiaPrima.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se obtuvo el ID generado para materia prima.");
                }
            }
        }
    }

    @Override
    public MateriaPrima obtenerMateriaPrimaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.materia_prima WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMateriaPrima(rs);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<MateriaPrima> obtenerTodasMateriasPrimas() throws SQLException {
        List<MateriaPrima> lista = new ArrayList<>();
        String sql = "SELECT * FROM system.materia_prima ORDER BY nombre";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMateriaPrima(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizarMateriaPrima(MateriaPrima materiaPrima) throws SQLException {
        String sql = "UPDATE system.materia_prima SET nombre=?, descripcion=?, unidad_medida=?, stock_actual=?, stock_minimo=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, materiaPrima.getNombre());
            ps.setString(2, materiaPrima.getDescripcion());
            ps.setString(3, materiaPrima.getUnidadMedida());
            ps.setInt(4, materiaPrima.getStockActual());
            ps.setInt(5, materiaPrima.getStockMinimo());
            ps.setInt(6, materiaPrima.getId());
            ps.executeUpdate();
        }
    }
    
public int obtenerStockActual(int idMateriaPrima) throws SQLException {
    String sql = "SELECT " +
                 "COALESCE(SUM(CASE WHEN tipo_movimiento = 'ENTRADA' THEN cantidad ELSE 0 END), 0) - " +
                 "COALESCE(SUM(CASE WHEN tipo_movimiento = 'SALIDA' THEN cantidad ELSE 0 END), 0) AS stock_actual " +
                 "FROM system.movimientos_materia_prima WHERE id_materia_prima = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idMateriaPrima);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("stock_actual");
            } else {
                return 0;
            }
        }
    }
}


   public void eliminarMateriaPrima(int id) throws SQLException {
    String sqlMovimientos = "DELETE FROM system.movimientos_materia_prima WHERE id_materia_prima = ?";
    try (PreparedStatement ps = connection.prepareStatement(sqlMovimientos)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    }
    String sqlMateriaPrima = "DELETE FROM system.materia_prima WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sqlMateriaPrima)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}

   private MateriaPrima mapearMateriaPrima(ResultSet rs) throws SQLException {
    MateriaPrima m = new MateriaPrima();
    m.setId(rs.getInt("id"));
    m.setNombre(rs.getString("nombre"));
    m.setDescripcion(rs.getString("descripcion"));
    m.setUnidadMedida(rs.getString("unidad_medida"));
    m.setStockActual(rs.getInt("stock_actual"));
    m.setStockMinimo(rs.getInt("stock_minimo")); 
        return m;
}
}
