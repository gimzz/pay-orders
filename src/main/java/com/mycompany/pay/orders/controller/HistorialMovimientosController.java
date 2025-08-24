package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAO;
import com.mycompany.pay.orders.model.MovimientoMateriaPrima;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialMovimientosController {

    @FXML private TableView<MovimientoMateriaPrima> tablaMovimientos;
    @FXML private TableColumn<MovimientoMateriaPrima, String> colFecha;
    @FXML private TableColumn<MovimientoMateriaPrima, String> colTipo;
    @FXML private TableColumn<MovimientoMateriaPrima, Integer> colCantidad;
    @FXML private TableColumn<MovimientoMateriaPrima, String> colMotivo;

    private MovimientoMateriaPrimaDAO movimientoDAO;
    private int idMateriaPrima; // id de la materia para filtrar movimientos

    public void setMovimientoDAO(MovimientoMateriaPrimaDAO dao) {
        this.movimientoDAO = dao;
    }

    public void setIdMateriaPrima(int id) {
        this.idMateriaPrima = id;
        cargarMovimientos();
    }

    private void cargarMovimientos() {
        try {
            List<MovimientoMateriaPrima> lista = movimientoDAO.obtenerMovimientosPorMateriaPrima(idMateriaPrima);
            ObservableList<MovimientoMateriaPrima> observableList = FXCollections.observableArrayList(lista);

            colFecha.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getFechaMovimiento() != null
                        ? cellData.getValue().getFechaMovimiento().format(formatter)
                        : "");
            });
            colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoMovimiento()));
            colCantidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
            colMotivo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMotivo()));

            tablaMovimientos.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) tablaMovimientos.getScene().getWindow();
        stage.close();
    }
}
