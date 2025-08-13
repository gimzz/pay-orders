package com.mycompany.pay.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author gimz
 */
public class PagosPedido {

    private int id;
    private int idPedido;
    private int idMetodoPago;
    private TipoMoneda tipoMoneda;  
    private BigDecimal monto;
    private LocalDateTime fechaPago;

    public enum TipoMoneda {
        VES,
        USD
    }

    public PagosPedido() {
    }

    public PagosPedido(int id, int idPedido, int idMetodoPago, TipoMoneda tipoMoneda, BigDecimal monto, LocalDateTime fechaPago) {
        this.id = id;
        this.idPedido = idPedido;
        this.idMetodoPago = idMetodoPago;
        this.tipoMoneda = tipoMoneda;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

}
