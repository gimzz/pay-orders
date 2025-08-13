package com.mycompany.pay.orders.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private Button btnCerrarSesion;  

    @FXML
    private void handleCerrarSesion() {
        Stage stagePrincipal = (Stage) btnCerrarSesion.getScene().getWindow();
        stagePrincipal.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Inicio de Sesión - Sistema de Pedidos");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
