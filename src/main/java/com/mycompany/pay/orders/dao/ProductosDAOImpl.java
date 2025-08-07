/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Productos;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gimz
 */
public class ProductosDAOImpl implements ProductosDAO {

    private Connection connection;

    public ProductosDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void agregarProducto(Productos productos) throws SQLException {
        String sql = "INSERT INTO system.productos (nombre, precio_usd, stock_actual, stock_minimo, activo) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, productos.getNombre());
            ps.setBigDecimal(2, productos.getPrecioUsd());
            ps.setInt(3, productos.getStockActual());
            ps.setInt(4, productos.getStockMinimo());
            ps.setBoolean(5, productos.isActivo());
            ps.executeUpdate();
        }
    }

    @Override
    public Productos obtenerProductoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.productos where id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Productos(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio_usd"),
                            rs.getInt("stock_actual"),
                            rs.getInt("stock_minimo"),
                            rs.getBoolean("activo")
                    );
                } else {
                    return null;
                }

            }
        }

    }

    @Override
    public List<Productos> obtenerTodosLosProductos() throws SQLException {
        List<Productos> listaProductos = new ArrayList<>();

        String sql = "SELECT * FROM system.productos";

        try (PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Productos productos = new Productos();
                productos.setId(rs.getInt("id"));
                productos.setNombre(rs.getString("nombre"));
                productos.setPrecioUsd(rs.getBigDecimal("precio_usd"));
                productos.setStockActual(rs.getInt("stock_actual"));
                productos.setStockMinimo(rs.getInt("stock_minimo"));
                productos.setActivo(rs.getBoolean("activo"));

                listaProductos.add(productos);
            }
        }

        return listaProductos;
    }

    @Override

    public void actualizarProducto(Productos productos) throws SQLException {
        String sql = "UPDATE system.productos SET nombre = ?, precio_usd = ?, stock_actual = ?, stock_minimo = ?, activo = ? WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, productos.getNombre());
            ps.setBigDecimal(2, productos.getPrecioUsd());
            ps.setInt(3, productos.getStockActual());
            ps.setInt(4, productos.getStockMinimo());
            ps.setBoolean(5, productos.isActivo());
            ps.setInt(6, productos.getId());
            ps.executeUpdate();

        }
    }
    @Override 
    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM system.productos where id = ? ";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

    }

    @Override
    public List<Productos> obtenerProductosActivos() throws SQLException {
        List<Productos> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM system.productos WHERE activo = true";
        try (PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Productos productos = new Productos();
                productos.setId(rs.getInt("id"));
                productos.setNombre(rs.getString("nombre"));
                productos.setPrecioUsd(rs.getBigDecimal("precio_usd"));
                productos.setStockActual(rs.getInt("stock_actual"));
                productos.setStockMinimo(rs.getInt("stock_minimo"));
                productos.setActivo(rs.getBoolean("activo"));

                listaProductos.add(productos);
            }
        }

        return listaProductos;

    }

    @Override

    public List<Productos> obtenerProductosPorDebajoDelStockMinimo() throws SQLException {
        List<Productos> listaProductos = new ArrayList<>();

        String sql = "SELECT * FROM system.productos WHERE stock_actual < stock_minimo";
        try (PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Productos productos = new Productos();
                productos.setId(rs.getInt("id"));
                productos.setNombre(rs.getString("nombre"));
                productos.setPrecioUsd(rs.getBigDecimal("precio_usd"));
                productos.setStockActual(rs.getInt("stock_actual"));
                productos.setStockMinimo(rs.getInt("stock_minimo"));
                productos.setActivo(rs.getBoolean("activo"));

                listaProductos.add(productos);
            }
        }

        return listaProductos;

    }

}
