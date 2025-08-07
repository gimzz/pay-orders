/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Clientes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gimz
 */
public class ClientesDAOImpl implements ClientesDAO {

    private Connection connection;

    public ClientesDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarCliente(Clientes cliente) throws SQLException {
        String sql = "INSERT INTO system.clientes (cedula, nombre, apellido, telefono, fecha_registro) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, cliente.getCedula());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            LocalDateTime fechaRegistro = cliente.getFechaRegistro();
            if (fechaRegistro != null) {
                ps.setTimestamp(5, Timestamp.valueOf(fechaRegistro));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }

            ps.executeUpdate();
        }

    }

    @Override
    public Clientes obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.clientes  where id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Clientes(
                            rs.getInt("id"),
                            rs.getInt("cedula"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("telefono"),
                            rs.getTimestamp("fecha_registro") != null ? rs.getTimestamp("fecha_registro").toLocalDateTime() : null
                    );

                } else {
                    return null;
                }

            }

        }

    }

    @Override

    public List<Clientes> obtenerTodosLosClientes() throws SQLException {
        List<Clientes> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM system.clientes";
        try (PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clientes clientes = new Clientes();
                clientes.setId(rs.getInt("id"));
                clientes.setCedula(rs.getInt("cedula"));
                clientes.setNombre(rs.getString("nombre"));
                clientes.setApellido(rs.getString("apellido"));
                clientes.setTelefono(rs.getString("telefono"));
                Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
                if (fechaRegistro != null) {
                    clientes.setFechaRegistro(fechaRegistro.toLocalDateTime());
                } else {
                    clientes.setFechaRegistro(null);
                }

                listaClientes.add(clientes);
            }
        }

        return listaClientes;

    }

    @Override
    public void actualizarCliente(Clientes cliente) throws SQLException {
        String sql = "UPDATE system.clientes SET cedula=?, nombre=?, apellido=?, telefono=? WHERE id=?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, cliente.getCedula());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getId());
            ps.executeUpdate();
        }

    }

    @Override
    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM system.clientes where id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

    }

}
