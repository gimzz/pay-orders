package com.mycompany.pay.orders.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pay.orders.dao.DetallePedidoDAOImpl;
import com.mycompany.pay.orders.dao.PagosPedidoDAO;
import com.mycompany.pay.orders.dao.PedidosDAOImpl;
import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.DetallePedido;
import com.mycompany.pay.orders.model.Pedidos;
import com.mycompany.pay.orders.model.TasadeCambio;

public class PedidosController {

    private PedidosDAOImpl pedidosDAO;
    private DetallePedidoDAOImpl detallesDAO;
    private ProductosDAO productosDAO;
    private TasadeCambioController tasaCambioController;
    private Connection connection;
    private PagosPedidoDAO pagosPedidoDAO;

    public PedidosController(Connection connection, ProductosDAO productosDAO, PagosPedidoDAO pagosPedidoDAO,
                             TasadeCambioController tasaCambioController) {
        this.connection = connection;
        this.pedidosDAO = new PedidosDAOImpl(connection);
        this.detallesDAO = new DetallePedidoDAOImpl(connection);
        this.productosDAO = productosDAO;
        this.pagosPedidoDAO = pagosPedidoDAO;
        this.tasaCambioController = tasaCambioController;
    }

    public void crearPedidoConDetalles(Pedidos pedido, List<DetallePedido> detalles) throws SQLException {
        try {
            connection.setAutoCommit(false);

            if (pedido.getTasaCambioAplicada() == null || pedido.getTasaCambioAplicada().compareTo(BigDecimal.ZERO) <= 0) {
                TasadeCambio ultimaTasa = tasaCambioController.obtenerUltimaTasaCambio();
                BigDecimal tasa = ultimaTasa != null ? ultimaTasa.getValor() : BigDecimal.ONE;
                pedido.setTasaCambioAplicada(tasa);
            }
            pedido.setTotalUsd(BigDecimal.ZERO);

            pedidosDAO.crearPedido(pedido);

            for (DetallePedido detalle : detalles) {
                detalle.setIdPedido(pedido.getId());
                detallesDAO.agregarDetalle(detalle);
            }

            actualizarTotalPedido(pedido.getId());

            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public List<DetallePedido> obtenerDetallesPorCliente(int clienteId) throws SQLException {
    List<Pedidos> pedidosCliente = pedidosDAO.obtenerPedidosPorCliente(clienteId);
    List<DetallePedido> detallesTotales = new ArrayList<>();
    for (Pedidos pedido : pedidosCliente) {
        List<DetallePedido> detallesPedido = detallesDAO.obtenerDetallesPorPedido(pedido.getId());
        detallesTotales.addAll(detallesPedido);
    }
    return detallesTotales;
}


    public Pedidos obtenerPedido(int id) throws SQLException {
        return pedidosDAO.obtenerPedidoPorId(id);
    }

    public List<DetallePedido> obtenerDetallesDePedido(int idPedido) throws SQLException {
        return detallesDAO.obtenerDetallesPorPedido(idPedido);
    }

   private void actualizarTotalPedido(int pedidoId) throws SQLException {
    List<DetallePedido> detalles = detallesDAO.obtenerDetallesPorPedido(pedidoId);
    BigDecimal total = BigDecimal.ZERO;

    for (DetallePedido detalle : detalles) {
        if (detalle.getSubtotalUsd() != null) {
            total = total.add(detalle.getSubtotalUsd());
        } else {
            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }
    }

    total = total.setScale(2, java.math.RoundingMode.HALF_UP);

    pedidosDAO.actualizarTotalPedido(pedidoId, total);
}


    public void validarModificacionPedido(int pedidoId) throws SQLException {
        Pedidos pedido = pedidosDAO.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        String estadoPago = pedidosDAO.obtenerEstadoPago(pedidoId);
        boolean entregado = pedido.isEntregado();
        if (entregado && "PAGADO".equalsIgnoreCase(estadoPago)) {
            throw new IllegalStateException("No se pueden modificar pedidos cerrados (pagados y entregados).");
        }
    }

    public void agregarDetalle(DetallePedido detalle) throws SQLException {
        validarModificacionPedido(detalle.getIdPedido());
        detallesDAO.agregarDetalle(detalle);
        actualizarTotalPedido(detalle.getIdPedido());
    }

    public void modificarDetalle(DetallePedido detalle) throws SQLException {
        validarModificacionPedido(detalle.getIdPedido());
        detallesDAO.actualizarDetalle(detalle);
        actualizarTotalPedido(detalle.getIdPedido());
    }

    public void eliminarDetalle(int idDetalle) throws SQLException {
        DetallePedido detalle = detallesDAO.obtenerDetallePorId(idDetalle);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle no encontrado");
        }
        validarModificacionPedido(detalle.getIdPedido());
        detallesDAO.eliminarDetalle(idDetalle);
        actualizarTotalPedido(detalle.getIdPedido());
    }

    public void actualizarEstadoEntrega(int pedidoId, boolean entregado) throws SQLException {
        pedidosDAO.actualizarEstadoEntrega(pedidoId, entregado);
        double totalPagado = pagosPedidoDAO.obtenerTotalPagadoPorPedido(pedidoId);
        Pedidos pedido = pedidosDAO.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        double totalPedido = pedido.getTotalUsd().doubleValue();
        String nuevoEstadoPago = (totalPagado >= totalPedido) ? "PAGADO" : "PENDIENTE";
        pedidosDAO.actualizarEstadoPago(pedidoId, nuevoEstadoPago);
    }

    public void eliminarPedidoCompleto(int pedidoId) throws SQLException {
        try {
            connection.setAutoCommit(false);
            detallesDAO.eliminarDetallesPorPedido(pedidoId);
            pedidosDAO.eliminarPedido(pedidoId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Pedidos> obtenerTodosLosPedidos() throws SQLException {
        return pedidosDAO.obtenerTodosLosPedidos();
    }

    public PedidosDAOImpl getPedidosDAO() {
        return pedidosDAO;
    }
}

