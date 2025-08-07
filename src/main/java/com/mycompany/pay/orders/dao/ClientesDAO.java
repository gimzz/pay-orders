/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;
import com.mycompany.pay.orders.model.Clientes;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author gimz
 */
public interface ClientesDAO {
    void agregarCliente(Clientes cliente) throws SQLException;
    Clientes obtenerClientePorId(int id) throws SQLException;
    List<Clientes> obtenerTodosLosClientes() throws SQLException;
    void actualizarCliente(Clientes clientes) throws SQLException;
    void eliminarCliente(int id) throws SQLException;
    
}
