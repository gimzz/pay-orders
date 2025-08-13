
package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.PagosPedidoDAO;
import com.mycompany.pay.orders.model.PagosPedido;
import com.mycompany.pay.orders.model.PagosPedido.TipoMoneda;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para manejar la lógica de negocio relacionada con Pagos de Pedido.
 * Usa internamente un PagosPedidoDAO para acceder a la base de datos.
 * 
 * @author gimz
 */
public class PagosPedidoController {

    private final PagosPedidoDAO pagosPedidoDAO;

    public PagosPedidoController(PagosPedidoDAO pagosPedidoDAO) {
        this.pagosPedidoDAO = pagosPedidoDAO;
    }
    
    public void registrarPago(PagosPedido pago) throws SQLException {
        if (pago == null) {
            throw new IllegalArgumentException("El objeto pago no puede ser null");
        }
        if (pago.getIdPedido() <= 0) {
            throw new IllegalArgumentException("El id del pedido es inválido");
        }
        if (pago.getIdMetodoPago() <= 0) {
            throw new IllegalArgumentException("El id del método de pago es inválido");
        }
        if (pago.getTipoMoneda() == null) {
            throw new IllegalArgumentException("El tipo de moneda es obligatorio");
        }
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now()); 
        }
        pagosPedidoDAO.registrarPago(pago);
    }

    public List<PagosPedido> obtenerPagosPorPedido(int idPedido) throws SQLException {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("idPedido inválido");
        }
        return pagosPedidoDAO.obtenerPagosPorPedido(idPedido);
    }

   
    public double obtenerTotalPagadoPorPedido(int idPedido) throws SQLException {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("idPedido inválido");
        }
        return pagosPedidoDAO.obtenerTotalPagadoPorPedido(idPedido);
    }

 
    public void eliminarPago(int idPago) throws SQLException {
        if (idPago <= 0) {
            throw new IllegalArgumentException("idPago inválido");
        }
        pagosPedidoDAO.eliminarPago(idPago);
    }
}