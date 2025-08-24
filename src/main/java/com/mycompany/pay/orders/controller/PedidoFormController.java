package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.*;
import com.mycompany.pay.orders.dao.*;
import java.io.IOException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PedidoFormController {

    @FXML
    private ComboBox<Clientes> comboCliente;
    @FXML
    private Button btnNuevoCliente;
    @FXML
    private TableView<DetallePedidoRow> tablaProductos;
    @FXML
    private TableColumn<DetallePedidoRow, Productos> colProducto;
    @FXML
    private TableColumn<DetallePedidoRow, Integer> colCantidad;
    @FXML
    private TableColumn<DetallePedidoRow, BigDecimal> colPrecio;  // Precio Unitario
    @FXML
    private TableColumn<DetallePedidoRow, BigDecimal> colPrecioUsd;  // Precio USD
    @FXML
    private TableColumn<DetallePedidoRow, BigDecimal> colSubtotal;
    @FXML
    private TableColumn<DetallePedidoRow, Void> colQuitar;
    @FXML
    private Button btnAgregarProducto;
    @FXML
    private ComboBox<TasadeCambio> comboTasaCambio;
    @FXML
    private Label lblTotalUsd, lblTotalLocal;
    @FXML
    private CheckBox chkPagado, chkEntregado;
    @FXML
    private Label lblEstado, lblMensaje;
    @FXML
    private ComboBox<MetodosdePago> comboMetodoPago;
    @FXML
    private Button btnGuardar, btnCancelar;

    private PedidosController pedidosController;
    private ProductosController productosController;
    private TasadeCambioController tasaCambioController;
    private ClientesController clientesController;
    private MetodosdePagoController metodosPagoController;
    private PagosPedidoController pagosPedidoController;

    private ObservableList<DetallePedidoRow> detalles = FXCollections.observableArrayList();
    private Pedidos pedidoEnEdicion = null; // null si es nuevo pedido

    private PedidosViewController pedidosViewController;

    public void setPedidosViewController(PedidosViewController controller) {
        this.pedidosViewController = controller;
    }

    public void setControllers(
            PedidosController pedidosCtl,
            ProductosController productosCtl,
            TasadeCambioController tasaCtl,
            ClientesController clientesCtl,
            MetodosdePagoController metodosPagoCtl,
            PagosPedidoController pagosCtl
    ) {
        this.pedidosController = pedidosCtl;
        this.productosController = productosCtl;
        this.tasaCambioController = tasaCtl;
        this.clientesController = clientesCtl;
        this.metodosPagoController = metodosPagoCtl;
        this.pagosPedidoController = pagosCtl;
    }

    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(cellData -> cellData.getValue().productoProperty());
        colProducto.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList()));
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioUnitarioProperty());
        colPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        colPrecioUsd.setCellValueFactory(cellData -> cellData.getValue().precioProperty());
        colPrecioUsd.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        colSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty());
        btnNuevoCliente.setOnAction(e -> abrirFormularioNuevoCliente());

        colQuitar.setCellFactory(col -> {
            TableCell<DetallePedidoRow, Void> cell = new TableCell<>() {
                private final Button btn = new Button("Quitar");

                {
                    btn.setOnAction(e -> {
                        detalles.remove(getIndex());
                        actualizarTotales();
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            };
            return cell;
        });
        tablaProductos.setItems(detalles);
        tablaProductos.setEditable(true);
        btnAgregarProducto.setOnAction(e -> abrirSelectorProducto());
        detalles.addListener((javafx.collections.ListChangeListener.Change<? extends DetallePedidoRow> change) -> actualizarTotales());
        comboTasaCambio.setOnAction(e -> {
            actualizarTotales();
            actualizarTasaCambioComboVisual();
        });
        chkPagado.setOnAction(e -> actualizarEstado());
        chkEntregado.setOnAction(e -> actualizarEstado());
        actualizarEstado();
        btnGuardar.setOnAction(this::guardarPedido);
        btnCancelar.setOnAction(e -> cerrar());
    }

    public void cargarDatosIniciales() {
        try {
            List<Clientes> clientes = clientesController.obtenerTodosLosClientes();
            comboCliente.setItems(FXCollections.observableArrayList(clientes));
            List<TasadeCambio> tasas = tasaCambioController.obtenerTodasLasTasasCambio();
            comboTasaCambio.setItems(FXCollections.observableArrayList(tasas));
            if (!tasas.isEmpty()) {
                comboTasaCambio.getSelectionModel().select(0);
            }
            List<Productos> productos = productosController.obtenerTodosLosProductos();
            colProducto.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(productos)));
            if (metodosPagoController != null) {
                List<MetodosdePago> metodos = metodosPagoController.listarMetodos();
                comboMetodoPago.setItems(FXCollections.observableArrayList(metodos));
                if (!metodos.isEmpty()) {
                    comboMetodoPago.getSelectionModel().select(0);
                }
            }
            actualizarTotales();
        } catch (Exception e) {
            lblMensaje.setText("Error cargando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

public void cargarDatosDespuesDeSetController(Pedidos pedidoEditar) {
    try {
        cargarDatosIniciales(); 
        this.pedidoEnEdicion = pedidoEditar;

        comboCliente.getSelectionModel().select(
            clientesController.obtenerTodosLosClientes().stream()
                .filter(c -> c.getId() == pedidoEditar.getClienteId())
                .findFirst()
                .orElse(null)
        );

        comboTasaCambio.getSelectionModel().select(
            tasaCambioController.obtenerTodasLasTasasCambio().stream()
                .filter(t -> t.getValor().equals(pedidoEditar.getTasaCambioAplicada()))
                .findFirst()
                .orElse(null)
        );

        actualizarTasaCambioComboVisual();

        chkEntregado.setSelected(pedidoEditar.isEntregado());

        List<DetallePedido> dets = pedidosController.obtenerDetallesDePedido(pedidoEditar.getId());
        detalles.clear();
        for (DetallePedido d : dets) {
            DetallePedidoRow r = new DetallePedidoRow();
            r.setProducto(productosController.obtenerProductoPorId(d.getIdProducto()));
            r.setCantidad(d.getCantidad());
            r.setPrecioUnitario(d.getPrecioUnitario());
            r.setPrecio(d.getPrecioUnitario());
            detalles.add(r);
        }

        int idMetodoPago = pedidosController.getPedidosDAO().obtenerIdMetodoPago(pedidoEditar.getId());
        if (idMetodoPago > 0) {
            comboMetodoPago.getItems().stream()
                .filter(m -> m.getId() == idMetodoPago)
                .findFirst()
                .ifPresent(m -> comboMetodoPago.getSelectionModel().select(m));
        }

        actualizarTotales();

    } catch (Exception e) {
        lblMensaje.setText("Error cargando datos: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void actualizarTasaCambioComboVisual() {
        comboTasaCambio.setCellFactory(lv -> new ListCell<TasadeCambio>() {
            @Override
            protected void updateItem(TasadeCambio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getValor().setScale(4, RoundingMode.HALF_UP).toPlainString());
                }
            }
        });
        comboTasaCambio.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TasadeCambio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getValor().setScale(4, RoundingMode.HALF_UP).toPlainString());
                }
            }
        });
    }

    private void abrirSelectorProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/SeleccionProductoForm.fxml"));
            Parent root = loader.load();
            SeleccionProductoFormController controladorSelec = loader.getController();
            controladorSelec.setProductos(productosController.obtenerTodosLosProductos());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleccionar Producto");
            stage.initModality(Modality.APPLICATION_MODAL);

            // Tamaño inicial + mínimos
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setMinWidth(580);
            stage.setMinHeight(400);
            stage.centerOnScreen();

            stage.showAndWait();

            if (controladorSelec.isAgregado()) {
                Productos productoSel = controladorSelec.getProductoSeleccionado();
                int cantidadSel = controladorSelec.getCantidadSeleccionada();
                DetallePedidoRow nuevaFila = new DetallePedidoRow();
                nuevaFila.setProducto(productoSel);
                nuevaFila.setCantidad(cantidadSel);
                nuevaFila.setPrecioUnitario(productoSel.getPrecioUsd());
                nuevaFila.setPrecio(productoSel.getPrecioUsd());
                detalles.add(nuevaFila);
                actualizarTotales();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error al abrir selector producto: " + ex.getMessage());
        }
    }

    private void actualizarTotales() {
        BigDecimal totalUsd = BigDecimal.ZERO;
        for (DetallePedidoRow row : detalles) {
            totalUsd = totalUsd.add(row.getSubtotal());
        }
        lblTotalUsd.setText(totalUsd.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        TasadeCambio tasa = comboTasaCambio.getSelectionModel().getSelectedItem();
        if (tasa != null) {
            BigDecimal totalLocal = totalUsd.multiply(tasa.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
            lblTotalLocal.setText(totalLocal.toString());
        } else {
            lblTotalLocal.setText("0.00");
        }
    }

    private void actualizarEstado() {
        boolean pagado = chkPagado.isSelected();
        boolean entregado = chkEntregado.isSelected();
        String texto = (!pagado && !entregado) ? "Pendiente"
                : (pagado && entregado) ? "CERRADO"
                        : (pagado ? "Pagado, falta entregar" : "Entregado, falta pagar");
        lblEstado.setText(texto);
    }

    @FXML
    private void abrirFormularioNuevoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/ClienteForm.fxml"));
            Parent root = loader.load();

            ClienteFormController formController = loader.getController();

            formController.setClientesController(this.clientesController);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Nuevo Cliente");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setWidth(450);
            stage.setHeight(350);
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            stage.showAndWait();

            // Si el cliente fue guardado, actualiza el combo de clientes
            if (formController.isGuardado()) {
                Clientes clienteNuevo = formController.getClienteGuardado();
                if (clienteNuevo != null) {
                    comboCliente.getItems().add(clienteNuevo);
                    comboCliente.setValue(clienteNuevo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir formulario de nuevo cliente.");
        }
    }

    @FXML
    private void guardarPedido(ActionEvent event) {
        lblMensaje.setText("");
        try {
            Clientes cliente = comboCliente.getSelectionModel().getSelectedItem();
            TasadeCambio tasa = comboTasaCambio.getSelectionModel().getSelectedItem();
            MetodosdePago metodoPago = comboMetodoPago.getSelectionModel().getSelectedItem();

            if (cliente == null) {
                lblMensaje.setText("Debe seleccionar cliente");
                return;
            }
            if (detalles.isEmpty()) {
                lblMensaje.setText("Agregue al menos 1 producto");
                return;
            }
            for (DetallePedidoRow row : detalles) {
                if (row.getProducto() == null || row.getCantidad() <= 0 || row.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                    lblMensaje.setText("Complete todos los productos y cantidades > 0");
                    return;
                }
            }
            if (metodoPago == null) {
                lblMensaje.setText("Debe seleccionar el método de pago");
                return;
            }
            BigDecimal totalUsd = detalles.stream().map(DetallePedidoRow::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

            if (pedidoEnEdicion == null) {
                Pedidos nuevoPedido = new Pedidos(0, cliente.getId(), LocalDateTime.now(), totalUsd, tasa.getValor(), chkEntregado.isSelected(), chkPagado.isSelected());
                pedidosController.crearPedidoConDetalles(
                        nuevoPedido,
                        detalles.stream().map(r -> r.toDetalle(nuevoPedido.getId())).toList()
                );
                if (chkPagado.isSelected()) {
                    PagosPedido pago = new PagosPedido();
                    pago.setIdPedido(nuevoPedido.getId());
                    pago.setIdMetodoPago(metodoPago.getId());
                    pago.setTipoMoneda(PagosPedido.TipoMoneda.USD);
                    pago.setMonto(nuevoPedido.getTotalUsd());
                    pago.setFechaPago(LocalDateTime.now());
                    pagosPedidoController.registrarPago(pago);

                    pedidosController.actualizarEstadoPago(nuevoPedido.getId()); // Actualiza el estado pagado
                }
                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Pedido guardado correctamente.");
            } else {
                pedidoEnEdicion.setClienteId(cliente.getId());
                pedidoEnEdicion.setEntregado(chkEntregado.isSelected());
                pedidoEnEdicion.setTasaCambioAplicada(tasa.getValor());
                pedidosController.crearPedidoConDetalles(
                        pedidoEnEdicion,
                        detalles.stream().map(r -> r.toDetalle(pedidoEnEdicion.getId())).toList()
                );
                pedidosController.actualizarEstadoPago(pedidoEnEdicion.getId());  // Actualiza el estado pagado

                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Pedido actualizado correctamente.");
            }

            if (pedidosViewController != null) {
                pedidosViewController.cargarPedidos();
                pedidosViewController.getTablaPedidos().refresh();
            }

            cerrar();

        } catch (Exception ex) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error al guardar pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cerrar() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    public static class DetallePedidoRow {

        private final ObjectProperty<Productos> producto = new SimpleObjectProperty<>();
        private final IntegerProperty cantidad = new SimpleIntegerProperty(1);
        private final ObjectProperty<BigDecimal> precioUnitario = new SimpleObjectProperty<>(BigDecimal.ZERO);
        private final ObjectProperty<BigDecimal> precio = new SimpleObjectProperty<>(BigDecimal.ZERO);

        public DetallePedidoRow() {
        }

        public Productos getProducto() {
            return producto.get();
        }

        public void setProducto(Productos p) {
            producto.set(p);
            if (p != null) {
                setPrecioUnitario(p.getPrecioUsd());
                setPrecio(p.getPrecioUsd());
            }
        }

        public ObjectProperty<Productos> productoProperty() {
            return producto;
        }

        public int getCantidad() {
            return cantidad.get();
        }

        public void setCantidad(int c) {
            cantidad.set(c);
        }

        public IntegerProperty cantidadProperty() {
            return cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario.get();
        }

        public void setPrecioUnitario(BigDecimal val) {
            precioUnitario.set(val);
        }

        public ObjectProperty<BigDecimal> precioUnitarioProperty() {
            return precioUnitario;
        }

        public BigDecimal getPrecio() {
            return precio.get();
        }

        public void setPrecio(BigDecimal val) {
            precio.set(val);
        }

        public ObjectProperty<BigDecimal> precioProperty() {
            return precio;
        }

        public BigDecimal getSubtotal() {
            return getPrecio().multiply(BigDecimal.valueOf(getCantidad()));
        }

        public ReadOnlyObjectWrapper<BigDecimal> subtotalProperty() {
            return new ReadOnlyObjectWrapper<>(getSubtotal());
        }

        public DetallePedido toDetalle(int idPedido) {
            DetallePedido d = new DetallePedido();
            d.setIdPedido(idPedido);
            d.setIdProducto(getProducto().getId());
            d.setCantidad(getCantidad());
            d.setPrecioUnitario(getPrecioUnitario());
            d.setSubtotalUsd(getSubtotal());
            return d;
        }
    }
}
