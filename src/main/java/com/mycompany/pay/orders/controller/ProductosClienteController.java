
package com.mycompany.pay.orders.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import com.mycompany.pay.orders.model.DetallePedido;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ProductosClienteController {

    @FXML private TableView<DetallePedido> tablaProductos;
    @FXML private TableColumn<DetallePedido, String> colProductoNombre;
    @FXML private TableColumn<DetallePedido, String> colPrecioUnitario;
    @FXML private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML private Label lblTitulo;

    private ObservableList<DetallePedido> listaDetalles;

    private PedidosController pedidosController;
    private ProductosController productosController;

    private int clienteId;

    public void setPedidosController(PedidosController pedidosController) {
        this.pedidosController = pedidosController;
    }

    public void setProductosController(ProductosController productosController) {
        this.productosController = productosController;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
        cargarProductosCliente();
    }

    @FXML
    private void initialize() {
        colProductoNombre.setCellValueFactory(cellData -> {
            int idProducto = cellData.getValue().getIdProducto();
            String nombreProducto = "Desconocido";
            try {
                if (productosController != null) {
                    nombreProducto = productosController.obtenerProductoPorId(idProducto).getNombre();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(nombreProducto);
        });

        colPrecioUnitario.setCellValueFactory(cellData -> {
            BigDecimal precioUnitario = cellData.getValue().getPrecioUnitario();
            String precioStr = precioUnitario != null ? precioUnitario.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
            return new SimpleStringProperty(precioStr);
        });

        colCantidad.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject()
        );

        listaDetalles = FXCollections.observableArrayList();
        tablaProductos.setItems(listaDetalles);
    }

    private void cargarProductosCliente() {
        if (pedidosController == null) {
            lblTitulo.setText("Error: controlador de pedidos no inyectado");
            return;
        }
        try {
            List<DetallePedido> detalles = pedidosController.obtenerDetallesPorCliente(clienteId);
            listaDetalles.setAll(detalles);
            lblTitulo.setText("Productos del Cliente ID: " + clienteId);
        } catch (SQLException e) {
            lblTitulo.setText("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
        stage.close();
    }
}