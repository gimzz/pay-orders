/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.model;

import java.time.LocalDateTime;

/**
 *
 * @author gimz
 */
public class MovimientoInventario {
    private int id; 
    private int idProducto;
    private String tipoMovimiento;
    private int cantidad;
    private LocalDateTime FechaMovimiento;
    private String motivo;

    public MovimientoInventario(){}
    
    public MovimientoInventario(int id, int idProducto, String tipoMovimiento, int cantidad, LocalDateTime fehaMovimiento, String motivo) {
        this.id = id;
        this.idProducto = idProducto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.FechaMovimiento = fehaMovimiento;
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaMovimiento() {
        return FechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fehaMovimiento) {
        this.FechaMovimiento = fehaMovimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
    
    
}
