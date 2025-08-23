package com.mycompany.pay.orders.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.model.Rol;
import com.mycompany.pay.orders.model.Usuario;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UsuarioFormController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<Rol> comboRol;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblMensaje;

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioEdicion; 
    private boolean guardado = false; 
private Usuario usuarioGuardado;

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @FXML
    private void initialize() {
        comboRol.setItems(FXCollections.observableArrayList(Arrays.asList(Rol.values())));
    }

    public void cargarUsuario(Usuario usuario) {
        this.usuarioEdicion = usuario;
        txtUsuario.setText(usuario.getNombreUsuario());
        comboRol.setValue(usuario.getRol());
        chkActivo.setSelected(usuario.isActivo());
    }

    public boolean isGuardado() {
        return guardado;
    }
public Usuario getUsuarioGuardado() {
    return usuarioGuardado;
}

  @FXML
private void handleGuardar() {
    String nombreUsuario = txtUsuario.getText().trim();
    String password = txtPassword.getText();
    Rol rol = comboRol.getValue();
    boolean activo = chkActivo.isSelected();

    if (nombreUsuario.isEmpty() || (usuarioEdicion == null && password.isEmpty()) || rol == null) {
        lblMensaje.setText("Por favor, completa todos los campos obligatorios.");
        return;
    }

    if (password.length() > 0 && password.length() < 6) {
        lblMensaje.setText("La contraseña debe tener al menos 6 caracteres.");
        return;
    }

    try {
        if (usuarioEdicion == null) {
            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(nombreUsuario);
            nuevo.setPassword(password);
            nuevo.setRol(rol);
            nuevo.setActivo(activo);
            nuevo.setFechaCreacion(LocalDateTime.now());

            usuarioDAO.agregarUsuario(nuevo);
            usuarioGuardado = nuevo;

        } else {
            usuarioEdicion.setNombreUsuario(nombreUsuario);
            if (!password.isEmpty()) {
                usuarioEdicion.setPassword(password);
            }
            usuarioEdicion.setRol(rol);
            usuarioEdicion.setActivo(activo);

            usuarioDAO.actualizarUsuario(usuarioEdicion);
            usuarioGuardado = usuarioEdicion; // Guardamos el usuario actualizado
        }
        guardado = true;
        cerrarVentana();
    } catch (SQLException e) {
        lblMensaje.setText("Error al guardar el usuario: " + e.getMessage());
        e.printStackTrace();
    }
}


    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        stage.close();
    }
}
