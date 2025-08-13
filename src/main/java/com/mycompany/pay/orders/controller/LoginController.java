package com.mycompany.pay.orders.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.dao.UsuarioDAOImpl;
import com.mycompany.pay.orders.model.Usuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;
    @FXML private Button btnLogin;

    @FXML private ImageView imageView;  

    private UsuarioController usuarioController;

    public LoginController() {
        try {
            Connection connection = obtenerConexionDB();
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl(connection);
            this.usuarioController = new UsuarioController(usuarioDAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método que se ejecuta al cargar el controlador, carga la imagen
    @FXML
    public void initialize() {
        try {
            Image img = new Image(getClass().getResourceAsStream("/images/fullempanada.png"));
            imageView.setImage(img);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection obtenerConexionDB() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/pay_orders_db";
        String usuario = "admin";
        String password = "admin123";
        return DriverManager.getConnection(url, usuario, password);
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Por favor, ingresa usuario y contraseña.");
            return;
        }

        try {
            Optional<Usuario> usuarioOpt = usuarioController.obtenerTodosLosUsuarios()
                .stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(username))
                .findFirst();

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                if (password.equals(usuario.getPassword())) {
                    lblMessage.setStyle("-fx-text-fill: green;");
                    lblMessage.setText("¡Inicio de sesión exitoso! Bienvenido " + usuario.getNombreUsuario());

                    abrirVentanaPrincipal();

                    // Cerrar ventana de login
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();

                } else {
                    lblMessage.setStyle("-fx-text-fill: red;");
                    lblMessage.setText("Contraseña incorrecta.");
                }
            } else {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("Usuario no encontrado.");
            }

        } catch (SQLException e) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Error al consultar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/RegistroView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Nuevo Usuario");
            stage.show();

            // Cerrar ventana login
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Error al abrir la ventana de registro.");
        }
    }

    private void abrirVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Pedidos - Ventana Principal");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Error al abrir la ventana principal.");
        }
    }

}
