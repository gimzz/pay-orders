package com.mycompany.pay.orders.model;

/**
 *
 * @author gimz
 */
public class MetodosdePago {
    private int id;
    private String descripcion;
    private boolean activo;
    
    public MetodosdePago(){}
    
    public MetodosdePago(int id, String descripcion, boolean activo) {
        this.id = id;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return descripcion != null ? descripcion : "Método de pago sin descripción";
    }
}
