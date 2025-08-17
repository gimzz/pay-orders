package com.mycompany.pay.orders.model;

import java.time.LocalDateTime;
import javafx.beans.property.*;

public class Usuario {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nombreUsuario;
    private String password;
    private ObjectProperty<Rol> rol;
    private SimpleBooleanProperty activo;
    private LocalDateTime fechaCreacion;

    public Usuario() {
        this.id = new SimpleIntegerProperty();
        this.nombreUsuario = new SimpleStringProperty();
        this.rol = new SimpleObjectProperty<>();
        this.activo = new SimpleBooleanProperty();
    }

    public Usuario(int id, String nombreUsuario, String password, Rol rol, boolean activo, LocalDateTime fechaCreacion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombreUsuario = new SimpleStringProperty(nombreUsuario);
        this.password = password;
        this.rol = new SimpleObjectProperty<>(rol);
        this.activo = new SimpleBooleanProperty(activo);
        this.fechaCreacion = fechaCreacion;
    }


    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario.get();
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario.set(nombreUsuario);
    }
    public StringProperty nombreUsuarioProperty() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol.get();
    }
    public void setRol(Rol rol) {
        this.rol.set(rol);
    }
    public ObjectProperty<Rol> rolProperty() {
        return rol;
    }

    public boolean isActivo() {
        return activo.get();
    }
    public void setActivo(boolean activo) {
        this.activo.set(activo);
    }
    public BooleanProperty activoProperty() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public StringProperty estadoProperty() {
        return new SimpleStringProperty(isActivo() ? "Activo" : "Inactivo");
    }

    public StringProperty rolPropertyString() {
        return new SimpleStringProperty(getRol() != null ? getRol().toString() : "");
    }
}
