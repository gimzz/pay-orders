package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.Clientes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClientesFormController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;

    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ListView<Clientes> listaClientes;

    private ClientesController clientesController;

    private Clientes clienteEdicion = null;
    private ObservableList<Clientes> clientesObservable;

    public void setClientesController(ClientesController clientesController) {
        this.clientesController = clientesController;
        cargarClientesEnLista();
    }

    private void cargarClientesEnLista() {
        try {
            clientesObservable = FXCollections.observableArrayList(clientesController.obtenerTodosLosClientes());
            listaClientes.setItems(clientesObservable);
        } catch (Exception e) {
            lblMensaje.setText("Error cargando clientes: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        limpiarFormulario();
        listaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarClienteParaEdicion(newSel);
            }
        });

        btnGuardar.setOnAction(e -> guardarCliente());
        btnCancelar.setOnAction(e -> limpiarFormulario());
        btnNuevo.setOnAction(e -> limpiarFormulario());
        btnEliminar.setOnAction(e -> eliminarCliente());
    }

    private void cargarClienteParaEdicion(Clientes cliente) {
        clienteEdicion = cliente;
        txtCedula.setText(cliente.getCedula() > 0 ? String.valueOf(cliente.getCedula()) : "");
        txtNombre.setText(cliente.getNombre() != null ? cliente.getNombre() : "");
        txtApellido.setText(cliente.getApellido() != null ? cliente.getApellido() : "");
        txtTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
        lblMensaje.setText("");
    }

    private void limpiarFormulario() {
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        lblMensaje.setText("");
        clienteEdicion = null;
        listaClientes.getSelectionModel().clearSelection();
    }

    private void guardarCliente() {
        String cedulaText = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            lblMensaje.setText("El nombre es obligatorio.");
            return;
        }
        if (apellido.isEmpty()) {
            lblMensaje.setText("El apellido es obligatorio.");
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
                clientesObservable.add(nuevo);
                listaClientes.getSelectionModel().select(nuevo);

                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Cliente agregado correctamente.");
            } else {
                clienteEdicion.setCedula(cedula);
                clienteEdicion.setNombre(nombre);
                clienteEdicion.setApellido(apellido);
                clienteEdicion.setTelefono(telefono);

                clientesController.actualizarCliente(clienteEdicion);
                listaClientes.refresh();

                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Cliente actualizado correctamente.");
            }
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error al guardar cliente: " + e.getMessage());
        }
    }

 private void eliminarCliente() {
    Clientes seleccionado = listaClientes.getSelectionModel().getSelectedItem();
    if (seleccionado == null) {
        lblMensaje.setText("Seleccione un cliente para eliminar.");
        return;
    }
    try {
        clientesController.eliminarCliente(seleccionado.getId());  // Pasar el id correcto
        clientesObservable.remove(seleccionado);
        limpiarFormulario();

        lblMensaje.setStyle("-fx-text-fill: green;");
        lblMensaje.setText("Cliente eliminado.");
    } catch (Exception e) {
        lblMensaje.setStyle("-fx-text-fill: red;");
        lblMensaje.setText("Error al eliminar cliente: " + e.getMessage());
    }
}

}
