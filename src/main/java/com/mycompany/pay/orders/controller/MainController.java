package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MateriaPrimaDAO;
import com.mycompany.pay.orders.dao.MateriaPrimaDAOImpl;
import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAO;
import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAOImpl;
import com.mycompany.pay.orders.dao.ClientesDAO;
import com.mycompany.pay.orders.dao.ClientesDAOImpl;
import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.dao.UsuarioDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainController {

    @FXML
    private StackPane panelContenido;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private void initialize() {
        abrirPanelUsuarios();
    }

    @FXML
    private void abrirPanelUsuarios() {
        cargarVistaEnPanel("/com/mycompany/pay/orders/view/UsuariosView.fxml");
    }

    @FXML
    private void abrirPanelPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/PedidosView.fxml"));
            Parent vista = loader.load();

            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/pay_orders_db",
                    "admin",
                    "admin123"
            );

            ClientesDAO clientesDAO = new ClientesDAOImpl(connection);
            ClientesController clientesController = new ClientesController(clientesDAO);

            PedidosViewController pedidosViewController = loader.getController();
            pedidosViewController.setControllers(connection, clientesController);

            panelContenido.getChildren().clear();
            panelContenido.getChildren().add(vista);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar vista Pedidos", e.getMessage());
        }
    }
@FXML
private void abrirPanelMateriaPrima() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/MateriaPrima.fxml"));
        Parent vista = loader.load();

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/pay_orders_db",
                "admin",
                "admin123"
        );

        MateriaPrimaDAO materiaPrimaDAO = new MateriaPrimaDAOImpl(connection);
        MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO = new MovimientoMateriaPrimaDAOImpl(connection);

        MateriaPrimaController materiaPrimaController = loader.getController();
        materiaPrimaController.setDAOs(materiaPrimaDAO, movimientoMateriaPrimaDAO);

        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(vista);

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlertaError("Error al cargar vista Materia Prima", e.getMessage());
    }
}

    private void cargarVistaEnPanel(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            Object controller = loader.getController();
            if (controller instanceof UsuarioController) {
                UsuarioController usuarioController = (UsuarioController) controller;
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/pay_orders_db",
                        "admin",
                        "admin123"
                );
                UsuarioDAO usuarioDAO = new UsuarioDAOImpl(connection);
                usuarioController.setUsuarioDAO(usuarioDAO);
            }
            panelContenido.getChildren().clear();
            panelContenido.getChildren().add(vista);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar vista", "No se pudo cargar la vista: " + rutaFXML + "\nDetalles: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error inesperado", "Ocurrió un error inesperado:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            Stage stagePrincipal = (Stage) btnCerrarSesion.getScene().getWindow();
            stagePrincipal.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Inicio de Sesión - Sistema de Pedidos");
            loginStage.setWidth(700);
            loginStage.setHeight(400);
            loginStage.setMinWidth(500);
            loginStage.setMinHeight(350);
            loginStage.show();
            loginStage.sizeToScene();
            loginStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cerrar sesión",
                    "No se pudo abrir la ventana de inicio de sesión.\nDetalles: " + e.getMessage());
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
