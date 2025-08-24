package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MateriaPrimaDAO;
import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAO;
import com.mycompany.pay.orders.model.MateriaPrima;
import com.mycompany.pay.orders.model.MovimientoMateriaPrima;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MateriaPrimaFormController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtUnidadMedida;
    @FXML
    private TextField txtStockActual;
    @FXML
    private TextField txtStockMinimo;

    @FXML
    private Label lblMensaje;

    private MateriaPrimaDAO materiaPrimaDAO;
private MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO;

    private boolean guardado = false;
    private MateriaPrima materiaPrimaEdicion;

    public void setMateriaPrimaDAO(MateriaPrimaDAO materiaPrimaDAO) {
        this.materiaPrimaDAO = materiaPrimaDAO;
    }
public void setMovimientoMateriaPrimaDAO(MovimientoMateriaPrimaDAO dao) {
    this.movimientoMateriaPrimaDAO = dao;
}
    public void cargarMateriaPrima(MateriaPrima materiaPrima) {
        if (materiaPrima == null) {
            return;
        }

        this.materiaPrimaEdicion = materiaPrima;
        txtNombre.setText(materiaPrima.getNombre());
        txtDescripcion.setText(materiaPrima.getDescripcion());
        txtUnidadMedida.setText(materiaPrima.getUnidadMedida());
        txtStockActual.setText(String.valueOf(materiaPrima.getStockActual()));
        txtStockMinimo.setText(String.valueOf(materiaPrima.getStockMinimo()));
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
    int stockActualNuevo = 0;
    int stockMinimo = 0;

    if (nombre.isEmpty()) {
        lblMensaje.setText("El nombre es obligatorio.");
        return;
    }

    try {
        try {
            stockActualNuevo = Integer.parseInt(txtStockActual.getText().trim());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Stock Actual debe ser un número entero válido.");
            return;
        }
        try {
            stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Stock Mínimo debe ser un número entero válido.");
            return;
        }
        if (stockMinimo > stockActualNuevo) {
            lblMensaje.setText("El stock mínimo no puede ser mayor que el stock actual.");
            return;
        }

        if (materiaPrimaEdicion == null) {
            // Nueva materia prima
            MateriaPrima nueva = new MateriaPrima();
            nueva.setNombre(nombre);
            nueva.setDescripcion(descripcion);
            nueva.setUnidadMedida(unidadMedida);
            nueva.setStockActual(stockActualNuevo);
            nueva.setStockMinimo(stockMinimo);

            materiaPrimaDAO.agregarMateriaPrima(nueva);
            materiaPrimaEdicion = nueva;

            if (stockActualNuevo > 0 && movimientoMateriaPrimaDAO != null) {
                MovimientoMateriaPrima movimiento = new MovimientoMateriaPrima();
                movimiento.setIdMateriaPrima(materiaPrimaEdicion.getId());
                movimiento.setTipoMovimiento("ENTRADA");
                movimiento.setCantidad(stockActualNuevo);
                movimiento.setMotivo("Stock inicial");
                movimiento.setFechaMovimiento(LocalDateTime.now());
                movimientoMateriaPrimaDAO.agregarMovimiento(movimiento);
            }
        } else {
            int stockAnterior = materiaPrimaEdicion.getStockActual();

            materiaPrimaEdicion.setNombre(nombre);
            materiaPrimaEdicion.setDescripcion(descripcion);
            materiaPrimaEdicion.setUnidadMedida(unidadMedida);
            materiaPrimaEdicion.setStockActual(stockActualNuevo);
            materiaPrimaEdicion.setStockMinimo(stockMinimo);

            materiaPrimaDAO.actualizarMateriaPrima(materiaPrimaEdicion);

            int diferencia = stockActualNuevo - stockAnterior;

            if (diferencia != 0 && movimientoMateriaPrimaDAO != null) {
                MovimientoMateriaPrima movimiento = new MovimientoMateriaPrima();
                movimiento.setIdMateriaPrima(materiaPrimaEdicion.getId());
                movimiento.setCantidad(Math.abs(diferencia));
                movimiento.setMotivo("Ajuste de stock");

                if (diferencia > 0) {
                    movimiento.setTipoMovimiento("ENTRADA");
                } else {
                    movimiento.setTipoMovimiento("SALIDA");
                }

                movimiento.setFechaMovimiento(LocalDateTime.now());
                movimientoMateriaPrimaDAO.agregarMovimiento(movimiento);
            }
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
