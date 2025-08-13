package com.mycompany.pay.orders.controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.dao.UsuarioDAOImpl;
import com.mycompany.pay.orders.model.Rol;
import com.mycompany.pay.orders.model.Usuario;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class RegistroController {

    @FXML private TextField txtNuevoUsername;
    @FXML private PasswordField txtNuevoPassword;
    @FXML private ComboBox<String> comboRol;
    @FXML private Label lblMensajeRegistro;

    private UsuarioController usuarioController;

    public RegistroController() {
        try {
            Connection connection = obtenerConexionDB();
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl(connection);
            this.usuarioController = new UsuarioController(usuarioDAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   @FXML
private void initialize() {
    comboRol.setItems(FXCollections.observableArrayList("ADMIN", "ENCARGADO"));
    comboRol.getSelectionModel().selectFirst();
}


    private Connection obtenerConexionDB() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/pay_orders_db";
        String usuario = "admin";
        String password = "admin123";
        return DriverManager.getConnection(url, usuario, password);
    }
@FXML
private void handleGuardarUsuario() {
    String username = txtNuevoUsername.getText().trim();
    String password = txtNuevoPassword.getText().trim();
    String rolStr = comboRol.getSelectionModel().getSelectedItem(); // valor String del combo
    
    if(username.isEmpty() || password.isEmpty() || rolStr == null) {
        lblMensajeRegistro.setStyle("-fx-text-fill: red;");
        lblMensajeRegistro.setText("Por favor complete todos los campos.");
        return;
    }

    try {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(username);
        nuevoUsuario.setPassword(password);
        
        Rol rolEnum = Rol.valueOf(rolStr);
        nuevoUsuario.setRol(rolEnum);

        nuevoUsuario.setActivo(true);

        usuarioController.agregarUsuario(nuevoUsuario);

        lblMensajeRegistro.setStyle("-fx-text-fill: green;");
        lblMensajeRegistro.setText("Usuario registrado exitosamente.");
        txtNuevoUsername.clear();
        txtNuevoPassword.clear();
        comboRol.getSelectionModel().selectFirst();

    } catch (Exception e) {
        lblMensajeRegistro.setStyle("-fx-text-fill: red;");
        lblMensajeRegistro.setText("Error: " + e.getMessage());
        e.printStackTrace();
    }
}

    @FXML
    private void volverLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/LoginView.fxml"));
            Stage stage = (Stage) txtNuevoUsername.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Inicio de Sesión - Sistema de Pedidos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
