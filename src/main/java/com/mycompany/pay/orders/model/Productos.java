package com.mycompany.pay.orders.model;

import java.math.BigDecimal;

public class Productos {
    private int id;
    private String nombre;
    private BigDecimal precioUsd;
    private int stockActual;
    private int stockMinimo;
    private boolean activo;
    private Integer idCategoria;  

    public Productos(int id, String nombre, BigDecimal precioUsd, int stockActual, int stockMinimo, boolean activo) {
    this(id, nombre, precioUsd, stockActual, stockMinimo, activo, null);
}

    public Productos(int id, String nombre, BigDecimal precioUsd, int stockActual, int stockMinimo, boolean activo, Integer idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.precioUsd = precioUsd;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.idCategoria = idCategoria;
    }
    
    
    
    public Productos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioUsd() {
        return precioUsd;
    }

    public void setPrecioUsd(BigDecimal precioUsd) {
        this.precioUsd = precioUsd;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
        public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
}
