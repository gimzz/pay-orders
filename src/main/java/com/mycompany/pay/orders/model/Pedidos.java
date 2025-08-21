package com.mycompany.pay.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Pedidos {

    private int id;
    private int clienteId;
    private LocalDateTime fechaPedido;
    private BigDecimal totalUsd;
    private BigDecimal tasaCambioAplicada;
    private boolean entregado;
    private BooleanProperty pagado;

    public Pedidos() {
        this.pagado = new SimpleBooleanProperty(this, "pagado", false);
    }

    public Pedidos(int id, int clienteId, LocalDateTime fechaPedido, BigDecimal totalUsd,
                   BigDecimal tasaCambioAplicada, boolean entregado, boolean pagado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.totalUsd = totalUsd;
        this.tasaCambioAplicada = tasaCambioAplicada;
        this.entregado = entregado;
        this.pagado = new SimpleBooleanProperty(this, "pagado", pagado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }

    public BigDecimal getTasaCambioAplicada() {
        return tasaCambioAplicada;
    }

    public void setTasaCambioAplicada(BigDecimal tasaCambioAplicada) {
        this.tasaCambioAplicada = tasaCambioAplicada;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public boolean isPagado() {
        return pagado.get();
    }

    public void setPagado(boolean pagado) {
        this.pagado.set(pagado);
    }

    public BooleanProperty pagadoProperty() {
        return pagado;
    }
}
