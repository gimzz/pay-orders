package com.mycompany.pay.orders.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.mycompany.pay.orders.dao.PagosPedidoDAO;
import com.mycompany.pay.orders.dao.PagosPedidoImpl;
import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.dao.ProductosDAOImpl;
import com.mycompany.pay.orders.dao.TasadeCambioDAO;
import com.mycompany.pay.orders.dao.TasadeCambioDAOImpl;
import com.mycompany.pay.orders.model.Clientes;
import com.mycompany.pay.orders.model.Pedidos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PedidosViewController {
    @FXML
    private TableView<Pedidos> tablaPedidos;
    @FXML
    private TableColumn<Pedidos, String> colClienteNombre;
    @FXML
    private TableColumn<Pedidos, String> colFecha;
    @FXML
    private TableColumn<Pedidos, BigDecimal> colTotalUsd;
    @FXML
    private TableColumn<Pedidos, String> colTotalBsd;
    @FXML
    private TableColumn<Pedidos, BigDecimal> colTasaCambio;
    @FXML
    private TableColumn<Pedidos, String> colEntregado;
    @FXML
    private TableColumn<Pedidos, String> colEstadoPago;
    @FXML
    private TableColumn<Pedidos, String> colMetodoPago;
    @FXML
    private Label lblMensaje;

    private ObservableList<Pedidos> listaPedidos;
    private PedidosController pedidosController;
    private ClientesController clientesController;
    private Connection connection;

    public void setControllers(Connection connection, ClientesController clientesController) {
        this.connection = connection;
        this.clientesController = clientesController;
        ProductosDAO productosDAO = new ProductosDAOImpl(connection);
        PagosPedidoDAO pagosPedidoDAO = new PagosPedidoImpl(connection);
        TasadeCambioDAO tasaCambioDAO = new TasadeCambioDAOImpl(connection);
        TasadeCambioController tasaCambioController = new TasadeCambioController(tasaCambioDAO);
        this.pedidosController = new PedidosController(connection, productosDAO, pagosPedidoDAO, tasaCambioController);
        cargarPedidos();
    }

    @FXML
    private void initialize() {
        colClienteNombre.setCellValueFactory(cellData -> {
            int clienteId = cellData.getValue().getClienteId();
            String nombreCompleto = "Desconocido";
            if (clientesController != null) {
                try {
                    Clientes cliente = clientesController.obtenerClientePorId(clienteId);
                    if (cliente != null) {
                        nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return new SimpleStringProperty(nombreCompleto);
        });

        colFecha.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaPedido() != null) {
                String fechaFormateada = cellData.getValue().getFechaPedido()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return new SimpleStringProperty(fechaFormateada);
            } else {
                return new SimpleStringProperty("");
            }
        });

        colTotalUsd.setCellValueFactory(new PropertyValueFactory<>("totalUsd"));

        colTotalBsd.setCellValueFactory(cellData -> {
            Pedidos pedido = cellData.getValue();
            if (pedido.getTotalUsd() != null && pedido.getTasaCambioAplicada() != null) {
                BigDecimal totalBsd = pedido.getTotalUsd().multiply(pedido.getTasaCambioAplicada());
                return new SimpleStringProperty(totalBsd.setScale(2, RoundingMode.HALF_UP).toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        colTasaCambio.setCellValueFactory(new PropertyValueFactory<>("tasaCambioAplicada"));

        colEntregado.setCellValueFactory(cellData -> {
            boolean entregado = cellData.getValue().isEntregado();
            return new SimpleStringProperty(entregado ? "Entregado" : "No entregado");
        });

        colEstadoPago.setCellValueFactory(cellData -> {
            try {
                String estadoPago = pedidosController.getPedidosDAO().obtenerEstadoPago(cellData.getValue().getId());
                return new SimpleStringProperty(estadoPago);
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("ERROR");
            }
        });

        colMetodoPago.setCellValueFactory(cellData -> {
            try {
                String metodoPago = pedidosController.getPedidosDAO().obtenerMetodoPago(cellData.getValue().getId());
                return new SimpleStringProperty(metodoPago != null ? metodoPago : "Desconocido");
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("ERROR");
            }
        });

        listaPedidos = FXCollections.observableArrayList();
        tablaPedidos.setItems(listaPedidos);
    }

    private void cargarPedidos() {
        try {
            List<Pedidos> pedidos = pedidosController.obtenerTodosLosPedidos();
            listaPedidos.setAll(pedidos);
            lblMensaje.setText("");
        } catch (SQLException e) {
            lblMensaje.setText("Error al cargar pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormularioNuevoPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/PedidoFormView.fxml"));
            Parent root = loader.load();

            PedidoFormController controller = loader.getController();

            controller.setControllers(
                pedidosController,
                new ProductosController(pedidosController.getProductosDAO()),
                pedidosController.getTasaCambioController(),
                clientesController,
                null, // Métodos de pago no definido, pasar null si no tienes controlador
                new PagosPedidoController(pedidosController.getPagosPedidoDAO())
            );

            controller.cargarDatosDespuesDeSetController(null);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Pedido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarPedidos();

        } catch (Exception e) {
            lblMensaje.setText("Error al abrir formulario nuevo pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormularioEditarPedido() {
        Pedidos seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un pedido para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/PedidoFormView.fxml"));
            Parent root = loader.load();

            PedidoFormController controller = loader.getController();

            controller.setControllers(
                pedidosController,
                new ProductosController(pedidosController.getProductosDAO()),
                pedidosController.getTasaCambioController(),
                clientesController,
                null, // Métodos de pago no definido, pasar null
                new PagosPedidoController(pedidosController.getPagosPedidoDAO())
            );

            controller.cargarDatosDespuesDeSetController(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Pedido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarPedidos();

        } catch (Exception e) {
            lblMensaje.setText("Error al abrir formulario edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarPedido() {
        Pedidos seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un pedido para eliminar.");
            return;
        }
        try {
            pedidosController.eliminarPedidoCompleto(seleccionado.getId());
            cargarPedidos();
            lblMensaje.setText("Pedido eliminado correctamente.");
        } catch (SQLException e) {
            lblMensaje.setText("Error al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarProductosCliente() {
        Pedidos seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un pedido para ver productos del cliente.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/ProductosClienteView.fxml"));
            Parent root = loader.load();

            ProductosClienteController controller = loader.getController();
            controller.setPedidosController(pedidosController);
            ProductosController productosController = new ProductosController(new ProductosDAOImpl(connection));
            controller.setProductosController(productosController);
            controller.setClienteId(seleccionado.getClienteId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Productos del Cliente: " + seleccionado.getClienteId());
            stage.setWidth(600);
            stage.setHeight(400);
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            lblMensaje.setText("Error al abrir ventana de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarEstadoEntrega() {
        Pedidos seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un pedido para actualizar estado de entrega.");
            return;
        }
        try {
            boolean nuevoEstado = !seleccionado.isEntregado();
            pedidosController.actualizarEstadoEntrega(seleccionado.getId(), nuevoEstado);
            cargarPedidos();
            lblMensaje.setText("Estado de entrega actualizado.");
        } catch (SQLException e) {
            lblMensaje.setText("Error al actualizar estado de entrega: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PedidosController getPedidosController() {
        return pedidosController;
    }
}
