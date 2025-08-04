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
    public class Usuario {
        private int id;
        private String nombreUsuario;
        private String password;
        private Rol rol;
        private boolean activo;
        private LocalDateTime fechaCreacion;
//constructor 
        
        public Usuario() {
}
    public Usuario(int id, String nombreUsuario, String password, Rol rol, boolean activo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        
        
        
        
    }

    
    //encapsulamiento
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
        
        
        
    }
