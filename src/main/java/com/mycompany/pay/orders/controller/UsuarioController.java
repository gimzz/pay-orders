package com.mycompany.pay.orders.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.model.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    private ObservableList<Usuario> listaUsuarios;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colUsuario;

    @FXML
    private TableColumn<Usuario, String> colRol;

    @FXML
    private TableColumn<Usuario, String> colEstado;

    @FXML
    private Label lblMensaje;

    public UsuarioController() {
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        cargarUsuarios();
    }

    public void agregarUsuario(Usuario usuario) throws SQLException, IllegalArgumentException {
        validarUsuario(usuario, true);
        usuarioDAO.agregarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorId(int id) throws SQLException {
        return usuarioDAO.obtenerUsuarioPorId(id);
    }

    public List<Usuario> obtenerTodosLosUsuarios() throws SQLException {
        return usuarioDAO.obtenerTodosLosUsuarios();
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException, IllegalArgumentException {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("El ID del usuario para actualizar debe ser mayor que 0");
        }
        validarUsuario(usuario, false);
        usuarioDAO.actualizarUsuario(usuario);
    }

    public void eliminarUsuario(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario a eliminar debe ser mayor que 0");
        }
        usuarioDAO.eliminarUsuario(id);
    }

    private void validarUsuario(Usuario usuario, boolean esNuevo) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña es obligatoria y debe tener al menos 6 caracteres");
        }
        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El rol de usuario es obligatorio");
        }
        if (esNuevo && usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
        }
    }

    @FXML
    private void initialize() {
        colUsuario.setCellValueFactory(cellData -> cellData.getValue().nombreUsuarioProperty());
        colRol.setCellValueFactory(cellData -> cellData.getValue().rolPropertyString());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        listaUsuarios = FXCollections.observableArrayList();
        tablaUsuarios.setItems(listaUsuarios);

        if (usuarioDAO != null) {
            cargarUsuarios();
        }
    }

    private void cargarUsuarios() {
        if (usuarioDAO == null) {
            lblMensaje.setText("Error: DAO no inicializado.");
            return;
        }
        try {
            List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
            listaUsuarios.setAll(usuarios);
            lblMensaje.setText("");
        } catch (SQLException e) {
            lblMensaje.setText("Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirFormularioAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/UsuarioForm.fxml"));
            Parent root = loader.load();

            UsuarioFormController formController = loader.getController();
            formController.setUsuarioDAO(usuarioDAO);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Nuevo Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setWidth(450);
            stage.setHeight(350);
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            stage.showAndWait();

            if (formController.isGuardado()) {
                Usuario nuevoUsuario = formController.getUsuarioGuardado(); // Debes crear este método en UsuarioFormController
                if (nuevoUsuario != null) {
                    listaUsuarios.add(nuevoUsuario);
                } else {
                    // Por seguridad, recargamos la lista si no podemos obtener usuario nuevo
                    cargarUsuarios();
                }
                lblMensaje.setText("Usuario agregado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir formulario de agregar usuario.");
        }
    }

    @FXML
    public void abrirFormularioEditar(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un usuario para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/pay/orders/view/UsuarioForm.fxml"));
            Parent root = loader.load();

            UsuarioFormController formController = loader.getController();
            formController.setUsuarioDAO(usuarioDAO);
            formController.cargarUsuario(seleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setWidth(450);
            stage.setHeight(350);
            stage.setMinWidth(400);
            stage.setMinHeight(300);

            stage.showAndWait();

            if (formController.isGuardado()) {
                cargarUsuarios();
                lblMensaje.setText("Usuario actualizado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            lblMensaje.setText("Error al abrir formulario de edición de usuario.");
        }
    }


    @FXML
    public void eliminarUsuario(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un usuario para eliminar.");
            return;
        }
        try {
            usuarioDAO.eliminarUsuario(seleccionado.getId());
            lblMensaje.setText("Usuario eliminado: " + seleccionado.getNombreUsuario());
            cargarUsuarios();
        } catch (SQLException e) {
            lblMensaje.setText("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
