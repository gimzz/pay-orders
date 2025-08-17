package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.Productos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class SeleccionProductoFormController {

    @FXML private ComboBox<Productos> comboProducto;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private Button btnAgregar, btnCancelar;
    @FXML private Label lblMensajeError;

    private Productos productoSeleccionado;
    private int cantidadSeleccionada;

    private boolean agregado = false;

    public void setProductos(List<Productos> productos) {
        comboProducto.setItems(FXCollections.observableArrayList(productos));
        if (!productos.isEmpty()) {
            comboProducto.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void initialize() {
        btnAgregar.setOnAction(e -> onAgregar());
        btnCancelar.setOnAction(e -> onCancelar());
    }

    private void onAgregar() {
        Productos p = comboProducto.getSelectionModel().getSelectedItem();
        Integer cantidad = spinnerCantidad.getValue();
        if (p == null) {
            lblMensajeError.setText("Seleccione un producto.");
            return;
        }
        if (cantidad == null || cantidad <= 0) {
            lblMensajeError.setText("Cantidad debe ser mayor que cero.");
            return;
        }
        productoSeleccionado = p;
        cantidadSeleccionada = cantidad;
        agregado = true;
        cerrarVentana();
    }

    private void onCancelar() {
        agregado = false;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) comboProducto.getScene().getWindow();
        stage.close();
    }

    public boolean isAgregado() {
        return agregado;
    }

    public Productos getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public int getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }
}
