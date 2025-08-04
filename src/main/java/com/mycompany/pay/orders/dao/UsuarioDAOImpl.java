package com.mycompany.pay.orders.dao;

import com.mycompany.pay.orders.model.Usuario;
import com.mycompany.pay.orders.model.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


//METODOS
public class UsuarioDAOImpl implements UsuarioDAO {

    private Connection connection;

    public UsuarioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
   public void agregarUsuario(Usuario usuario) throws SQLException {
    String sql = "INSERT INTO system.usuarios (nombre_usuario, password_usuario, rol, activo, fecha_creacion) VALUES (?, ?, ?::rol_enum, ?, ?)";
    try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
        ps.setString(1, usuario.getNombreUsuario());
        ps.setString(2, usuario.getPassword());
        ps.setString(3, usuario.getRol().name());  
        ps.setBoolean(4, usuario.isActivo());
        ps.setTimestamp(5, Timestamp.valueOf(usuario.getFechaCreacion()));
        ps.executeUpdate();
    }
}

    @Override
    public Usuario obtenerUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM system.usuarios WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre_usuario"),
                        rs.getString("password_usuario"),
                        Rol.valueOf(rs.getString("rol")),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime()
                    );
                } else {
                    System.out.print("El usuario con ese id no existe");
                    return null;
                }
            }
        }
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() throws SQLException {
        List<Usuario> listaUsuarios = new ArrayList<>();

        String sql = "SELECT * FROM system.usuarios";

        try (PreparedStatement ps = this.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setPassword(rs.getString("password_usuario"));
                usuario.setRol(Rol.valueOf(rs.getString("rol")));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());

                listaUsuarios.add(usuario);
            }
        }

        return listaUsuarios;
    }
    
    
    @Override
public void actualizarUsuario(Usuario usuario) throws SQLException {
    String sql = "UPDATE system.usuarios SET nombre_usuario = ?, password_usuario = ?, rol = ?::rol_enum, activo = ? WHERE id = ?";
    try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
        ps.setString(1, usuario.getNombreUsuario());
        ps.setString(2, usuario.getPassword());
        ps.setString(3, usuario.getRol().name());
        ps.setBoolean(4, usuario.isActivo());
        ps.setInt(5, usuario.getId());
        ps.executeUpdate();
    }
}
    
    @Override
public void eliminarUsuario(int id) throws SQLException {
    String sql = "DELETE FROM system.usuarios WHERE id = ?";
    try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}


}
