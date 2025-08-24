package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.Productos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.SQLException;
import java.util.List;

public class ProductoViewController {

    @FXML private TableView<Productos> tablaProductos;
    @FXML private TableColumn<Productos, Integer> colId;
    @FXML private TableColumn<Productos, String> colNombre;
    @FXML private TableColumn<Productos, java.math.BigDecimal> colPrecioUsd;

    @FXML private Label lblMensaje;

    @FXML private VBox formularioContainer;

    private ProductosDAO productosDAO;
    private ObservableList<Productos> listaProductos;

    private ProductoFormController formController;

    public void setProductosDAO(ProductosDAO dao) {
        this.productosDAO = dao;
        cargarProductos();
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioUsd.setCellValueFactory(new PropertyValueFactory<>("precioUsd"));

        formularioContainer.getChildren().clear();
    }

    private void cargarProductos() {
        try {
            List<Productos> lista = productosDAO.obtenerTodosLosProductos();
            listaProductos = FXCollections.observableArrayList(lista);
            tablaProductos.setItems(listaProductos);
        } catch (SQLException e) {
            lblMensaje.setText("Error cargando productos: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarFormularioNuevo() {
        mostrarFormulario(null);
    }

    @FXML
    private void mostrarFormularioEditar() {
        Productos seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un producto para editar");
            return;
        }
        mostrarFormulario(seleccionado);
    }

    private void mostrarFormulario(Productos producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/ProductoFormView.fxml"));
            Parent formRoot = loader.load();

            formController = loader.getController();
            formController.setProductosDAO(productosDAO);
            formController.setProducto(producto);
            formController.setOnCloseCallback(() -> {
                formularioContainer.getChildren().clear();
                cargarProductos();
            });

            formularioContainer.getChildren().clear();
            formularioContainer.getChildren().add(formRoot);

        } catch (Exception e) {
            lblMensaje.setText("Error cargando formulario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarProducto() {
        Productos seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un producto para eliminar");
            return;
        }
        try {
            productosDAO.eliminarProducto(seleccionado.getId());
            lblMensaje.setText("Producto eliminado correctamente");
            cargarProductos();
            formularioContainer.getChildren().clear();
        } catch (SQLException e) {
            lblMensaje.setText("Error eliminando producto: " + e.getMessage());
        }
    }
}
