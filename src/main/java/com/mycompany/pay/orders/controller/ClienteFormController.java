package com.mycompany.pay.orders.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.mycompany.pay.orders.controller.ClientesController;
import com.mycompany.pay.orders.model.Clientes;

public class ClienteFormController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;

    private ClientesController clientesController;
    private Clientes clienteEdicion;
    private boolean guardado = false;
    private Clientes clienteGuardado;

    public void setClientesController(ClientesController clientesController) {
        this.clientesController = clientesController;
    }

    public void cargarCliente(Clientes cliente) {
        this.clienteEdicion = cliente;
        if (cliente != null) {
            txtCedula.setText(String.valueOf(cliente.getCedula()));
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtTelefono.setText(cliente.getTelefono());
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Clientes getClienteGuardado() {
        return clienteGuardado;
    }

    @FXML
    private void handleGuardar() {
        String cedulaText = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            lblMensaje.setText("El nombre o apodo es obligatorio.");
            return;
        }

        int cedula = 0;
        if (!cedulaText.isEmpty()) {
            try {
                cedula = Integer.parseInt(cedulaText);
                if (cedula <= 0) {
                    lblMensaje.setText("La cédula debe ser mayor que cero.");
                    return;
                }
            } catch (NumberFormatException e) {
                lblMensaje.setText("La cédula debe ser un número válido.");
                return;
            }
        }

        try {
            if (clienteEdicion == null) {
                Clientes nuevo = new Clientes();
                nuevo.setCedula(cedula);
                nuevo.setNombre(nombre);
                nuevo.setApellido(apellido);
                nuevo.setTelefono(telefono);

                clientesController.agregarCliente(nuevo);
                clienteGuardado = nuevo;
            } else {
                clienteEdicion.setCedula(cedula);
                clienteEdicion.setNombre(nombre);
                clienteEdicion.setApellido(apellido);
                clienteEdicion.setTelefono(telefono);

                clientesController.actualizarCliente(clienteEdicion);
                clienteGuardado = clienteEdicion;
            }
            guardado = true;
            cerrarVentana();
        } catch (Exception e) {
            lblMensaje.setText("Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
