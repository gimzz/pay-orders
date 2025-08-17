package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.model.Clientes;
import com.mycompany.pay.orders.model.DetallePedido;
import com.mycompany.pay.orders.model.PagosPedido;
import com.mycompany.pay.orders.model.Pedidos;
import com.mycompany.pay.orders.model.Productos;
import com.mycompany.pay.orders.model.TasadeCambio;
import com.mycompany.pay.orders.dao.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javafx.fxml.FXMLLoader;

public class PedidoFormController {
    @FXML private ComboBox<Clientes> comboCliente;
    @FXML private Button btnNuevoCliente;
    @FXML private TableView<DetallePedidoRow> tablaProductos;
    @FXML private TableColumn<DetallePedidoRow, Productos> colProducto;
    @FXML private TableColumn<DetallePedidoRow, Integer> colCantidad;
    @FXML private TableColumn<DetallePedidoRow, BigDecimal> colPrecio;
    @FXML private TableColumn<DetallePedidoRow, BigDecimal> colSubtotal;
    @FXML private TableColumn<DetallePedidoRow, Void> colQuitar;
    @FXML private Button btnAgregarProducto;
    @FXML private ComboBox<TasadeCambio> comboTasaCambio;
    @FXML private Label lblTotalUsd, lblTotalLocal;
    @FXML private CheckBox chkPagado, chkEntregado;
    @FXML private Label lblEstado, lblMensaje;
    @FXML private Button btnGuardar, btnCancelar;

    private PedidosController pedidosController;
    private ProductosController productosController;
    private TasadeCambioController tasaCambioController;
    private ClientesController clientesController;
    private PagosPedidoController pagosPedidoController;

    private ObservableList<DetallePedidoRow> detalles = FXCollections.observableArrayList();

    public void setControllers(
        PedidosController pedidosCtl, ProductosController productosCtl,
        TasadeCambioController tasaCtl, ClientesController clientesCtl,
        PagosPedidoController pagosCtl
    ) {
        this.pedidosController = pedidosCtl;
        this.productosController = productosCtl;
        this.tasaCambioController = tasaCtl;
        this.clientesController = clientesCtl;
        this.pagosPedidoController = pagosCtl;
    }

    public void cargarDatosDespuesDeSetController() {
        try {
            List<Clientes> clientes = clientesController.obtenerTodosLosClientes();
            comboCliente.setItems(FXCollections.observableArrayList(clientes));

            List<TasadeCambio> tasas = tasaCambioController.obtenerTodasLasTasasCambio();
            comboTasaCambio.setItems(FXCollections.observableArrayList(tasas));
            if (!tasas.isEmpty()) {
                comboTasaCambio.getSelectionModel().select(0);
            }

            List<Productos> productos = productosController.obtenerTodosLosProductos();

            colProducto.setCellValueFactory(cellData -> cellData.getValue().productoProperty());
            colProducto.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(productos)));

            colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
            colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty());
            colPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

            colSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty());

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

            btnAgregarProducto.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/SeleccionProductoForm.fxml"));
                    Parent root = loader.load();
                    SeleccionProductoFormController controladorSelec = loader.getController();
                    controladorSelec.setProductos(productosController.obtenerTodosLosProductos());
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Seleccionar Producto");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    if (controladorSelec.isAgregado()) {
                        Productos productoSel = controladorSelec.getProductoSeleccionado();
                        int cantidadSel = controladorSelec.getCantidadSeleccionada();
                        DetallePedidoRow nuevaFila = new DetallePedidoRow();
                        nuevaFila.setProducto(productoSel);
                        nuevaFila.setCantidad(cantidadSel);
                        nuevaFila.setPrecio(productoSel.getPrecioUsd());
                        detalles.add(nuevaFila);
                        actualizarTotales();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    lblMensaje.setText("Error abriendo selector producto: " + ex.getMessage());
                }
            });

            detalles.addListener((javafx.collections.ListChangeListener.Change<? extends DetallePedidoRow> change) -> actualizarTotales());
            comboTasaCambio.setOnAction(e -> actualizarTotales());
            chkPagado.setOnAction(e -> actualizarEstado());
            chkEntregado.setOnAction(e -> actualizarEstado());
            actualizarEstado();
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error cargando datos: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
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
    private void guardarPedido(ActionEvent event) {
        lblMensaje.setText("");
        try {
            Clientes cliente = comboCliente.getSelectionModel().getSelectedItem();
            TasadeCambio tasa = comboTasaCambio.getSelectionModel().getSelectedItem();
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
            Pedidos nuevoPedido = new Pedidos(0, cliente.getId(), LocalDateTime.now(), BigDecimal.ZERO, tasa.getValor(), chkEntregado.isSelected());

            pedidosController.crearPedidoConDetalles(
                    nuevoPedido,
                    detalles.stream().map(r -> r.toDetalle(nuevoPedido.getId())).toList()
            );

            if (chkPagado.isSelected()) {
                if (pagosPedidoController == null) {
                    lblMensaje.setText("Error: controlador de pagos no inicializado.");
                    return;
                }
                PagosPedido pago = new PagosPedido();
                pago.setIdPedido(nuevoPedido.getId());
                pago.setIdMetodoPago(1); // Ajusta según método válido
                pago.setTipoMoneda(PagosPedido.TipoMoneda.USD);
                pago.setMonto(nuevoPedido.getTotalUsd());
                pago.setFechaPago(LocalDateTime.now());
                pagosPedidoController.registrarPago(pago);
            }

            ((Stage) btnGuardar.getScene().getWindow()).close();
        } catch (Exception ex) {
            lblMensaje.setText("Error al guardar pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static class DetallePedidoRow {
        private final ObjectProperty<Productos> producto = new SimpleObjectProperty<>();
        private final IntegerProperty cantidad = new SimpleIntegerProperty(1);
        private final ObjectProperty<BigDecimal> precio = new SimpleObjectProperty<>(BigDecimal.ZERO);

        public DetallePedidoRow() {}

        public Productos getProducto() {
            return producto.get();
        }
        public void setProducto(Productos p) {
            producto.set(p);
            if (p != null) {
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
            d.setPrecioUnitario(getPrecio());
            d.setSubtotalUsd(getSubtotal());
            return d;
        }
    }
}
