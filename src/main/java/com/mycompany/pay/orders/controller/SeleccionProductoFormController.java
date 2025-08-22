package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.Productos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.util.List;

public class SeleccionProductoFormController {

    @FXML private TableView<Productos> tablaProductos;
    @FXML private TableColumn<Productos, String> colNombreProducto;
    @FXML private TableColumn<Productos, BigDecimal> colPrecioProducto;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private Button btnAgregar, btnCancelar;
    @FXML private Label lblMensajeError;

    private Productos productoSeleccionado;
    private int cantidadSeleccionada;
    private boolean agregado = false;

    public void setProductos(List<Productos> productos) {
        tablaProductos.setItems(FXCollections.observableArrayList(productos));
    }

    @FXML
    private void initialize() {
        colNombreProducto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colPrecioProducto.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrecioUsd()));

        spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            productoSeleccionado = newSelection;
        });
    }

    @FXML
    private void onAgregarProducto() {
        if (productoSeleccionado == null) {
            lblMensajeError.setText("Seleccione un producto de la tabla.");
            return;
        }
        cantidadSeleccionada = spinnerCantidad.getValue();
        if (cantidadSeleccionada <= 0) {
            lblMensajeError.setText("Cantidad debe ser mayor a cero.");
            return;
        }
        agregado = true;
        cerrarVentana();
    }

    @FXML
    private void onCancelar() {
        agregado = false;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
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
