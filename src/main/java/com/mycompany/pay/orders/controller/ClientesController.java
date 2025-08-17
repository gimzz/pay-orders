/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.controller;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.pay.orders.dao.ClientesDAO;
import com.mycompany.pay.orders.model.Clientes;

/**
 *
 * @author gimz
 */
public class ClientesController {

    private ClientesDAO clientesDAO;

    private ClientesController clientesController;
    public ClientesController(ClientesDAO clientesDAO) {
        this.clientesDAO = clientesDAO;
    }
    

    public void setClientesController(ClientesController clientesController) {
        this.clientesController = clientesController;
    }

    public void agregarCliente(Clientes clientes) throws SQLException, IllegalArgumentException {
        validarClientes(clientes, true);
        clientesDAO.agregarCliente(clientes);
    }

    public Clientes obtenerClientePorId(int id) throws SQLException, IllegalArgumentException {
        return clientesDAO.obtenerClientePorId(id);
    }

    public List<Clientes> obtenerTodosLosClientes() throws SQLException, IllegalArgumentException {
        return clientesDAO.obtenerTodosLosClientes();
    }

    public void actualizarCliente(Clientes clientes) throws SQLException, IllegalArgumentException {
        validarClientes(clientes, true);
        clientesDAO.actualizarCliente(clientes);
    }

    public void eliminarCliente(int id) throws SQLException, IllegalArgumentException {
        clientesDAO.eliminarCliente(id);
    }

    private void validarClientes(Clientes clientes, boolean esNuevo) {
        if (clientes == null) {
            throw new IllegalArgumentException("El cliente no puede ser null");
        }

        if (clientes.getNombre() == null || clientes.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }

        if (clientes.getApellido() == null || clientes.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio.");
        }

        if (!clientes.getNombre().matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El nombre no puede contener caracteres especiales ni numeros.");
        }

        if (!clientes.getApellido().matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El apellido no puede contener caracteres especiales ni números.");
        }

        if (clientes.getCedula() <= 0) {
            throw new IllegalArgumentException("La cedula es obligatoria y debe ser mayor a cero (0).");
        }

    }

}
