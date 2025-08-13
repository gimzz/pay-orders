package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.TasadeCambio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TasadeCambioDAOImpl implements TasadeCambioDAO {

    private Connection connection;

    public TasadeCambioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registrarTasaCambio(TasadeCambio tasa) throws SQLException {
        String sql = "INSERT INTO system.tasa_cambio (fecha, moneda_origen, moneda_destino, valor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(tasa.getFechaTasaCambio()));
            ps.setString(2, tasa.getMonedaOrigen());
            ps.setString(3, tasa.getMonedaDestino());
            ps.setBigDecimal(4, tasa.getValor());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                throw new SQLException("No se pudo insertar la tasa de cambio.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tasa.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public TasadeCambio obtenerTasaCambioPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM system.tasa_cambio WHERE CAST(fecha AS DATE) = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, fecha); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraerTasadeCambioDeResultSet(rs);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public TasadeCambio obtenerUltimaTasaCambio() throws SQLException {
        String sql = "SELECT * FROM system.tasa_cambio ORDER BY fecha DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return extraerTasadeCambioDeResultSet(rs);
            } else {
                return null;
            }
        }
    }

    @Override
    public List<TasadeCambio> obtenerTodasLasTasasCambio() throws SQLException {
        String sql = "SELECT * FROM system.tasa_cambio ORDER BY fecha DESC";
        List<TasadeCambio> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TasadeCambio tasa = extraerTasadeCambioDeResultSet(rs);
                lista.add(tasa);
            }
        }
        return lista;
    }

    private TasadeCambio extraerTasadeCambioDeResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Timestamp ts = rs.getTimestamp("fecha");
        LocalDateTime fechaTasaCambio = ts != null ? ts.toLocalDateTime() : null;
        String monedaOrigen = rs.getString("moneda_origen");
        String monedaDestino = rs.getString("moneda_destino");
        BigDecimal valor = rs.getBigDecimal("valor");

        return new TasadeCambio(id, fechaTasaCambio, monedaOrigen, monedaDestino, valor);
    }
}
