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
public class Clientes {

    private int id;
    private int cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDateTime fechaRegistro;

    public Clientes(int id, int cedula, String nombre, String apellido, String telefono, LocalDateTime fechaRegistro) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }

    public Clientes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        if (cedula <= 0) {
            throw new IllegalArgumentException("La cedula debe ser mayor a cero (0)");
        }

        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if (!nombre.matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El nombre no puede contener caracteres especiales ni números");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser vacío");
        }
        if (!apellido.matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El apellido no puede contener caracteres especiales ni números");
        }
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        if (fechaRegistro != null && fechaRegistro.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de registro no puede ser futura");
        }
        this.fechaRegistro = fechaRegistro;
    }

}
