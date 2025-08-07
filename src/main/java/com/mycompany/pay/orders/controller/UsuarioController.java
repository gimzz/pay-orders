package com.mycompany.pay.orders.controller;

import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.model.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
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

    /**
     * Método privado para validar un objeto Usuario antes de persistirlo o
     * actualizarlo.
     *
     * @param usuario Usuario a validar
     * @param esNuevo Indica si es nuevo usuario (true para agregar, false para
     * actualizar)
     */
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
}
