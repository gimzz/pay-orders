package com.mycompany.pay.orders;

import com.mycompany.pay.orders.dao.UsuarioDAOImpl;
import com.mycompany.pay.orders.dao.UsuarioDAO;
import com.mycompany.pay.orders.model.Rol;
import com.mycompany.pay.orders.model.Usuario;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PayOrders {

    public static void main(String[] args) {
        try (Connection connection = com.mycompany.pay.orders.dao.DBConnection.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl(connection);

//            Usuario nuevoUsuario = new Usuario(0, "Giovana", "12345", Rol.ADMIN, true, LocalDateTime.now());
//            usuarioDAO.agregarUsuario(nuevoUsuario);
//            System.out.println("Usuario agregado.");

            Usuario u = usuarioDAO.obtenerUsuarioPorId(4);
            if (u != null) {
                System.out.println("Usuario leído de BD: " + u.getNombreUsuario() + ", Rol: " + u.getRol());
            } else {
                System.out.println("Usuario con ID 3 no encontrado.");
            }

            List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
            System.out.println("Lista de usuarios:");
            for (Usuario usuario : usuarios) {
                System.out.println("ID: " + usuario.getId() + ", Nombre: " + usuario.getNombreUsuario() + ", Rol: " + usuario.getRol());
            }

//            if (u != null) {
//                u.setPassword("nuevoPassword");
//                u.setActivo(false);
//                usuarioDAO.actualizarUsuario(u);
//                System.out.println("Usuario actualizado.");
//            }
//
//            usuarioDAO.eliminarUsuario(2);
//            System.out.println("Usuario eliminado si existía.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
