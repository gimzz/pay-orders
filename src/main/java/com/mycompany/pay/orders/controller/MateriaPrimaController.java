package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.MateriaPrimaDAO;
import com.mycompany.pay.orders.dao.MovimientoMateriaPrimaDAO;
import com.mycompany.pay.orders.model.MateriaPrima;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MateriaPrimaController {

    @FXML
    private TableView<MateriaPrima> tablaMateriaPrima;
    @FXML
    private TableColumn<MateriaPrima, String> colNombre;
    @FXML
    private TableColumn<MateriaPrima, String> colDescripcion;
    @FXML
    private TableColumn<MateriaPrima, String> colUnidadMedida;

    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;

    @FXML
    private Label lblMensaje;
    @FXML
    private Button btnRegistrarMovimiento;
    private ObservableList<MateriaPrima> listaMateriaPrima;
 private MateriaPrimaDAO materiaPrimaDAO;
private MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO;

public void setDAOs(MateriaPrimaDAO materiaPrimaDAO, MovimientoMateriaPrimaDAO movimientoMateriaPrimaDAO) {
    this.materiaPrimaDAO = materiaPrimaDAO;
    this.movimientoMateriaPrimaDAO = movimientoMateriaPrimaDAO;
    cargarMateriasPrimas();
}


    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colUnidadMedida.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));

        listaMateriaPrima = FXCollections.observableArrayList();
        tablaMateriaPrima.setItems(listaMateriaPrima);
        btnRegistrarMovimiento.setOnAction(e -> abrirFormularioRegistrarMovimiento());

        btnAgregar.setOnAction(e -> abrirFormularioAgregar());
        btnEditar.setOnAction(e -> abrirFormularioEditar());
        btnEliminar.setOnAction(e -> eliminarMateriaPrima());
    }

    private void cargarMateriasPrimas() {
        try {
            List<MateriaPrima> materias = materiaPrimaDAO.obtenerTodasMateriasPrimas();
            listaMateriaPrima.setAll(materias);
            lblMensaje.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error cargando materias primas: " + e.getMessage());
        }
    }

    private void abrirFormularioRegistrarMovimiento() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/MovimientoMateriaPrimaForm.fxml"));
        Parent root = loader.load();

        MovimientoMateriaPrimaFormController movimientoController = loader.getController();
        movimientoController.setDAOs(materiaPrimaDAO, movimientoMateriaPrimaDAO);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Registrar Movimiento de Materia Prima");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(400);
        stage.setMinHeight(350);

        stage.showAndWait();

        if (movimientoController.isGuardado()) {
            cargarMateriasPrimas();
        }
    } catch (Exception e) {
        e.printStackTrace();
        lblMensaje.setText("Error al abrir formulario de movimiento: " + e.getMessage());
    }
}


    private void abrirFormularioAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/MateriaPrimaForm.fxml"));
            Parent root = loader.load();

            MateriaPrimaFormController formController = loader.getController();
            formController.setMateriaPrimaDAO(this.materiaPrimaDAO);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Materia Prima");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMinWidth(400);
            stage.setMinHeight(350);

            stage.showAndWait();

            if (formController.isGuardado()) {
                MateriaPrima nueva = formController.getMateriaPrimaGuardada();
                if (nueva != null) {
                    listaMateriaPrima.add(nueva);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error al abrir formulario de agregar: " + ex.getMessage());
        }
    }

    private void abrirFormularioEditar() {
        MateriaPrima seleccionado = tablaMateriaPrima.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione una materia prima para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/MateriaPrimaForm.fxml"));
            Parent root = loader.load();

            MateriaPrimaFormController formController = loader.getController();
            formController.setMateriaPrimaDAO(materiaPrimaDAO);
            formController.cargarMateriaPrima(seleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Materia Prima");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMinWidth(400);
            stage.setMinHeight(350);

            stage.showAndWait();

            if (formController.isGuardado()) {
                cargarMateriasPrimas();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error al abrir formulario de editar: " + ex.getMessage());
        }
    }

    private void eliminarMateriaPrima() {
        MateriaPrima seleccionado = tablaMateriaPrima.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione una materia prima para eliminar.");
            return;
        }
        try {
            materiaPrimaDAO.eliminarMateriaPrima(seleccionado.getId());
            listaMateriaPrima.remove(seleccionado);
            lblMensaje.setText("Materia prima eliminada correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error al eliminar materia prima: " + ex.getMessage());
        }
    }
}
