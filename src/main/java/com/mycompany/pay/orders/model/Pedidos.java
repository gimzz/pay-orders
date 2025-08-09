/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author gimz
 */
public class Pedidos {

    private int id;
    private int clienteId;
    private LocalDateTime fechaPedido;
    private BigDecimal totalUsd;
    private BigDecimal tasaCambioAplicada;
    private boolean entregado;

    public Pedidos() {
    }

    public Pedidos(int id, int clienteId, LocalDateTime fechaPedido, BigDecimal totalUsd, BigDecimal tasaCambioAplicada, boolean entregado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.totalUsd = totalUsd;
        this.tasaCambioAplicada = tasaCambioAplicada;
        this.entregado = entregado;
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
}
