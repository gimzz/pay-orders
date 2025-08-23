package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MateriaPrimaDAO;
import com.mycompany.pay.orders.model.MateriaPrima;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MateriaPrimaFormController {

    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtUnidadMedida;

    @FXML private Label lblMensaje;

    private MateriaPrimaDAO materiaPrimaDAO;

    private boolean guardado = false;
    private MateriaPrima materiaPrimaEdicion;

    public void setMateriaPrimaDAO(MateriaPrimaDAO materiaPrimaDAO) {
        this.materiaPrimaDAO = materiaPrimaDAO;
    }

    public void cargarMateriaPrima(MateriaPrima materiaPrima) {
        if (materiaPrima == null) return;

        this.materiaPrimaEdicion = materiaPrima;
        txtNombre.setText(materiaPrima.getNombre());
        txtDescripcion.setText(materiaPrima.getDescripcion());
        txtUnidadMedida.setText(materiaPrima.getUnidadMedida());
    }

    public boolean isGuardado() {
        return guardado;
    }

    public MateriaPrima getMateriaPrimaGuardada() {
        if (guardado) {
            return materiaPrimaEdicion;
        }
        return null;
    }

@FXML
private void handleGuardar() {
    String nombre = txtNombre.getText().trim();
    String descripcion = txtDescripcion.getText().trim();
    String unidadMedida = txtUnidadMedida.getText().trim();

    if (nombre.isEmpty()) {
        lblMensaje.setText("El nombre es obligatorio.");
        return;
    }

    try {
        if (materiaPrimaEdicion == null) {
            MateriaPrima nueva = new MateriaPrima();
            nueva.setNombre(nombre);
            nueva.setDescripcion(descripcion);
            nueva.setUnidadMedida(unidadMedida);

            materiaPrimaDAO.agregarMateriaPrima(nueva);
            materiaPrimaEdicion = nueva;
        } else {
            materiaPrimaEdicion.setNombre(nombre);
            materiaPrimaEdicion.setDescripcion(descripcion);
            materiaPrimaEdicion.setUnidadMedida(unidadMedida);

            materiaPrimaDAO.actualizarMateriaPrima(materiaPrimaEdicion);
        }
        guardado = true;
        cerrarVentana();
    } catch (Exception e) {
        lblMensaje.setText("Error al guardar materia prima: " + e.getMessage());
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
