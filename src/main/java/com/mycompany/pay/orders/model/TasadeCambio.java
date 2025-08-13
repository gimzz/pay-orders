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
public class TasadeCambio {
    private int id;
    private LocalDateTime fechaTasaCambio;
    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal valor;

    public TasadeCambio(){}
    
    public TasadeCambio(int id, LocalDateTime fechaTasaCambio, String monedaOrigen, String monedaDestino, BigDecimal valor) {
        this.id = id;
        this.fechaTasaCambio = fechaTasaCambio;
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaTasaCambio() {
        return fechaTasaCambio;
    }

    public void setFechaTasaCambio(LocalDateTime fechaTasaCambio) {
        this.fechaTasaCambio = fechaTasaCambio;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    
}
