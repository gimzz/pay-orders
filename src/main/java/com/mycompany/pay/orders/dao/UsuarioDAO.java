/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;
import com.mycompany.pay.orders.model.Usuario;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author gimz
 */
public interface UsuarioDAO {
   void agregarUsuario(Usuario usuario) throws SQLException;
    Usuario obtenerUsuarioPorId(int id) throws SQLException;
    List<Usuario> obtenerTodosLosUsuarios() throws SQLException;
    void actualizarUsuario(Usuario usuario) throws SQLException;
    void eliminarUsuario(int id) throws SQLException;
}
