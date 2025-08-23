package com.mycompany.pay.orders.model;

import java.time.LocalDateTime;

public class MovimientoMateriaPrima {
    private int id;
    private int idMateriaPrima;
    private String tipoMovimiento;
    private int cantidad;
    private String motivo;
    private LocalDateTime fechaMovimiento;

    public MovimientoMateriaPrima() {
    }

    public MovimientoMateriaPrima(int id, int idMateriaPrima, String tipoMovimiento, int cantidad, String motivo, LocalDateTime fechaMovimiento) {
        this.id = id;
        this.idMateriaPrima = idMateriaPrima;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fechaMovimiento = fechaMovimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(int idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}
