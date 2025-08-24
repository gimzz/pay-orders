package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MateriaPrimaDAO;
import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAO;
import com.mycompany.pay.orders.model.MateriaPrima;
import com.mycompany.pay.orders.model.MovimientoMateriaPrima;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoMateriaPrimaFormController {

    @FXML private ComboBox<MateriaPrima> comboMateriaPrima;
    @FXML private Label lblStockActual;
    @FXML private TextField txtCantidad;
    @FXML private TextArea txtMotivo;
    @FXML private Label lblMensaje;

    private MateriaPrimaDAO materiaPrimaDAO;
    private MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO;

    private boolean guardado = false;

    public void setDAOs(MateriaPrimaDAO materiaPrimaDAO, MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO) {
        this.materiaPrimaDAO = materiaPrimaDAO;
        this.movimientoMateriaPrimaDAO = movimientoMateriaPrimaDAO;
        cargarMateriasPrimas();
    }

    private void cargarMateriasPrimas() {
        try {
            List<MateriaPrima> lista = materiaPrimaDAO.obtenerTodasMateriasPrimas();

            for (MateriaPrima m : lista) {
                int stock = materiaPrimaDAO.obtenerStockActual(m.getId());
                m.setStockActual(stock);
            }

            ObservableList<MateriaPrima> observableList = FXCollections.observableArrayList(lista);
            comboMateriaPrima.setItems(observableList);

            if (!observableList.isEmpty()) {
                comboMateriaPrima.getSelectionModel().selectFirst();
                lblStockActual.setText(String.valueOf(comboMateriaPrima.getSelectionModel().getSelectedItem().getStockActual()));
            } else {
                lblStockActual.setText("");
            }

            comboMateriaPrima.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    lblStockActual.setText(String.valueOf(newVal.getStockActual()));
                    lblMensaje.setText("");
                } else {
                    lblStockActual.setText("");
                }
            });

        } catch (Exception ex) {
            lblMensaje.setText("Error cargando materias primas: " + ex.getMessage());
            lblStockActual.setText("");
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleGuardar() {
        MateriaPrima materiaSeleccionada = comboMateriaPrima.getSelectionModel().getSelectedItem();
        if (materiaSeleccionada == null) {
            lblMensaje.setText("Seleccione una materia prima.");
            return;
        }
        String cantidadStr = txtCantidad.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (cantidadStr.isEmpty()) {
            lblMensaje.setText("Ingrese la cantidad de movimiento.");
            return;
        }
        if (motivo.isEmpty()) {
            lblMensaje.setText("Ingrese el motivo del movimiento.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                lblMensaje.setText("La cantidad debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("Cantidad debe ser un número entero válido.");
            return;
        }

        try {
            int stockActual = materiaPrimaDAO.obtenerStockActual(materiaSeleccionada.getId());
            if (cantidad > stockActual) {
                lblMensaje.setText("Cantidad a sacar excede el stock actual (" + stockActual + ").");
                return;
            }

            MovimientoMateriaPrima movimiento = new MovimientoMateriaPrima();
            movimiento.setIdMateriaPrima(materiaSeleccionada.getId());
            movimiento.setTipoMovimiento("SALIDA");
            movimiento.setCantidad(cantidad);
            movimiento.setMotivo(motivo);
            movimiento.setFechaMovimiento(LocalDateTime.now());

            movimientoMateriaPrimaDAO.agregarMovimiento(movimiento);
            guardado = true;
            cerrarVentana();

        } catch (Exception ex) {
            lblMensaje.setText("Error al guardar movimiento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) comboMateriaPrima.getScene().getWindow();
        stage.close();
    }

    public boolean isGuardado() {
        return guardado;
    }
}
