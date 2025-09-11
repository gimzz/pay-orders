package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.TasadeCambio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TasaCambioFormController {

    @FXML private TableView<TasadeCambio> tablaTasas;
    @FXML private TableColumn<TasadeCambio, String> colFecha;
    @FXML private TableColumn<TasadeCambio, String> colMonedaOrigen;
    @FXML private TableColumn<TasadeCambio, String> colMonedaDestino;
    @FXML private TableColumn<TasadeCambio, BigDecimal> colValor;

    @FXML private DatePicker datePickerFecha;
    @FXML private TextField txtMonedaOrigen;
    @FXML private TextField txtMonedaDestino;
    @FXML private TextField txtValor;

    private TasadeCambioController tasaCambioController;
    private ObservableList<TasadeCambio> listaTasas = FXCollections.observableArrayList();

    public void setTasaCambioController(TasadeCambioController controller) {
        this.tasaCambioController = controller;
    }

    public void cargarTasasExistentes() {
        try {
            List<TasadeCambio> tasas = tasaCambioController.obtenerTodasLasTasasCambio();
            listaTasas.setAll(tasas);
            tablaTasas.setItems(listaTasas);
        } catch (Exception e) {
            mostrarAlertaError("Error al cargar tasas", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFechaTasaCambio().toLocalDate().toString()));
        colMonedaOrigen.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMonedaOrigen()));
        colMonedaDestino.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMonedaDestino()));
        colValor.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValor()));

        datePickerFecha.setValue(LocalDate.now());
        txtMonedaOrigen.clear();
        txtMonedaDestino.clear();
        txtValor.clear();
    }

    @FXML
    private void handleAgregarTasa() {
        if (datePickerFecha.getValue() == null) {
            mostrarAlertaError("Validación", "Debe seleccionar una fecha");
            return;
        }
        String monedaOrigen = txtMonedaOrigen.getText().trim();
        String monedaDestino = txtMonedaDestino.getText().trim();
        String valorStr = txtValor.getText().trim();

        if (monedaOrigen.isEmpty() || monedaDestino.isEmpty() || valorStr.isEmpty()) {
            mostrarAlertaError("Validación", "Complete todos los campos");
            return;
        }

        BigDecimal valor;
        try {
            valor = new BigDecimal(valorStr);
        } catch (NumberFormatException e) {
            mostrarAlertaError("Validación", "Valor debe ser numérico");
            return;
        }

        try {
            TasadeCambio nuevaTasa = new TasadeCambio();
            nuevaTasa.setFechaTasaCambio(datePickerFecha.getValue().atStartOfDay());
            nuevaTasa.setMonedaOrigen(monedaOrigen);
            nuevaTasa.setMonedaDestino(monedaDestino);
            nuevaTasa.setValor(valor);

            tasaCambioController.registrarTasaCambio(nuevaTasa);
            listaTasas.add(nuevaTasa);
            limpiarFormulario();
        } catch (Exception e) {
            mostrarAlertaError("Error al agregar tasa", e.getMessage());
        }
    }

    @FXML
    private void handleEliminarTasa() {
        TasadeCambio seleccionada = tablaTasas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlertaError("Validación", "Seleccione una tasa para eliminar");
            return;
        }
        try {
            tasaCambioController.eliminarTasaCambio(seleccionada.getId());
            listaTasas.remove(seleccionada);
        } catch (Exception e) {
            mostrarAlertaError("Error al eliminar tasa", e.getMessage());
        }
    }

    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) tablaTasas.getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        datePickerFecha.setValue(null);
        txtMonedaOrigen.clear();
        txtMonedaDestino.clear();
        txtValor.clear();
    }

    private void mostrarAlertaError(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
