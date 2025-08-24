package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.Productos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductoViewController {

    @FXML private TableView<Productos> tablaProductos;
    @FXML private TableColumn<Productos, Integer> colId;
    @FXML private TableColumn<Productos, String> colNombre;
    @FXML private TableColumn<Productos, BigDecimal> colPrecioUsd;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioUsd;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Label lblMensaje;

    private ProductosDAO productosDAO;
    private ObservableList<Productos> productosObservable;

    public void setProductosDAO(ProductosDAO dao) {
        this.productosDAO = dao;
        cargarProductos();
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioUsd.setCellValueFactory(new PropertyValueFactory<>("precioUsd"));

        btnAgregar.setOnAction(e -> agregarProducto());
        btnEliminar.setOnAction(e -> eliminarProducto());

        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    txtNombre.setText(newSelection.getNombre());
                    txtPrecioUsd.setText(newSelection.getPrecioUsd().toString());
                }
            }
        );
    }

    private void cargarProductos() {
        try {
            List<Productos> lista = productosDAO.obtenerTodosLosProductos();
            productosObservable = FXCollections.observableArrayList(lista);
            tablaProductos.setItems(productosObservable);
            lblMensaje.setText("");
        } catch (SQLException e) {
            lblMensaje.setText("Error al cargar productos: " + e.getMessage());
        }
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecioUsd.getText().trim();

        if (nombre.isEmpty()) {
            lblMensaje.setText("Debe ingresar nombre del producto.");
            return;
        }
        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr);
        } catch (NumberFormatException e) {
            lblMensaje.setText("Precio USD inválido.");
            return;
        }

        Productos producto = new Productos();
        producto.setNombre(nombre);
        producto.setPrecioUsd(precio);
        producto.setActivo(true);
        producto.setStockActual(0);
        producto.setStockMinimo(0);

        try {
            productosDAO.agregarProducto(producto);
            lblMensaje.setText("Producto agregado correctamente.");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException e) {
            lblMensaje.setText("Error al agregar producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        Productos seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un producto para eliminar.");
            return;
        }
        try {
            productosDAO.eliminarProducto(seleccionado.getId());
            lblMensaje.setText("Producto eliminado.");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException e) {
            lblMensaje.setText("Error al eliminar producto: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtPrecioUsd.clear();
    }
}
