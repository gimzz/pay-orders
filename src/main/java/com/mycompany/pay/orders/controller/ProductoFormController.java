package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.Productos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ProductoFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioUsd;
    @FXML private Label lblMensaje;

    private ProductosDAO productosDAO;
    private Productos producto;

    private Runnable onCloseCallback;

    public void setProductosDAO(ProductosDAO dao) {
        this.productosDAO = dao;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecioUsd.setText(producto.getPrecioUsd().toString());
        } else {
            txtNombre.clear();
            txtPrecioUsd.clear();
        }
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecioUsd.getText().trim();

        if (nombre.isEmpty()) {
            lblMensaje.setText("El nombre es obligatorio");
            return;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr);
        } catch (NumberFormatException e) {
            lblMensaje.setText("Precio inválido");
            return;
        }

        if (producto == null) {
            producto = new Productos();
        }
        producto.setNombre(nombre);
        producto.setPrecioUsd(precio);

        try {
            if (producto.getId() == 0) {
                productosDAO.agregarProducto(producto);
            } else {
                productosDAO.actualizarProducto(producto);
            }
            if (onCloseCallback != null) onCloseCallback.run(); 
        } catch (SQLException e) {
            lblMensaje.setText("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        if (onCloseCallback != null) onCloseCallback.run(); 
    }
}
