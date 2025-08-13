package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.DetallePedidoDAOImpl;
import com.mycompany.pay.orders.dao.PagosPedidoDAO;
import com.mycompany.pay.orders.dao.PedidosDAOImpl;
import com.mycompany.pay.orders.dao.ProductosDAO;
import com.mycompany.pay.orders.model.DetallePedido;
import com.mycompany.pay.orders.model.Pedidos;
import com.mycompany.pay.orders.controller.TasadeCambioController;
import com.mycompany.pay.orders.model.TasadeCambio;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PedidosController {

    private PedidosDAOImpl pedidosDAO;
    private DetallePedidoDAOImpl detallesDAO;
    private ProductosDAO productosDAO;
    private TasadeCambioController tasaCambioController;

    private Connection connection;
    private PagosPedidoDAO pagosPedidoDAO;

    public PedidosController(Connection connection, ProductosDAO productosDAO, PagosPedidoDAO pagosPedidoDAO, TasadeCambioController tasaCambioController) {
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
                if (ultimaTasa == null) {
                    BigDecimal tasaPorDefecto = BigDecimal.ONE;
                    pedido.setTasaCambioAplicada(tasaPorDefecto);
                    System.out.println("No existe tasa de cambio registrada aún, se usará la tasa por defecto: " + tasaPorDefecto);
                } else {
                    pedido.setTasaCambioAplicada(ultimaTasa.getValor());
                    System.out.println("No se proporcionó tasa; se aplicó la última tasa registrada: " + ultimaTasa.getValor());
                }
            }

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

    public Pedidos obtenerPedido(int id) throws SQLException {
        return pedidosDAO.obtenerPedidoPorId(id);
    }

    public List<DetallePedido> obtenerDetallesDePedido(int idPedido) throws SQLException {
        return detallesDAO.obtenerDetallesPorPedido(idPedido);
    }

    public void actualizarEstadoEntrega(int pedidoId, boolean entregado) throws SQLException {
        Pedidos pedido = pedidosDAO.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        double totalPagado = pagosPedidoDAO.obtenerTotalPagadoPorPedido(pedidoId);
        double totalPedido = pedido.getTotalUsd().doubleValue();

        if (entregado && totalPagado < totalPedido) {
            System.out.println("Advertencia: El pedido se marca como entregado, pero no está pagado completamente.");
        } else if (!entregado && totalPagado >= totalPedido) {
            System.out.println("Atención: El pedido está pagado pero aún no entregado.");
        }

        pedidosDAO.actualizarEstadoEntrega(pedidoId, entregado);
        String nuevoEstadoPago = (totalPagado >= totalPedido) ? "PAGADO" : "PENDIENTE_DE_PAGO";
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

    public BigDecimal obtenerPrecioUnitarioProducto(int id) throws SQLException {
        return productosDAO.obtenerPrecioUnitarioProducto(id);
    }

    public List<Pedidos> obtenerTodosLosPedidos() throws SQLException {
        return pedidosDAO.obtenerTodosLosPedidos();
    }

    public List<Pedidos> obtenerPedidosEntregados() throws SQLException {
        return pedidosDAO.obtenerPedidosEntregados();
    }

    public List<Pedidos> obtenerPedidosNoEntregados() throws SQLException {
        return pedidosDAO.obtenerPedidosNoEntregados();
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

    public void agregarDetalle(DetallePedido detalle) throws SQLException {
        validarModificacionPedido(detalle.getIdPedido());
        detallesDAO.agregarDetalle(detalle);
        actualizarTotalPedido(detalle.getIdPedido());
    }

    public boolean isPedidoCerrado(int pedidoId) throws SQLException {
        Pedidos pedido = pedidosDAO.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        String estadoPago = pedidosDAO.obtenerEstadoPago(pedidoId);
        return pedido.isEntregado() && "PAGADO".equalsIgnoreCase(estadoPago);
    }

private void actualizarTotalPedido(int pedidoId) throws SQLException {
    List<DetallePedido> detalles = detallesDAO.obtenerDetallesPorPedido(pedidoId);
    BigDecimal total = BigDecimal.ZERO;  // Inicializa total en cero antes del ciclo
    for (DetallePedido d : detalles) {
        BigDecimal cantidad = new BigDecimal(d.getCantidad());
        BigDecimal subtotal = d.getPrecioUnitario().multiply(cantidad);
        total = total.add(subtotal);
    }
    pedidosDAO.actualizarTotalPedido(pedidoId, total);
}

}
