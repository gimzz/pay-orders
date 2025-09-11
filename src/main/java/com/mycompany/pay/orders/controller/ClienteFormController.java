package com.mycompany.pay.orders.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClienteFormController {

    @FXML private TextField txtNombre;
    @FXML private Label lblMensaje;

    private boolean guardado = false;
    private String nombreGuardado;

    public boolean isGuardado() {
        return guardado;
    }

    public String getNombreGuardado() {
        return nombreGuardado;
    }

    @FXML
    private void handleGuardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblMensaje.setText("El nombre es obligatorio.");
            return;
        }

        nombreGuardado = nombre;
        guardado = true;
        cerrarVentana();
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
